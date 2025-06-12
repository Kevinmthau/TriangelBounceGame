# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

- **Build the app**: `./gradlew build`
- **Install debug APK**: `./gradlew installDebug`
- **Clean build**: `./gradlew clean`
- **Run lint checks**: `./gradlew lint`

## Project Architecture

This is an Android game called "Triangle Bounce Game" built with Java and the Android SDK. The game uses a custom `SurfaceView` for real-time 2D graphics rendering.

### Core Game Architecture

- **MainActivity**: Entry point that creates and manages the `GameView`
- **GameView**: Main game loop using `SurfaceView` and `Runnable` for threading
  - Handles touch input for placing triangles and dropping the ball
  - Manages game state (triangle placement phase, ball drop phase, win condition)
  - Renders all game objects at 60fps in a separate thread
- **Game Objects**:
  - **Ball**: Physics-based ball with gravity, bounce, and collision detection
  - **Triangle**: Static red triangles that act as bounce surfaces
  - **Bucket**: Blue goal area where the ball must land to win

### Game Flow

1. Player taps to place 4 triangles on screen
2. After 4 triangles are placed, a bucket appears at the bottom
3. Player taps to drop a yellow ball from the top center
4. Ball bounces off triangles using realistic physics
5. Goal is to get the ball to land in the bucket

### Physics Implementation

The Ball class implements:
- Gravity and velocity physics
- Complex collision detection with triangle edges using distance-to-line-segment calculations
- Realistic bounce mechanics with surface normal reflection
- Energy dampening to simulate friction

### Threading Model

Uses Android's `SurfaceView` pattern:
- Game loop runs on separate thread via `Runnable`
- Synchronized access to triangle list for thread safety
- Activity lifecycle methods (onResume/onPause) control game thread

### Build Configuration

- Target SDK: 34 (Android 14)
- Min SDK: 21 (Android 5.0)
- Java 8 compatibility
- Uses AndroidX libraries (AppCompat, ConstraintLayout)