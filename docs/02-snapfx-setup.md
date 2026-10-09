# 02 — SnapFX setup

The experiment pins SnapFX release tag `v0.8.0`.

Exact upstream source:

```text
Beowolve/SnapFX
tag: v0.8.0
commit: 6253f6443c74718b2bb8f861835dc97c6f5e374f
```

The published release contains `snapfx-core-0.8.0-0.jar`. The experiment Maven model therefore consumes:

```text
org.snapfx:snapfx-core:0.8.0-0
```

## Reproducible build path

Normal Maven Central availability for this exact artifact is not assumed.

CI uses the following controlled path:

1. checkout the experiment source;
2. set up Temurin Java 21;
3. checkout SnapFX tag `v0.8.0`;
4. assert that the tag resolves to exact commit
   `6253f6443c74718b2bb8f861835dc97c6f5e374f`;
5. run the upstream Gradle wrapper task
   `:snapfx-core:publishToMavenLocal`;
6. assert that Maven-local contains
   `org.snapfx:snapfx-core:0.8.0-0`;
7. run `mvn verify` for this experiment.

This keeps upstream source and dependency metadata intact and avoids committing
third-party binary JARs.

## Local use

A clean Maven checkout cannot resolve SnapFX directly because the pinned artifact
is not available from Maven Central.

On Windows, use the repository bootstrap:

```powershell
.\bootstrap.ps1
```

The script:

1. checks out the exact pinned SnapFX commit;
2. publishes `snapfx-core` to Maven local;
3. verifies that the expected JAR and POM exist under `~/.m2/repository`.

After that, ordinary Maven and NetBeans resolution works:

```text
mvn verify
mvn javafx:run
```

`run.ps1` combines bootstrap, verification and application launch.

This distinction is part of the experiment result: the dependency path is
reproducibly bootstrappable, but it is not yet a transparent clean-checkout Maven
dependency path.
