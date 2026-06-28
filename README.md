# Flappy Bird (Java)
🚧 **v1 — work in progress.**

This release is backend logic plus a throwaway test harness to make sure it all works. The real frontend is being built separately by my partner Hassan and will land in a future release.

## Quick start

Download `FlappyBird.zip` from this repo, extract it, then run the `.exe` inside — no JDK or JavaFX setup needed, everything's bundled in.

**Controls**

| Action | Key |
|---|---|
| Jump | `Space` or click |
| Restart (after game over) | `R` or click |

## What's in this version

- `Bird`, `Pipe`, `Board` — the full game logic, no UI dependencies at all
- `FlappyBirdApp` — a bare-bones JavaFX window used to playtest the backend (gravity feel, collisions, scoring, spawn timing). **Not the real frontend** — just rectangles and a circle on a canvas, built to verify the logic works before handing it off.

## Coming in later releases

- Real frontend/UI (Hassan)
- Sprites, animations, sound
- Polished menus, restart flow, possibly difficulty scaling

## Classes

**`Bird`** — tracks vertical position and velocity, applies gravity each frame, handles the jump impulse.
- `update()` — applies gravity, moves the bird
- `jump()` — upward velocity kick
- `getBounds()` — hitbox for collisions

**`Pipe`** — one pipe pair (top + bottom) with a gap, scrolls left each frame.
- `update()` — moves left
- `isOffScreen()` — true once fully past the left edge
- `getTopBounds()` / `getBottomBounds()` — hitboxes
- `isScored()` / `setScored(boolean)` — whether this pipe has already counted toward the score

**`Board`** — owns the bird and the pipe list, runs one frame of logic per `update()` call: movement, collisions, cleanup, scoring, spawning.
- `update()` — advances one frame; no-ops once the game is over
- `getScore()` / `isGameOver()` / `getPipes()`

## Running from source

Needs the JavaFX SDK (not bundled with the JDK). Add to your run config:

```
--module-path "/path/to/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml
```

Run `FlappyBirdApp.main()`.
## Contributors: 
- Amr ( Backend and game logic )
- Claude (Frontend)
-  AlphaTesterAnas ( Tester ) 
