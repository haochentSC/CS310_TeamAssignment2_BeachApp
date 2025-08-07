# BeachApp Documentation

## Overview
BeachApp is an Android application that helps users discover and review beaches in Southern California. The app integrates Google Maps for location-based browsing, provides real‑time weather and wave data, and allows users to submit and manage beach reviews. Firebase Realtime Database is used for persisting users, beaches, and reviews.

## Key Features
- **User Authentication** – Registration and login screens validate credentials and store user accounts in Firebase.
- **Interactive Map** – `MapsActivity` displays markers for available beaches and supports filtering by common activities through a spinner control.
- **Beach Details** – `DisplayBeachActivity` fetches beach metadata from Firebase, retrieves weather and marine forecasts through the `WeatherAPI` helper, and highlights the two most popular activities at each beach.
- **Reviews** – Users can browse existing reviews, add new reviews with optional photos, and rate beaches. Activity tags selected during review submission are aggregated to track popularity.
- **Portfolio** – `PortfolioActivity` shows a user profile and a list of their submitted reviews. Users may update or delete reviews, with beach ratings recalculated accordingly.

## Architecture
The application follows a multi‑activity structure:
- `MainActivity` initializes Firebase and routes to the authentication flow.
- `LoginActivity` and `RegisterActivity` handle user credentials.
- `MapsActivity` hosts the Google Map and dispatches users to beach pages.
- `DisplayBeachActivity` presents weather information and actions for viewing or managing reviews.
- `ReviewActivity` shows all reviews for a beach and links to `AddReviewActivity` for creating a new one.
- `PortfolioActivity` serves as a user dashboard for review management.

Supporting classes include:
- `Beach`, `Review`, and `User` model classes.
- `WeatherAPI`, a static utility for retrieving current conditions and forecasts from Tomorrow.io and Open‑Meteo.

## Data Storage
- **Firebase Realtime Database** – Stores users, beaches, and reviews. Beach ratings are computed from stored reviews.
- **Local JSON (`activity_tag.json`)** – Maintains counts of user‑selected activity tags per beach to surface top activities.

## Build and Run
1. Install Android Studio (Giraffe or newer) with Android SDK 34 and a configured emulator or device.
2. Clone the repository and open it in Android Studio.
3. Ensure a valid Firebase configuration (`google-services.json`) is present under the `app` module.
4. Build and run the `app` module. `MainActivity` launches `LoginActivity` by default.

### Command Line
```
sh gradlew assembleDebug   # Builds the debug APK
sh gradlew test            # Runs unit tests (requires network access for Gradle distribution)
```

## Testing
Unit tests reside in `app/src/test/java` and cover URL construction for weather queries, credential validation, and map utilities. Instrumentation tests live under `app/src/androidTest/java` and exercise UI flows such as login, registration, map interactions, and review screens.

Attempting to run the test suite with `sh gradlew test` in a restricted environment may fail to download the Gradle distribution, resulting in an `HTTP/1.1 403 Forbidden` error.

## Directory Structure
```
app/
  src/main/java/com/example/beachapp/   # Activities, models, helpers
  src/main/res/                         # Layouts, drawables, values
  src/test/java/                        # Local unit tests
  src/androidTest/java/                 # Instrumentation tests
```

## Dependencies
- AndroidX AppCompat, Material Components, ConstraintLayout
- Google Play Services Maps & Location
- Firebase Realtime Database and Authentication
- OkHttp for HTTP requests
- Robolectric and Espresso for testing

## Future Enhancements
- Additional beach data and dynamic retrieval from backend sources
- Expanded activity tagging and recommendation system
- Offline caching for weather and review data

