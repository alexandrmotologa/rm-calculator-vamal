# 🏛️ Architecture & Technical Documentation

This document provides an in-depth explanation of the design patterns, layer boundaries, and data flow implemented in **Customs Duty Calculator**.

---

## 1. Architectural Paradigm

The application is architected according to **Clean Architecture** and the official **Android Architecture Guidelines**, incorporating:

* **Unidirectional Data Flow (UDF)**: UI state flows down from ViewModels to Composable screens, and user events flow up.
* **Separation of Concerns (SoC)**: Domain logic is isolated from UI frameworks and data persistence mechanisms.
* **Dependency Inversion Principle (DIP)**: ViewModels and UseCases depend on abstract domain repository interfaces, not concrete implementations.
* **Type-Safe Domain Modeling**: Categoriess and currencies are encapsulated via strongly-typed enums (`ProductCategory`, `Currency`) rather than raw primitive strings.

---

## 2. Layer Breakdown

### A. Domain Layer (`md.customs.calculator.domain`)
The core business logic layer. It has zero dependencies on Android UI components or Room/Retrofit libraries.

* **`model/`**:
  * `ProductCategory`: Enum representing all product categories with their respective standard duty rates (0% to 15%) and localization resource keys.
  * `Currency`: Supported fiat currencies (`MDL`, `EUR`, `USD`, `RON`, `GBP`).
  * `TaxConstants`: Centralized fiscal rules (e.g., 150 EUR threshold, 20% VAT rate, 50 MDL standard fee, 12 MDL October 2026 fee).
* **`repository/`**:
  * `ExchangeRateRepository`: Interface contract for fetching daily foreign exchange rates in MDL.
  * `HistoryRepository`: Interface contract for persisting and retrieving calculation records.
* **`usecase/`**:
  * `CalculateTaxesUseCase`: Pure Kotlin mathematical calculation engine supporting both current and October 2026 fiscal scenarios.

### B. Data Layer (`md.customs.calculator.data`)
Handles data persistence, network requests, caching strategies, and mapping.

* **`local/`**:
  * `AppDatabase`: Room SQLite database for calculation history.
  * `dao/CalculationHistoryDao`: Room Data Access Object exposing reactive Kotlin Coroutines `Flow`.
  * `datastore/SettingsManager`: Jetpack DataStore Preferences for exchange rates caching and persistent language preferences.
* **`remote/`**:
  * `api/BnmApiService`: Retrofit interface consuming National Bank of Moldova (BNM) XML exchange rates.
  * `dto/BnmExchangeRateResponse`: XML-deserialized Data Transfer Objects using `XMLUtil`.
* **`repository/`**:
  * `ExchangeRateRepositoryImpl`: Implements offline-first caching logic:
    1. Checks if rates were already synced today in `DataStore`.
    2. Fetches XML from BNM if cache is missing or outdated.
    3. Falls back to cached rates if offline/network error.
  * `HistoryRepositoryImpl`: Connects `HistoryRepository` domain contract to `CalculationHistoryDao`.

### C. Presentation Layer (`md.customs.calculator.presentation`)
UI rendered completely declaratively using **Jetpack Compose** and **Material 3**.

* **`calculator/`**:
  * `CalculatorScreen`: Main screen orchestrating modular UI cards.
  * `components/`: Modular, decoupled cards (`ProductSectionCard`, `FinancialsSectionCard`, `LegislationCard`, `DisclaimerCard`).
  * `ResultBottomSheet`: Animated Material 3 bottom sheet presenting itemized tax breakdowns.
  * `CalculatorViewModel`: Manages `CalculatorUiState`, user input validation, asynchronous calculation triggering, and history saving.
* **`history/`**:
  * `HistoryScreen`: LazyColumn displaying past calculations with swipe-to-delete and tracking resolution.
  * `HistoryViewModel`: Exposes `StateFlow<List<CalculationHistoryEntity>>` collected reactively from Room.
* **`util/`**:
  * `AppStrings`: Runtime localization dictionary for Romanian, English, and Russian.
  * `LanguageManager`: Reactive language state controller.
  * `TrackingResolver`: Deep link generator for courier companies.

---

## 3. Dependency Injection (`md.customs.calculator.di`)

The application uses **Manual Dependency Injection** via `AppContainer` and `DefaultAppContainer`, adhering to Google's official best practices for modular Android applications:

* Single instance of `AppDatabase` and `SettingsManager`.
* Lazy initialization of network clients (`Retrofit`, `BnmApiService`).
* Clean binding of domain repository interfaces to data layer implementations.
* Custom `AppViewModelProvider.Factory` creating ViewModels with injected domain dependencies.
