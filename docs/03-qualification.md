# 03 — Qualification

Experiment 008 separates machine-verifiable evidence from interaction evidence that must be observed in a real desktop session.

## Current baseline

- Java 21
- JavaFX 21.0.10
- Transit 2.0.0 from Maven Central
- SnapFX 0.8.0-0 built from upstream tag `v0.8.0`
- exact SnapFX source commit `6253f6443c74718b2bb8f861835dc97c6f5e374f`

## Automated qualification

### Q-01 — Reproducible dependency and build path

Question: can the experiment build from a clean runner without committing third-party binaries?

Pass when:

- exact SnapFX tag/commit is checked;
- `snapfx-core` is published to Maven local from that source;
- Transit resolves from Maven Central;
- `mvn verify` succeeds.

Run on both:

- Ubuntu 24.04;
- Windows 2025.

### Q-02 — Stable panel identity and layout reconstruction

Question: can a saved SnapFX layout reconstruct ordinary JavaFX panels through stable application-owned IDs?

Pass when the automated round-trip test:

- creates a split/tab layout;
- serializes it;
- creates a fresh `DockGraph`;
- recreates nodes through the `DockNodeFactory`;
- restores every expected panel ID.

This test deliberately uses plain JavaFX `Group` nodes so it does not depend on a visible desktop or JavaFX control toolkit.

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
