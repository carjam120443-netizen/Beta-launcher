# Beta Launcher 🚀

A clean, lightweight Android home launcher built from scratch with one goal: **install it and have it look good immediately.**

## ✨ What it includes

- 🏠 Android `HOME` launcher integration
- 📱 Automatic installed-app discovery
- 🔎 Instant app search
- ⬆️ Swipe up to open the app drawer
- ⬇️ Swipe down to return home
- 🕐 Large live clock and localized date
- ⭐ Clean favorites-style dock
- 🎨 Material 3 / system light-dark theme foundation
- 🧩 Adaptive launcher icon foundation
- ⚡ Lightweight Java implementation with minimal dependencies
- 🔒 No ads, trackers, accounts, or unnecessary network permissions
- 💾 Designed so launcher preferences can persist locally as customization grows
- 🤖 GitHub Actions automatically builds a debug APK

## 🎯 Design goals

Beta Launcher should feel like a real launcher on the **first boot**, not a demo app. The default experience intentionally avoids clutter, giant onboarding screens, placeholder UI, and unnecessary services.

Future releases will expand customization with persistent favorites, folders, widget hosting, notification indicators, gesture actions, wallpaper-aware theming, and a dedicated settings screen.

## 🛠️ Build

Requirements:

- Android Studio or a compatible Gradle environment
- JDK 17
- Android SDK 35

Build the debug APK:

```bash
./gradlew assembleDebug
```

The APK is produced at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

Every push and pull request also runs the GitHub Actions build and uploads the APK as a workflow artifact.

## 📦 Package

`com.carson.betalauncher`

## 📄 License

License terms will be added before the first stable release.
