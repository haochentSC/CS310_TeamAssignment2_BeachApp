# BeachApp

BeachApp is an Android application for discovering and reviewing beaches in Southern California. The project demonstrates a multi-activity architecture, Firebase-backed data storage, and integration with Google Maps and external weather services.

## Features
- **User Authentication** – Register and log in with credentials stored in Firebase Realtime Database.
- **Interactive Map** – Browse available beaches on a Google Map and filter by common activities.
- **Beach Details** – View real-time weather conditions, top activities, and existing reviews.
- **Reviews** – Submit ratings, text, and photos; edit or delete submissions from your profile.
- **User Portfolio** – Access a personalized dashboard listing all of your reviews.

## Requirements
- Android Studio Giraffe (or newer)
- Android SDK 34 with an emulator or physical device
- Firebase configuration (`google-services.json`) in the `app` module

## Quick Start
1. Clone the repository.
2. Place your `google-services.json` in `app/`.
3. Open the project in Android Studio.
4. Build and run the `app` module.
5. Log in with the test credentials `ss` / `ss` or register a new account.

## Testing
Run the unit and espresso test suite from the test folders. 

## Documentation
For a deeper exploration of the architecture, data model, and development workflow, see [DOCUMENTATION.md](DOCUMENTATION.md).
