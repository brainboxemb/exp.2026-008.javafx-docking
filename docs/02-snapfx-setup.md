# 02 — SnapFX setup

The experiment pins SnapFX to upstream tag `v0.8.0`.

At the time this experiment was initialized, the upstream project documents Java 21+ / JavaFX 21+ and provides Maven publication metadata, but normal Maven Central consumption for the selected release still needs to be verified.

For the first local run, use a source checkout of the pinned tag and publish `snapfx-core` to the local Maven repository before building this experiment.

The experiment intentionally does not commit a third-party SnapFX JAR.

Once Maven Central availability is confirmed for the exact selected version, replace the local-publish prerequisite with the normal repository dependency and record that result here.
