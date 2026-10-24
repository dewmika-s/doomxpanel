# Arena Lab Simulator — Android educational project

**Purpose:** a polished, offline Android app for a university demonstration about game-assistance concepts, UI state, telemetry, configuration persistence, and event logging.

## Important scope

This is a standalone simulator. It does **not** connect to Free Fire or any other game. It does not read or modify game memory, inject code, automate aim, manipulate network traffic, or bypass anti-cheat. Values such as FPS and latency are illustrative UI values only.

## Features

- Dark neon dashboard built with native Android views
- Interactive concept toggles with persistent preferences
- Latency slider and simulated connection-quality label
- Start/pause and reset demo controls
- Timestamped local event log with clear action
- No network permission or third-party runtime dependency
- APK reference notes in `docs/APK_REFERENCE_NOTES.md`

## Build in Android Studio

1. Install Android Studio and a compatible JDK (JDK 17 is recommended).
2. Extract this ZIP.
3. In Android Studio, choose **Open** and select the `ArenaLabSimulator` folder (the folder containing `settings.gradle`).
4. Allow Gradle sync. Android Gradle Plugin 8.7.3 may download on first sync; an internet connection is required for that first dependency download.
5. Install Android SDK Platform 35 when prompted.
6. Connect an Android phone with USB debugging enabled, or start an emulator.
7. Select **app** and click **Run**.
8. To produce an APK: **Build → Build Bundle(s) / APK(s) → Build APK(s)**. Android Studio displays the output location.

This source bundle does not include a Gradle wrapper. Android Studio can import the project and use its configured Gradle installation; alternatively generate a wrapper from a local Gradle installation with `gradle wrapper`.

## Suggested university demo

1. Explain that the screen models a panel, but the controls only alter this app's own local state.
2. Toggle each feature and show the event log.
3. Change simulated latency and show how the quality label changes.
4. Restart the app to demonstrate saved toggle preferences.
5. Explain the ethical and technical distinction between a UI simulator and software that modifies a third-party game.

## Suggested evaluation checklist

- [ ] App opens without a login or network connection
- [ ] Feature toggles update and persist
- [ ] Latency slider updates the value and quality indicator
- [ ] Start/pause changes its label and adds a log entry
- [ ] Reset returns the latency to 42 ms and pauses the demo
- [ ] Clear log removes old entries
- [ ] No game integration or network permissions are present
