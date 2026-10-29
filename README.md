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

## Market-price data coverage

- The market-price screen reads daily min/max/modal price records from the Government of India Open Government Data (OGD) / AGMARKNET feed when the API is reachable and a returned record matches a configured Indian market.
- Each displayed quote shows the source and the market's reported date. Daily price records are not tick-by-tick exchange quotes, and arrival volumes or day-to-day change are not claimed unless a source supplies them.
- Currency conversion rates are a separate feed and do not count as evidence that a crop price is live.
- International market profiles are currently labels/placeholders for future integrations, not live price feeds. Their seeded example prices are no longer displayed as current market quotes. Proper global coverage requires separate verified market data sources (some require provider API keys or licensing).

## User manual and measurement inputs

- Open **Menu • All Tools → How to Use Khetrova — Full Feature Manual** for the separate, step-by-step guide covering land units, map survey, CAD, BOQ, market data, farmer tools, Vault, and PDF export.
- Map surveys no longer start with a preloaded sample parcel. Enter a real location and mark the actual boundary points before reviewing area or perimeter. The 2D Plot CAD screen no longer preloads sample side lengths; enter the measured sides. A land-value estimate is shown only after a rate per square foot is supplied.
- The manual uses Google ML Kit on-device translation for its supported languages. The app's language list contains more languages than ML Kit supports; unsupported target languages fall back to English. The first translation for a supported language downloads a language model, which may use mobile data. Machine translation can be imperfect.
- The How to Use manual uses Google Mobile Ads native-ad slots after its guide sections, but the slots currently use Google's official **test** native-ad unit ID and the manifest's test App ID. Test ads do not earn revenue. Before publishing, replace both with Khetrova's own approved production App ID and native-ad unit ID, and complete the required consent/privacy setup. Other screens still use the older labelled test/demo ad-card UI.

## Build and publish a signed APK from GitHub Actions

Run **Actions → Build Khetrova Release APK → Run workflow**. This workflow is manual-only, builds the release APK without Gradle build cache, and uploads the APK plus a SHA-256 checksum directly to a GitHub Release. It does not upload Actions artifacts.

Before the first run, create one permanent Android upload keystore on a trusted device and keep a secure offline backup. **Never commit the keystore or its passwords to the repository.** For example, in Termux after installing a JDK that includes `keytool`:

```sh
keytool -genkeypair -v -keystore khetrova-upload-key.jks -alias upload -keyalg RSA -keysize 2048 -validity 10000
base64 -w 0 khetrova-upload-key.jks
```

Copy the base64 output securely, then add these repository Actions secrets under **Settings → Secrets and variables → Actions**:

- `KEYSTORE_BASE64` — the complete base64 output
- `STORE_PASSWORD` — the keystore password
- `KEY_ALIAS` — the alias used when creating the key (example: `upload`)
- `KEY_PASSWORD` — the key password

Use the same keystore for every future release. If you lose it or replace it, Android generally will not allow the new APK to update an installation signed with the old key. Do not post the base64 value or passwords in chat, issues, commits, or screenshots.

On each manual run, the workflow uses the GitHub Actions run number as Android `versionCode`, and the selected release tag as `versionName`. Leave the release-tag field blank to auto-generate `v1.0.RUN_NUMBER`, or enter a new unique tag. The workflow intentionally fails before building if signing secrets are missing.

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
