# JavaFX Docking Experiment

Proof-of-principle repository for evaluating an IDE-style dockable JavaFX workbench for the Event Timing Development Client.

Cross-project coordination: [brainboxemb.meta Experiment 008](https://github.com/brainboxemb/brainboxemb.meta/blob/main/experiments/008-javafx-docking/README.md), tracking issue [brainboxemb.meta#183](https://github.com/brainboxemb/brainboxemb.meta/issues/183).

## Local quick start

SnapFX v0.8.0 is **not available from Maven Central**. On a clean checkout, plain `mvn verify`, `mvn javafx:run` and the initial NetBeans Maven import therefore cannot resolve `org.snapfx:snapfx-core:0.8.0-0` yet.

Bootstrap the pinned SnapFX dependency once:

```powershell
.\bootstrap.ps1
```

After that, use the repository Maven Wrapper; no global Maven installation is required:

```powershell
.\\mvnw.cmd verify
.\\mvnw.cmd javafx:run
```

For NetBeans, run `.\bootstrap.ps1` once and then reload/reopen the Maven project. Repeat the bootstrap only when the pinned SnapFX version changes or the local Maven repository is cleared.

To bootstrap, verify and launch in one command:

```powershell
.\run.ps1
```

## NetBeans

The executable main class is:

```text
io.github.brainboxemb.experimental.docking.DockingExperimentApplication
```

It is a plain Java launcher. The JavaFX lifecycle lives in
`DockingExperimentFxApplication`. This mirrors the Event Timing Development
Client setup so NetBeans Run and Debug can discover a normal main class.

## Goal

Determine whether a docking framework can provide a maintainable workbench for current and future Development Client tools such as:

- TimingNode, Registration and Simulation controls;
- Terminal;
- Device Log and Client Log;
- Registrations and LogBook;
- future realtime tag plots, inspectors and diagnostics.

The experiment is intentionally separate from the production Development Client. A successful result may inform a later migration; this repository is not the production implementation owner.

## Baseline

- Java 21
- JavaFX 21
- Maven
- Transit 2.0.0 as application look-and-feel baseline
- SnapFX as the first docking candidate
- BentoFX 0.16.0 as the second docking candidate

Transit and SnapFX are evaluated as replaceable infrastructure around ordinary JavaFX views. The application panels must not depend on either theme or docking-specific APIs unless the experiment proves that such coupling is unavoidable.

SnapFX is evaluated first, not assumed to be the final docking choice. BentoFX is the second candidate because its explicit IDE-style root/branch/leaf model is a materially different architecture rather than a near-duplicate API.

## Candidate launchers

NetBeans can run either plain Java launcher:

```text
SnapFX:
io.github.brainboxemb.experimental.docking.DockingExperimentApplication

BentoFX:
io.github.brainboxemb.experimental.docking.BentoExperimentApplication
```

Both use the same `PanelCatalog` mock views and Transit theme.

## Questions

The first SnapFX proof of principle must answer:

1. Can ordinary JavaFX views be docked as left/right/top/bottom/center tabs without leaking docking APIs into the view implementation?
2. Can panels be floated, moved between windows and docked again reliably?
3. Does the layout behave well with tables, log consoles and a continuously updating plot-like view?
4. Can split positions, tabs and floating-window geometry be saved and restored?
5. Can Transit style normal application controls while SnapFX supplies only the workbench/docking chrome?
6. Does Transit 2.0.0 behave correctly on the experiment's Java 21 / JavaFX 21 baseline?
7. Can Terminal, Device Log and Client Log remain dark/monospace inside a light workbench?
8. Is SnapFX practical to consume reproducibly from the Maven-based Development Client?

## Representative workbench

The PoP should use representative mock panels rather than production Event Timing code:

```text
+----------------------+----------------------+
| TimingNode /         | Registrations        |
| Registration /       |                      |
| Simulation           |                      |
+----------------------+----------------------+
| Terminal /           | LogBook              |
| Device Log /         |                      |
| Client Log           |                      |
+----------------------+----------------------+

Additional dockable:
- Tag Plot
```

The Tag Plot exists to exercise a dynamic/realtime view; it is not intended to implement production tag plotting.

## SnapFX dependency

SnapFX currently targets Java 21+ / JavaFX 21+. The first candidate is pinned to tag `v0.8.0`.

Until a normal Maven Central dependency is available for the chosen version, the experiment downloads the pinned upstream GitHub Release JAR, verifies its SHA-256 and installs it into Maven local with retained dependency metadata. No SnapFX source checkout or Gradle build is required.

## Theme dependency

Transit 2.0.0 is available from Maven Central as `com.pixelduke:transit:2.0.0`.

The current Transit source uses Java 17 and JavaFX 22. Compatibility with this experiment's Java 21 / JavaFX 21 baseline is therefore an explicit qualification result rather than an assumption.

## Windows helper

`bootstrap.ps1` owns the verified GitHub Release JAR download and Maven-local installation. `run.ps1` calls that bootstrap first, runs `mvn verify`, and then launches the experiment. Use `.\run.ps1 -VerifyOnly` to bootstrap and verify without opening the UI.

## Decision rule

Adoption requires at least:

- reliable docking, tabbing, floating and redocking;
- cross-window behaviour suitable for multi-monitor development use;
- stable layout persistence and recovery;
- acceptable CSS/theme integration with Transit;
- clean separation between workbench infrastructure and panel/view code;
- a reproducible Maven build path;
- a normal Maven/IDE dependency path, or an explicitly accepted production packaging/repository strategy for SnapFX.

A failed experiment is valid evidence. Do not work around framework limitations inside the mock views merely to make a candidate appear suitable.
