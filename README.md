# Chirp
<div style="display: flex; justify-content: center; align-items: flex-start; flex-wrap: wrap; gap: 15px;">
    <img src="screenshot/preview/gif_mobile.gif" alt="GifMobile" width="200"/>
    <img src="screenshot/preview/chat_preview_1.gif" alt="GifMobile" width="600"/>
</div>

>Chirp is a modern chat application built with Kotlin Multiplatform, designed to provide a seamless and robust communication experience across multiple platforms. 
>Key features include:

# Project Features

*   **User Authentication:** Secure and straightforward user authentication, allowing users to easily sign up, log in, and manage their accounts.
*   **Real-time Chat:** Engage in instant conversations with real-time message delivery, ensuring users stay connected without delay.
*   **User Profile Management:** Personalize your experience by managing your user profile, including the ability to update details and potentially profile pictures.
*   **Notifications:** Stay updated with real-time notifications for new messages and important events, ensuring you never miss a beat.
*   **Offline-First Approach:** Enjoy uninterrupted access to your conversations with an offline-first pattern. Messages and data are available even without an active internet connection, syncing automatically once connectivity is restored.
*   **MVVM Architecture:** Built with the Model-View-ViewModel (MVVM) architectural pattern for a clean, maintainable, and testable codebase.
*   **Data Persistence:** Robust data persistence is achieved through:
    *   **Room:** For structured local storage of chat data and user information.
    *   **DataStore:** For storing simple key-value data, such as user preferences and authentication tokens.


# Chirp Project Tech Stack

This project is built using **Kotlin Multiplatform**, targeting **Android**, **iOS**, and **Desktop (JVM)**.

## Core Technologies:

*   **Kotlin Multiplatform:** Enables code sharing across different platforms.
*   **Jetpack Compose:** Used for building the UI on Android.
*   **Compose Multiplatform:** Extends Jetpack Compose for desktop (JVM) development.

## Architecture & Dependencies:

*   **Dependency Injection:**
    *   **Koin:** A lightweight dependency injection framework for Kotlin.
*   **Networking:**
    *   **Ktor:** A framework for creating asynchronous clients and servers in Kotlin, used here for network requests.
    *   **Kotlinx Serialization:** For handling JSON serialization and deserialization.
*   **Asynchronous Programming:**
    *   **Kotlinx Coroutines:** For asynchronous programming.
*   **Utilities:**
    *   **Kotlinx Datetime:** For handling date and time operations.
*   **AndroidX Libraries:**
    *   `core-ktx`: Core Android Kotlin extensions.
    *   `activity-compose`: For integrating Compose with Android activities.
    *   `lifecycle-runtime-ktx`, `lifecycle-viewmodel-compose`: For managing UI-related data in a lifecycle-aware manner.
    *   `datastore-preferences`: For simple key-value data storage.
*   **Databases:**
    *   **Room:** An Android Architecture Component for SQLite database access.
*   **Backend Services:**
    *   **Firebase:** Used for push notifications (Firebase Messaging).
*   **Image Loading:**
    *   **Coil:** An image loading library for Android and Compose.

## Desktop Specifics:

*   **UI Toolkit:**
    *   **Compose for Desktop:** As mentioned, used for the desktop UI.
*   **System Integration:**
    *   **jSystemThemeDetector:** For detecting system theme changes on desktop.

## Other Libraries:

*   **Logging:**
    *   **Kermit:** A multiplatform logging library.
*   **Permissions Handling:**
    *   **Moko Permissions:** For handling runtime permissions across different platforms.
*   **Adaptive UI:**
    *   **Material3 Adaptive:** For building adaptive UIs that respond to different screen sizes and configurations.
*   **Build Configuration:**
    *   **BuildKonfig:** For generating build configuration constants.


## Preview
<div style="display: flex; flex-wrap: wrap; gap: 15px;">
    <img src="screenshot/preview/gif_mobile.gif" alt="GifMobilw" width="400"/>
</div>

![Preview1](screenshot/preview/chat_preview_1.gif)
![Preview2](screenshot/preview/chat_preview_2.gif)
![Preview3](screenshot/preview/chat_preview_3.gif)
![Preview4](screenshot/preview/chat_preview_4.gif)

## Mobile Android/Ios
<h2>Login</h2>
<div style="display: flex; flex-wrap: wrap; gap: 15px;">
    <img src="screenshot/login/splash.png" alt="SplashMobile" width="200"/>
    <img src="screenshot/login/1.png" alt="SignIn" width="200"/>
    <img src="screenshot/login/2.png" alt="SingUp" width="200"/>
    <img src="screenshot/login/3.png" alt="RecoverPassword" width="200"/>
</div>
<br/>
<h3>Chat</h3>
<div style="display: flex; flex-wrap: wrap; gap: 15px;">
    <img src="screenshot/chat/chat_1.png" alt="Chat1" width="200"/>
    <img src="screenshot/chat/chat_2.png" alt="Chat2" width="200"/>
    <img src="screenshot/chat/chat_3.png" alt="Chat3" width="200"/>
    <img src="screenshot/chat/chat_4.png" alt="Chat4" width="200"/>
    <img src="screenshot/chat/chat_5.png" alt="Chat5" width="200"/>
</div>
<br/>
<h3>NewChat</h3>
<div style="display: flex; flex-wrap: wrap; gap: 15px;">
    <img src="screenshot/newchat/newchat_1.png" alt="NewChat1" width="200"/>
    <img src="screenshot/newchat/newchat_2.png" alt="NewChat2" width="200"/>
    <img src="screenshot/newchat/newchat_3.png" alt="NewChat3" width="200"/>
</div>
<br/>
<h3>Profile</h3>
<div style="display: flex; flex-wrap: wrap; gap: 15px;">
    <img src="screenshot/profile/profile_1.png" alt="Profile1" width="200"/>
    <img src="screenshot/profile/profile_2.png" alt="Profile2" width="200"/>
    <img src="screenshot/profile/profile_3.png" alt="Profile3" width="200"/>
</div>
<br/>
<h2>Tablet</h2>
<h3>Tablet Android chat</h3>

![Tablet1](screenshot/tablet/tablet_1.png)
![Tablet2](screenshot/tablet/tablet_2.png)
![Tablet3](screenshot/tablet/tablet_3.png)
![Tablet4](screenshot/tablet/tablet_4.png)
![Tablet5](screenshot/tablet/tablet_5.png)

## Desktop
### Desktop Mac, Linux and windows
![Desktop1](screenshot/desktop/desktop_1.png)
![Desktop2](screenshot/desktop/desktop_2.png)
![Desktop3](screenshot/desktop/desktop_3.png)
![Desktop4](screenshot/desktop/desktop_4.png)
![Desktop5](screenshot/desktop/desktop_5.png)
![Desktop6](screenshot/desktop/vid.gif)

