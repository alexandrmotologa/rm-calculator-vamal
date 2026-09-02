# 🇲🇩 Customs Duty Calculator (Calculator Vamal Moldova)

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.0-7F52FF.svg?style=flat-square&logo=kotlin)](https://kotlinlang.org)
[![Android SDK](https://img.shields.io/badge/Android%20SDK-26%20--%2036-3DDC84.svg?style=flat-square&logo=android)](https://developer.android.com)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-BOM%202025.01-4285F4.svg?style=flat-square&logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![Material 3](https://img.shields.io/badge/Material%203-Enabled-6750A4.svg?style=flat-square)](https://m3.material.io)
[![Architecture](https://img.shields.io/badge/Architecture-Clean%20%2B%20MVVM-blue.svg?style=flat-square)](docs/ARCHITECTURE.md)
[![License](https://img.shields.io/badge/License-MIT-green.svg?style=flat-square)](LICENSE)

A modern, offline-first Android application designed to calculate **customs duties, VAT, and postal processing fees** for international parcels imported into the Republic of Moldova. 

Built with **Jetpack Compose**, **Clean Architecture**, **Kotlin Coroutines & Flow**, **Room Database**, and **Material 3**.

---

## ✨ Key Features

* 🧮 **Dual Legislative Calculation Engines**:
  * **Current Law**: Exemption under 150 EUR; standard customs duty (0% - 15%), 20% VAT on taxable base (Parcel + Duty), and 50 MDL procedure fee for parcels over 150 EUR.
  * **Upcoming Fiscal Reform (October 1, 2026)**: 20% VAT on parcel value + 12 MDL fixed operational fee for parcels $\le$ 150 EUR.
* 💱 **Live & Cached Exchange Rates**:
  * Automatic daily synchronization with the **National Bank of Moldova (BNM)** XML exchange rate API.
  * **Offline-First**: Graceful fallback to local cached rates in `DataStore` if offline.
* 📦 **Parcel Logistics & Tracking Resolver**:
  * Track parcels directly via one-tap deep links for **DHL, FedEx, Poșta Moldovei, Nova Poshta, Fan Courier, and Pesoto**.
* 💾 **Calculation History**:
  * Persistent local SQLite storage powered by **Room Database** with real-time `Flow` observation.
* 🌐 **Full Trilingual Localization**:
  * Seamless runtime switching between **Romanian (RO)**, **English (EN)**, and **Russian (RU)**, persisted in `DataStore`.
* ⚡ **Performance & Memory Optimized**:
  * Fully configured for **R8 Full Mode**, aggressive code inlining, and resource shrinking (`isShrinkResources = true`).

---

## 🏗️ Architecture & Tech Stack

This project strictly adheres to **Clean Architecture** and **SOLID** principles, promoting high testability, modularity, and maintainability.

```
                  ┌──────────────────────────────┐
                  │      Presentation Layer      │
                  │ (Jetpack Compose, ViewModels)│
                  └──────────────┬───────────────┘
                                 │
                  ┌──────────────▼───────────────┐
                  │         Domain Layer         │
                  │ (UseCases, Repositories,     │
                  │  Type-Safe Models)           │
                  └──────────────▲───────────────┘
                                 │
                  ┌──────────────┴───────────────┐
                  │          Data Layer          │
                  │ (Room, DataStore, Retrofit)  │
                  └──────────────────────────────┘
```

| Layer / Concern | Technology / Library |
| :--- | :--- |
| **Language & Tooling** | Kotlin 2.0.0, KSP, Gradle Kotlin DSL |
| **UI Framework** | Jetpack Compose (BOM 2025.01.00), Material 3 |
| **Architecture Pattern** | MVVM + Clean Architecture + Unidirectional Data Flow (UDF) |
| **Dependency Injection** | Manual Dependency Container (`AppContainer`, `AppViewModelProvider`) |
| **Asynchronous & Reactive** | Kotlin Coroutines, StateFlow, SharedFlow |
| **Local Database** | Room Database (SQLite) with KSP |
| **Key-Value Persistence** | Jetpack DataStore Preferences |
| **Networking & Parsing** | Retrofit 2, OkHttp 3, XMLUtil Serialization, Kotlinx Serialization |
| **Unit Testing** | JUnit 4, Kotlin Test |
| **Minification & Optimization**| R8 Full Mode, ProGuard, Resource Shrinking |

---

## 📁 Project Structure

```
app/src/main/java/md/customs/calculator/
├── data/
│   ├── local/
│   │   ├── dao/                 # Room DAOs (CalculationHistoryDao)
│   │   ├── datastore/           # DataStore Preferences (SettingsManager)
│   │   ├── entity/              # Database Entities (CalculationHistoryEntity)
│   │   └── AppDatabase.kt       # Room Database Configuration
│   ├── remote/
│   │   ├── api/                 # Retrofit Services (BnmApiService)
│   │   └── dto/                 # XML Response DTOs (BnmExchangeRateResponse)
│   └── repository/              # Repository Implementations (ExchangeRateRepositoryImpl, HistoryRepositoryImpl)
├── di/
│   └── AppContainer.kt          # Manual Dependency Injection Container & ViewModel Factory
├── domain/
│   ├── model/                   # Domain Models (ProductCategory, Currency, TaxConstants)
│   ├── repository/              # Domain Repository Contracts (ExchangeRateRepository, HistoryRepository)
│   └── usecase/                 # Pure Business Logic (CalculateTaxesUseCase)
├── presentation/
│   ├── calculator/              # Main Calculator Screen, Components, & ViewModel
│   │   └── components/          # Modular UI Cards (Product, Financials, Legislation, Disclaimer)
│   ├── history/                 # Saved Calculations History Screen & ViewModel
│   ├── navigation/              # Jetpack Compose NavHost Navigation
│   ├── theme/                   # Material 3 Colors, Typography, & Themes
│   └── util/                    # Localization (AppStrings, LanguageManager) & TrackingResolver
└── CalculatorApplication.kt     # Application class initializing AppContainer
```

---

## 🚀 Getting Started

### Prerequisites
* **Android Studio Ladybug (2024.2.1)** or newer
* **JDK 17** or **JDK 21**
* **Android SDK**: `compileSdk = 36`, `minSdk = 26`

### Cloning & Building
1. Clone the repository:
   ```bash
   git clone https://github.com/alexandrmotologa/rm-calculator-vamal.git
   cd rm-calculator-vamal
   ```

2. Build and run debug build:
   ```bash
   ./gradlew assembleDebug
   ```

3. Run automated unit tests:
   ```bash
   ./gradlew testDebugUnitTest
   ```

### Release Signing Setup (Optional)
To generate signed release bundles, copy `app/keystore.properties.example` to `app/keystore.properties` and add your signing keystore credentials:
```bash
cp app/keystore.properties.example app/keystore.properties
```

---

## 📖 Documentation

* [Architecture Deep Dive](docs/ARCHITECTURE.md) — Comprehensive overview of Clean Architecture layers, data flow, and dependency injection.
* [Customs Legislation & Tax Rules](docs/CUSTOMS_RULES.md) — Detailed breakdown of customs formulas and the October 2026 fiscal policy.
* [Contributing Guide](docs/CONTRIBUTING.md) — Guidelines for code style, branching, and pull requests.

---

## ⚖️ Legal & Government Information Disclaimer

> [!IMPORTANT]
> **Disclaimer**: This application is an independent open-source utility developed for educational and personal informational purposes. It **DOES NOT** represent an official government body and is **NOT** affiliated with the Customs Service of the Republic of Moldova (*Serviciul Vamal al RM*) or the Government of the Republic of Moldova. Official legal references can be consulted at [customs.gov.md](https://customs.gov.md/) and [legis.md](https://www.legis.md/).

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).