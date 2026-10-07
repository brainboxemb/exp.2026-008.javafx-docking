# 01 — Experiment plan

## Question

Can a JavaFX docking framework provide a maintainable IDE-style workbench for the Event Timing Development Client without coupling individual views to the docking framework?

## Candidate 1 — SnapFX

Use SnapFX first with Java 21, JavaFX 21, Maven and JMetro.

Pin the upstream SnapFX source to tag `v0.8.0` for repeatability.

## Representative panels

- TimingNode
- Registration
- Simulation
- Terminal
- Device Log
- Client Log
- Registrations
- LogBook
- Tag Plot

The Tag Plot is only a dynamic-view workload. It is not production plotting code.

## Qualification

1. Dock left/right/top/bottom.
2. Tab/center docking.
3. Resize splits.
4. Float and redock.
5. Drag between main and floating windows.
6. Save and restore layout.
7. Recreate panels from stable IDs.
8. Combine JMetro with SnapFX CSS.
9. Keep Terminal and logs dark/monospace.
10. Verify a reproducible Maven dependency/build path.

## Exit criteria

SnapFX is suitable for a production spike when core docking behaviour is reliable, layout restore works across restart, panel implementations stay docking-independent, styling is acceptable, and the build/dependency path is reproducible.

If those criteria fail, record the failure and evaluate the next candidate against the same representative workbench.
