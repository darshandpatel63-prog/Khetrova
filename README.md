# Khetrova

**Land. Farm. Build. Simplified.**

Khetrova is an Android toolkit for land measurement and unit conversion, irregular plot surveying, construction material estimates, farmer records, market-price information, water-tank calculations, and PDF reports.

## Build the app

1. Install Android Studio with a compatible JDK (JDK 17 or newer for the current Android Gradle Plugin).
2. Open this repository as an Android project and allow Gradle to sync.
3. Run the `app` configuration on an Android device or emulator, or build a debug APK with:

   ```bash
   ./gradlew assembleDebug
   ```

4. Find the debug APK at `app/build/outputs/apk/debug/app-debug.apk`.

## Release signing

The release build uses environment variables so signing credentials do not need to be stored in the repository:

- `KEYSTORE_PATH`: path to the upload keystore (defaults to `my-upload-key.jks` in the project root)
- `STORE_PASSWORD`: keystore password
- `KEY_PASSWORD`: key password

The configured key alias is `upload`. Keep keystore files and passwords private; never commit them.

## App identity

- App name: **Khetrova**
- Android application ID: `com.ddtechlabshub.khetrova`

## Branding

The scalable vector logo is available at [assets/branding/khetrova-logo.svg](assets/branding/khetrova-logo.svg). Android launcher icons are maintained under `app/src/main/res`.
