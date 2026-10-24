# Reference APK — preliminary static inventory

Reference filename: `PANEL SALIR PING V20_3.3.10.apk`

The uploaded file was recognized as an Android APK. A ZIP-level inventory showed:

- `AndroidManifest.xml`
- `classes.dex` and `classes2.dex`
- Native libraries under `lib/arm64-v8a/` and `lib/armeabi-v7a/`, including `libSunnyMod.so` and `libSunnyModDB.so`
- Android resources under `res/`

## What this establishes

The package contains DEX bytecode, Android resources, and native libraries for two CPU ABIs. This is a preliminary inventory only.

## What this does not establish

The inventory alone does not prove what each feature does, whether the app successfully modifies a game, or whether its claims are accurate. The APK has not been executed, and no claim is made that its native code has been fully reverse engineered. The simulator in this project is independently written and does not reuse the reference APK's code or libraries.

## Safe next steps for a university report

- Record package name, version, SDK declarations, permissions, and signing metadata using Android SDK Build Tools (`apkanalyzer`, `aapt`, and `apksigner`).
- Inspect decompiled code only in an isolated lab environment and only to document high-level behavior.
- Do not install an unknown APK on a primary phone or enter real account credentials.
- Keep screenshots and observations separate from assumptions; label unverified behavior clearly.
