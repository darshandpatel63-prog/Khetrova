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
- The current `AdMobNativeAdCard` composable is a labelled test/demo creative, not a real Google Mobile Ads native-ad request. Native ad slots are placed after manual sections, but production ad serving requires the Mobile Ads SDK and the app's own configured production ad-unit IDs. Do not treat the demo creatives as live ads.

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
