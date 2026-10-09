# 01 — Experiment plan

## Question

Can a JavaFX docking framework provide a maintainable IDE-style workbench for the Event Timing Development Client without coupling individual views to the docking framework?

## Candidate 1 — SnapFX + Transit

Use SnapFX first with:

- Java 21;
- JavaFX 21;
- Maven;
- Transit 2.0.0 for the application look and feel.

Pin the upstream SnapFX source to tag `v0.8.0` for repeatability.

Transit is consumed from Maven Central. Its current source targets Java 17 and JavaFX 22, so JavaFX 21 compatibility is a qualification question rather than an assumed property.

## Architecture boundary

Representative panels are ordinary JavaFX nodes and must not know about SnapFX or Transit.

The intended experiment boundary is:

```text
panel/view
   |
   v
ordinary JavaFX Node
   |
   +-------------------+
   |                   |
   v                   v
SnapFX workbench    Transit scene theme
adapter             application shell
```

SnapFX floating windows create independent JavaFX scenes. The workbench therefore exposes a generic `Scene` decoration hook so the application shell can apply the same Transit and application CSS to the primary scene and every floating scene.

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
8. Apply Transit consistently to primary and floating scenes.
9. Combine Transit with SnapFX CSS/chrome.
10. Verify Transit 2.0.0 on Java 21 / JavaFX 21.
11. Keep Terminal and logs dark/monospace.
12. Verify a reproducible Maven dependency/build path.

## Qualification record

Detailed qualification cases and evidence rules are in [03 — Qualification](03-qualification.md).

## Exit criteria

SnapFX is suitable for a production spike when core docking behaviour is reliable, layout restore works across restart, panel implementations stay docking-independent, styling is acceptable, and the build/dependency path is reproducible.

Transit is suitable as the theme baseline when ordinary JavaFX controls, floating scenes and the SnapFX chrome coexist correctly on Java 21 / JavaFX 21 without requiring view-specific theme coupling.

If either candidate fails those criteria, record the failure and evaluate the next candidate only against the unresolved requirement.

## Candidate 2 — BentoFX + Transit

BentoFX 0.16.0 is evaluated against the same representative panels.

The BentoFX comparison uses the same visible default composition as the current
SnapFX workspace so the comparison is not biased by a different UI concept.

Default composition:

```text
left                            right
---------------------------     ---------------------------
TimingNode / Registration /     Registrations
Simulation (tabs)               ---------------------------
---------------------------     LogBook
Device Log                      ---------------------------
---------------------------     Tag Plot
Terminal
---------------------------
Client Log
```

BentoFX still uses its explicit root/branch/leaf model internally, with built-in
external/floating stages, Maven Central dependency and application-owned compact
CSS.

Do not add application-owned layout persistence during the first BentoFX pass.
If BentoFX has no equivalent of SnapFX JSON save/load, retain that as a candidate
difference rather than hiding it.

The BentoFX launcher is:

```text
io.github.brainboxemb.experimental.docking.BentoExperimentApplication
```
