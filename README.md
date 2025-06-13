# 📸 CameraX Android Application

This repository contains an Android application built using **CameraX**, the modern camera API provided by Android Jetpack. The app demonstrates how to implement essential camera functionalities such as live preview, photo capture, and video recording with clean architecture and modern Android development practices.

---

## 📌 Table of Contents

- [About the Project](#about-the-project)
- [Key Features](#key-features)
- [Tech Stack](#tech-stack)
- [Setup & Installation](#setup--installation)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Permissions](#permissions)
- [Future Enhancements](#future-enhancements)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

---

## 📖 About the Project

The **CameraX Android Application** is a sample project aimed at providing a seamless, user-friendly camera experience with minimum code and maximum efficiency. It serves as a starting point for developers who wish to understand how to integrate camera features into Android apps using **CameraX** API, which is designed to be lifecycle-aware and compatible across most Android devices.

---

## ✨ Key Features

- 📷 **Live Camera Preview** – Display real-time camera feed using CameraX Preview use case.
- 📸 **Capture Photos** – Take high-resolution photos and store them in external/internal storage.
- 🎥 **Record Videos** – Record HD video using the VideoCapture use case (if implemented).
- 🔁 **Camera Switching** – Toggle between front and back cameras.
- ♻️ **Lifecycle-Aware Camera** – Automatically handles camera release and rebinding during app lifecycle changes.
- 🧭 **Orientation Handling** – Maintains correct preview and capture orientation.
- 💡 **Clean UI** – Built using Jetpack Compose or XML for a modern interface.

---

## 🛠 Tech Stack

| Component      | Technology        |
|----------------|-------------------|
| Language       | Kotlin            |
| UI Framework   | Jetpack Compose / XML (use the one you used) |
| Camera API     | Jetpack CameraX   |
| Android Tools  | ViewModel, Lifecycle, ActivityResult APIs |
| Min SDK        | 21+               |
| Target SDK     | 34+               |
| IDE            | Android Studio Giraffe or later |

---

## 🧑‍💻 Setup & Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/CameraX-Android-App.git
Open the project in Android Studio.

Build the project and let Gradle resolve dependencies.

Connect a device or use an emulator with a camera.

Run the app using the Run button or Shift + F10.

▶️ Usage
Launch the app.

Allow permissions (Camera, Audio, Storage).

Use buttons to:

Capture photos

Record videos

Switch between cameras

Captured media is saved in device storage.

🗂 Project Structure
css
Copy
Edit
📁 CameraX-Android-App/
│
├── 📁 app/
│   ├── 📁 src/
│   │   ├── 📁 main/
│   │   │   ├── 📁 java/
│   │   │   │   └── com.example.cameraxapp/
│   │   │   │       ├── MainActivity.kt
│   │   │   │       └── CameraUtils.kt
│   │   │   ├── 📁 res/
│   │   │   └── AndroidManifest.xml
├── build.gradle
└── README.md
🔐 Permissions
Required permissions:

CAMERA

RECORD_AUDIO (optional for video)

WRITE_EXTERNAL_STORAGE (if targeting SDK < 29)

READ_EXTERNAL_STORAGE (if accessing saved media)

Use ActivityResultContracts to request them at runtime.

🚧 Future Enhancements
Add gallery view of captured images/videos

Add zoom and flash toggle

Implement ML integration (e.g., face detection)

Add in-app photo editing options

Add gesture support (pinch to zoom, tap to focus)

🤝 Contributing
Contributions are welcome! To contribute:

Fork the repo

Create a branch: git checkout -b feature/your-feature

Commit: git commit -m 'Add some feature'

Push: git push origin feature/your-feature

Open a pull request

