# 02 — SnapFX setup

The experiment pins SnapFX release `v0.8.0`.

Upstream release artifact:

```text
Beowolve/SnapFX
release: v0.8.0
artifact: snapfx-core-0.8.0-0.jar
size: 261481 bytes
SHA-256: 77c16fe87e795762aea4be0abb4ada12782503be1c9511c46b3e11b765354b3b
```

The experiment Maven model consumes:

```text
org.snapfx:snapfx-core:0.8.0-0
```

## Dependency metadata

The upstream SnapFX core build declares:

```text
com.google.code.gson:gson:2.10.1
```

The GitHub Release contains the core JAR but no Maven POM asset. Therefore the
experiment retains a small local bootstrap POM at
`bootstrap/snapfx-core-0.8.0-0.pom` so installing the release JAR does not lose
its Gson dependency metadata.

That POM is metadata only; no SnapFX source or binary is committed to this
repository.

## Reproducible bootstrap path

`bootstrap.ps1`:

1. downloads only the upstream GitHub Release JAR;
2. verifies the pinned SHA-256;
3. installs the verified JAR into Maven local with
   `maven-install-plugin:3.1.4`;
4. uses the retained bootstrap POM so Gson remains a transitive dependency;
5. verifies the expected JAR and POM exist under `~/.m2/repository`.

CI runs the exact same bootstrap on Ubuntu 24.04 and Windows 2025 before
`mvn verify`.

No SnapFX Git checkout and no SnapFX Gradle build are required.

## Local use

A clean Maven checkout cannot resolve SnapFX directly because the pinned artifact
is not available from Maven Central.

Run:

```powershell
.\bootstrap.ps1
```

After that, ordinary Maven and NetBeans resolution works:

```text
mvn verify
mvn javafx:run
```

`run.ps1` combines bootstrap, verification and application launch.

This distinction remains part of the experiment result: the dependency is
reproducibly bootstrappable from an official release artifact, but it is not yet
a transparent Maven Central dependency.
