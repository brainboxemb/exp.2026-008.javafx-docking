# 03 — Qualification

Experiment 008 separates machine-verifiable evidence from interaction evidence that must be observed in a real desktop session.

## Current baseline

- Java 21
- JavaFX 21.0.10
- Transit 2.0.0 from Maven Central
- SnapFX 0.8.0-0 from upstream GitHub Release `v0.8.0`
- pinned release JAR SHA-256 `77c16fe87e795762aea4be0abb4ada12782503be1c9511c46b3e11b765354b3b`

## Automated qualification

### Q-01A — Reproducible bootstrap build path

Question: can the experiment bootstrap and build from a clean runner without committing third-party binaries?

Pass when:

- the exact SnapFX release JAR is downloaded;
- its pinned SHA-256 is verified;
- `snapfx-core` is installed into Maven local with retained Gson dependency metadata;
- Transit resolves from Maven Central;
- `mvn verify` succeeds.

Run on both:

- Ubuntu 24.04;
- Windows 2025.

Status: **passed** on source `fcce3ae9b7a8811b6c57c2352b707fb392d34ea2`.

Earlier evidence established the source-build route. Issue #5 replaces that heavier route with the official GitHub Release JAR bootstrap; its dedicated CI run is the authority for the release-artifact path.

### Q-01B — Clean Maven/IDE dependency resolution

Question: can a developer clone the Maven project and immediately run `mvn verify`, `mvn javafx:run`, or import it into NetBeans without a dependency bootstrap step?

Status: **not passed**.

Observed result on a clean local Maven repository:

```text
Could not find artifact org.snapfx:snapfx-core:jar:0.8.0-0 in central
```

SnapFX v0.8.0 is not published to Maven Central. The current PoP therefore
requires `bootstrap.ps1` (or the equivalent CI release-artifact install step) before normal
Maven/NetBeans dependency resolution works.

Impact: this does not invalidate the docking behaviour PoP, but it is a
production-adoption constraint. Production use should require either normal
upstream repository publication, a controlled internal Maven repository, or an
explicitly accepted alternative packaging strategy.

Tracked by experiment issue #3.

### Q-02 — Stable panel identity and layout reconstruction

Question: can a saved SnapFX layout reconstruct ordinary JavaFX panels through stable application-owned IDs?

Pass when the automated round-trip test:

- creates a split/tab layout;
- serializes it;
- creates a fresh `DockGraph`;
- recreates nodes through the `DockNodeFactory`;
- restores every expected panel ID.

This test deliberately uses plain JavaFX `Group` nodes so it does not depend on a visible desktop or JavaFX control toolkit.

Status: **passed** on source `fcce3ae9b7a8811b6c57c2352b707fb392d34ea2`.

