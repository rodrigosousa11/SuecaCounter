# SuecaCounter

**SuecaCounter** is an android application developed in Kotlin using Android Studio, designed to simplify score counting in the "Sueca" card game. The application utilizes Firebase Realtime Database to store game details and Firebase Authentication for user authentication.

## Features

- **Score Tracking:** Allows users to select the cards played and the winning team of each round, facilitating score counting in the Sueca game.

- **Game History:** Stores scores, winners, and date/time for each game in Firebase Realtime Database, providing users with a complete history of their matches.

- **User Authentication:** Utilizes Firebase Authentication to authenticate users, ensuring a secure and personalized environment.

## Project Setup

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/rodrigosousa11/SuecaCounter
   ```
   
2. **Firebase Configuration:**
   - Create a project on [Firebase Console](https://console.firebase.google.com/).
   - Set up Firebase Realtime Database to store game scores.
   - Activate and configure Firebase Authentication for user authentication.
   - Add Firebase configurations to the project in the `google-services.json` file in the project folder.

3. **Android Studio Setup:**
   - Open the project in Android Studio.
   - Verify and update dependencies in the `build.gradle` file.

4. **Running the App:**
   - Connect an Android device or start an emulator.
   - Run the app in Android Studio.

## Contributions

Contributions are welcome! Feel free to open issues and send pull requests to improve SuecaCounter.

## License

This project is licensed under the [MIT License](LICENSE).
