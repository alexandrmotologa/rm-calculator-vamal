# 🤝 Contributing Guidelines

Thank you for your interest in contributing to **Customs Duty Calculator**!

---

## 🛠️ Development Setup

1. **Fork and Clone** the repository:
   ```bash
   git clone https://github.com/your-username/rm-calculator-vamal.git
   cd rm-calculator-vamal
   ```
2. Open the project in **Android Studio Ladybug (2024.2.1)** or newer.
3. Ensure you have **JDK 17** or **JDK 21** configured as the Gradle JDK.

---

## 📐 Code Style & Conventions

* Follow the official [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html) and [Android Kotlin Style Guide](https://developer.android.com/kotlin/style-guide).
* Keep Domain Layer logic pure Kotlin without Android framework dependencies.
* Build UI components using **Jetpack Compose** and **Material 3**.
* All UI strings must be localized across `RO`, `EN`, and `RU` in `AppStrings.kt`.

---

## 🧪 Testing

Before submitting a Pull Request, make sure all unit tests pass:
```bash
./gradlew testDebugUnitTest
```

If adding new calculation rules or categories, please add corresponding unit tests under `app/src/test/java/md/customs/calculator/`.

---

## 🚀 Submitting a Pull Request

1. Create a feature branch:
   ```bash
   git checkout -b feature/your-feature-name
   ```
2. Commit your changes using conventional commit messages:
   ```bash
   git commit -m "feat: add support for EUR exchange rate history"
   ```
3. Push to your fork and submit a Pull Request against the `main` branch.
