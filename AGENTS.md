# Agent guidance

This repository is a JavaFX docking Proof of Principle for the Event Timing Development Client.

## Start here

Read, in order:

1. `README.md`;
2. `docs/01-plan.md`;
3. the active GitHub issue/PR when present.
4. `brainboxemb/brainboxemb.meta` shared workflow guidance for cross-project work.

## Scope

Keep this repository independent from production Event Timing code. Use representative mock panels to test workbench behaviour.

The current baseline is Java 21 + JavaFX 21 + Maven. SnapFX is the first candidate, not a predetermined winner.

## Rules

- Keep panel/view classes independent of docking-framework APIs where practical.
- Put candidate-specific integration behind a small workbench adapter layer.
- Do not commit third-party binary JARs when a reproducible source/build path is available.
- Record framework limitations instead of hiding them with application-specific workarounds.
- A failed experiment is valid evidence.
- Add another docking candidate only when SnapFX leaves a concrete material question unresolved.

## Cross-project authority

`brainboxemb.meta` owns the Experiment 008 status, cross-project question and any later production-handoff decision. This repository owns only the isolated PoP implementation and evidence.

Tracking issue: `brainboxemb/brainboxemb.meta#183`.