Evidence: workflow run [37733421797](https://github.com/brainboxemb/exp.2026-008.javafx-docking/actions/runs/37733421797) ran `SnapFxLayoutPersistenceTest` on both Ubuntu and Windows: 1 test, 0 failures, 0 errors, 0 skipped.

## Interactive qualification

These cases use the representative experiment application on Windows.

### Q-03 — Dock and tab

Question: can each representative panel be moved to left, right, top, bottom and center/tab targets?

Pass when the panel remains usable and no view-specific code is needed.

### Q-04 — Split resize

Question: can split dividers be resized repeatedly with the table, logs and Raw Data visible?

Pass when resizing remains responsive and content is not clipped into an unusable state.

### Q-05 — Float and redock

Question: can Terminal, Device Log, Client Log and Raw Data be floated and redocked?

Pass when the same panel instance remains usable and the operation does not lose its content.

### Q-06 — Cross-window and multi-monitor

Question: can a floating panel move between windows/screens and dock back into the main workbench?

Pass when drag/drop targets remain usable across windows and monitor boundaries.

### Q-07 — Layout persistence

Question: do tab groups, split ratios and floating-window geometry survive save, application restart and load?

Pass when the restored layout is functionally equivalent to the saved layout and all panel IDs resolve through the application factory.

### Q-08 — Transit on primary and floating scenes

Question: is Transit applied consistently to the main workbench and newly created SnapFX floating scenes?

Pass when ordinary controls have the same light theme in both contexts and the application CSS is present in both scenes.

Observed on Windows before issue #13: **not passed**. Transit was applied after
`stage.setScene(...)` but before `stage.show()`, causing native-frame access
while JavaFX `tkStage` was still null. The UI still rendered, but startup logged
two native-window `NullPointerException` traces.

Issue #13 changes the lifecycle so Transit CSS is prepared before show and native
window theming is applied only after the Stage is showing. This case remains
open until the corrected Windows run is observed without those exceptions.

### Q-09 — Dark terminal/log content

Question: can Terminal, Device Log and Client Log remain dark/monospace inside the Transit light theme?

Pass when floating/docking those panels does not replace their dark content styling.

### Q-10 — Raw Data selection context

Question: does the application-owned Raw Data pane follow meaningful application
selection independently of the docking framework?

Pass when:
- the pane shows a representative latest event before a table selection;
- selecting a Registrations row shows that interpreted registration's
  underlying message;
- selecting a LogBook row shows that committed record's raw message;
- moving/docking the views does not break that shared selection context.

## Evidence rule

Do not mark an interactive case passed from source inspection alone. Record the Windows run, exact experiment commit and observed result after the case has actually been executed.

## BentoFX interactive comparison

### B-02 — Empty source area pruning

Question: when the only dockable in a BentoFX leaf is moved into another leaf,
does the empty source area disappear and does the parent split redistribute the
freed space automatically?

Reference action:

```text
Terminal -> Device Log (center/tab drop)
```

Pass when Terminal becomes a tab beside Device Log, the old Terminal area is
removed from the layout, and the remaining left-column areas expand without
application code resizing individual split dividers.

The top-level left/right comparison columns remain explicitly non-pruning so the
overall two-column workspace stays stable.

### B-03 — Compact header metrics

Question: can BentoFX tab/header chrome be made as compact as the SnapFX
comparison without shrinking normal UI text?

The experiment uses BentoFX's supported Header factory and keeps the font at
12 px while reducing the internal GridPane padding from 6 px on every side to
2 px vertical / 6 px horizontal. The outer application CSS header padding is
also removed.

Pass when the tab text remains comfortably readable and the visible header
height is close to the compact SnapFX workbench.

### Q-11 — SnapFX compact tab typography

Question: can SnapFX docking tabs use the same typography and density as the
application workbench while retaining float and close actions?

The experiment overrides SnapFX's 11 px tab-label default with 12 px JavaFX
`System`, normal weight, 24 px tab height, compact horizontal padding and
14 px float/close controls.

Pass when the tabs visually match the surrounding workbench and their float and
close actions remain easy to use.

### Q-12 — Local tab-group scope

Question: does the TimingNode / Registration / Simulation tab strip belong only
to the top-left control area instead of spanning the full workbench?

The default SnapFX composition now builds the split tree before applying CENTER
docking for local tab groups.

Automated evidence verifies that:
- the graph root is a `DockSplitPane`;
- TimingNode, Registration and Simulation share one `DockTabPane`;
- that tab pane is nested inside the split tree and is not the graph root.

Interactive pass condition: the control-tab strip is visible only above its
local panel area.

### Q-13 — Application stylesheet priority

Question: do application-owned SnapFX tab metrics actually override SnapFX's
built-in stylesheet after `SnapFX.initialize(...)`?

The application stylesheet is now explicitly moved to the end of each managed
Scene stylesheet list during decoration. This applies to the primary Scene and
floating SnapFX Scenes.

Pass when SnapFX dock-tab labels render with the configured 12 px System font
instead of SnapFX's built-in 11 px rule.

### Q-14 — Separate default tool areas

Question: does the SnapFX default layout expose the same simultaneously visible
tool areas as the BentoFX comparison?

Pass when:
- TimingNode / Registration / Simulation are the only default tab group;
- Device Log, Terminal and Client Log are separate left-column areas;
- Registrations, LogBook and Raw Data are separate right-column areas.

### Q-15 — Single-panel floating chrome

Question: does a floating SnapFX window avoid showing the same panel title in
both the floating-window title bar and an inner DockNode header?

The adapter hides the inner DockNode header when a floating window contains one
DockNode. If the floating graph later contains multiple nodes, inner headers are
restored automatically after the floating layout rebuild.

Pass when a single floated panel shows one title/chrome row while multi-panel
floating layouts retain enough internal chrome for docking.
