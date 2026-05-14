# SandSim — Functional Requirements

This document lists functional requirements for the SandSim project. Each requirement is written to be testable and includes acceptance criteria, inputs/outputs, and relevant edge cases.

## 1. Overview
SandSim is an interactive 2D particle simulation that models sand, water, smoke, and stone. Users interact via keyboard and mouse to place particles and manipulate the camera. The system simulates particle behavior each tick and renders the scene.

## 2. Actors
- User: interacts with the application via keyboard and mouse.
- System: the running application that updates the simulation and renders frames.
- Test harness / automated tests: code-level tests that assert world state and logic.

## 3. High-level functional areas
- Input handling (keyboard + mouse)
- Particle creation & placement
- Particle physics/behavior update
- Rendering
- Camera control
- Resource loading (textures/models)
- Persistence (none required in baseline)

## 4. Requirements (user-facing)

R1: Launch the application
- Description: User can start the application and a window appears with the simulation.
- Inputs: user runs `com.apet2929.game.Launcher` or equivalent packaged app.
- Outputs: an application window with a visible grid area and (initially) no active particles.
- Acceptance: The window opens without crashing; UI elements (grid or background) are visible.
- Edge cases: If native libraries fail to initialize (GLFW, LWJGL), the app must exit with a clear error message.

R2: Place particles with mouse
- Description: Left-clicking on the simulation places particles of the currently selected type.
- Inputs: left mouse click (or hold to paint) at screen position.
- Outputs: appropriate particles appear on grid cells under the cursor.
- Acceptance: After a single click, at least one cell near the cursor contains a particle of the selected type. Holding and dragging fills multiple cells.
- Edge cases: Clicking outside the grid should be ignored or mapped to nearest valid cell.

R3: Select particle types with number keys
- Description: Numeric keys map to particle types (0..4) and change the `selectedParticleType`.
- Inputs: key press for digits 0..9 (only 0..4 are mapped meaningfully).
- Outputs: the sim's `selectedParticleType` is updated.
- Acceptance: Pressing `1` selects Sand, `2` Water, `3` Smoke, `4` Stone, `0` clears/empties. The UI (console or small indicator if present) may reflect selection.
- Edge cases: Pressing other keys does nothing.

R4: Brush and spray inputs
- Description: Spacebar triggers a larger brush placement; left mouse with brushSize places a square of particles.
- Inputs: spacebar; left click with normal or larger brush enabled.
- Outputs: multiple particles added in brush area.
- Acceptance: After pressing space at a cursor location, a larger cluster (brushSize * 10) of particles is created.

R5: Camera control with keyboard
- Description: Arrow keys pan the camera; `.` and `/` zoom in/out.
- Inputs: arrow keys, period, slash.
- Outputs: the view updates (panning/zoom). Objects' on-screen positions change accordingly.
- Acceptance: Pressing left moves the visible view left (world appears to shift right relative to window). Zoom keys change visible scale.

R6: Particle dynamics
- Description: Particles behave according to their type each simulation tick.
  - Sand: falls, collides with solids, piles up.
  - Water: falls and disperses/spreads horizontally.
  - Smoke: rises and disperses.
  - Stone: immovable solids.
- Inputs: simulation step (tick) triggered by engine loop.
- Outputs: particle positions update; world state mutates accordingly.
- Acceptance: After placing sand above empty space and advancing simulation for a few ticks, the sand's Y position decreases (it falls). After placing water, particles spread horizontally.
- Edge cases: Physics should not allow out-of-bounds array access; world must check indices before swaps.

R7: Rendering pipeline
- Description: The renderer draws the grid lines (optionally) and all visible particles with the correct textures.
- Inputs: model, particle textures, camera transform.
- Outputs: pixels rendered to the window showing textures at expected screen locations.
- Acceptance: Each placed particle appears with correct texture. Grid lines draw or hide depending on zoom.

R8: Asset loading resilience
- Description: Missing textures fall back to a NOT_FOUND texture rather than crash.
- Inputs: request to load texture by name.
- Outputs: either the requested texture or a default ``NOT_FOUND`` texture.
- Acceptance: If texture asset name is invalid, the system uses a default placeholder texture and logs the event.

## 5. Requirements (developer / API-level)

R9: World API
- Methods (must exist and behave as documented):
  - `World.spawnParticle(ParticleType type, int x, int y)` — creates and places a particle of the given type at grid coords.
  - `World.getAt(int x, int y)` — returns the particle at coords or null if out-of-bounds.
  - `World.setAt(int x, int y, Particle p)` — sets particle at coords (checks bounds).
  - `World.update()` — advances particle logic one tick.
- Acceptance: Unit tests should be able to call these methods and assert resulting grid contents.

R10: Particle factory
- Description: `ParticleType.createParticleByMatrix(int x,int y)` must reliably create new instances of the appropriate subclass and set positions.
- Acceptance: For each `ParticleType` enum value, the factory returns a `Particle` with `getType()` matching the enum value.

R11: Deterministic behavior for tests
- Description: Particle behavior should be deterministic given the same seed and timing; test harness may simulate ticks by calling `World.update()` repeatedly.
- Acceptance: Tests that run `update()` in a deterministic environment reproduce the same particle positions each run.

R12: No unsafe reflection dependence
- Description: Subclasses should not rely on fragile reflection to determine type (the refactor must ensure explicit type assignment in constructors).
- Acceptance: Particle base class uses explicit `ParticleType` (no valueOf(getClass...) calls remain).

## 6. Non-functional constraints (short)
- Performance: The engine should maintain interactive frame rates (target depends on machine, but simulation loop must be optimized for modest grid sizes).
- Memory: Native buffers must be freed after use (no native mem leaks during long runs).
- Robustness: Application should not crash on malformed input (e.g., clicking outside grid) and must validate bounds before array access.

## 7. Error modes and acceptance messages
- On native library load failure: show a clear error message and exit gracefully.
- On missing assets: log warning and use fallback texture.

## 8. Acceptance test matrix (high-level)
- UT1: `World.spawnParticle` creates correct particle type in the grid (unit test).
- UT2: `ParticleType.createParticleByMatrix` returns instance with matching `getType()` (unit test for each type).
- UT3: `World.update` causes sand to move down after a few steps (integration unit test: place sand above empty cell, call update N times, assert new coordinates).
- UT4: Camera pans on arrow key input (unit test uses mocked `WindowManager` to simulate key press and asserts camera moved).
- UT5: AssetCache fallback returns NOT_FOUND texture when loader fails (unit test mocking loader to throw exception).

## 9. Edge cases and robustness tests
- Boundary placements near edges: placing particles at x=0 or y=0 should be allowed if in range; placing outside should be ignored.
- Concurrent modifications: ensure `World.update()` iterates over a stable list of particles to avoid double-updating in a single tick.
- Large brush sizes: verify performance does not degrade catastrophically for moderate brush sizes.
