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

Question: can split dividers be resized repeatedly with the table, logs and Tag Plot visible?

Pass when resizing remains responsive and content is not clipped into an unusable state.

### Q-05 — Float and redock

Question: can Terminal, Device Log, Client Log and Tag Plot be floated and redocked?

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

### Q-09 — Dark terminal/log content

Question: can Terminal, Device Log and Client Log remain dark/monospace inside the Transit light theme?

Pass when floating/docking those panels does not replace their dark content styling.

### Q-10 — Dynamic Tag Plot

Question: does the continuously updating Tag Plot remain live while tabbing, resizing, floating and redocking?

Pass when updates continue without duplicate timelines, freezes or layout corruption.

## Evidence rule

Do not mark an interactive case passed from source inspection alone. Record the Windows run, exact experiment commit and observed result after the case has actually been executed.
