# JavaFX Docking Experiment

Proof-of-principle repository for evaluating an IDE-style dockable JavaFX workbench for the Event Timing Development Client.

Cross-project coordination: [brainboxemb.meta Experiment 008](https://github.com/brainboxemb/brainboxemb.meta/blob/main/experiments/008-javafx-docking/README.md), tracking issue [brainboxemb.meta#183](https://github.com/brainboxemb/brainboxemb.meta/issues/183).

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
- JMetro as application look-and-feel baseline
- SnapFX as the first docking candidate

SnapFX is evaluated first, not assumed to be the final choice. Other docking libraries may be added when a concrete unresolved question requires comparison.

## Questions

The first SnapFX proof of principle must answer:

1. Can ordinary JavaFX views be docked as left/right/top/bottom/center tabs without leaking docking APIs into the view implementation?
2. Can panels be floated, moved between windows and docked again reliably?
3. Does the layout behave well with tables, log consoles and a continuously updating plot-like view?
4. Can split positions, tabs and floating-window geometry be saved and restored?
5. Can JMetro style the application controls while SnapFX supplies only the workbench/docking chrome?
6. Can Terminal, Device Log and Client Log remain dark/monospace inside a light workbench?
7. Is SnapFX practical to consume reproducibly from the Maven-based Development Client?

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

Until a normal Maven Central dependency is verified for the chosen version, the experiment should consume SnapFX through a reproducible local-publish step from the pinned upstream tag rather than committing third-party binaries.

## Decision rule

Adoption requires at least:

- reliable docking, tabbing, floating and redocking;
- cross-window behaviour suitable for multi-monitor development use;
- stable layout persistence and recovery;
- acceptable CSS integration with JMetro;
- clean separation between workbench infrastructure and panel/view code;
- a reproducible Maven build path.

A failed experiment is valid evidence. Do not work around framework limitations inside the mock views merely to make a candidate appear suitable.
