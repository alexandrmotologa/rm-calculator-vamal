# Calculator Vamal Moldova (RM) / Customs Calculator Moldova

![Android](https://img.shields.io/badge/Platform-Android-green.svg)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)
![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-orange.svg)

---

## 🇷🇴 Descriere (Romanian)

Această aplicație este un instrument util pentru calcularea taxelor vamale pentru coletele importate în Republica Moldova. Oferă o interfață modernă și calcule precise bazate pe legislația în vigoare și pe cursul valutar oficial.

### 🌟 Funcționalități Cheie

*   **Calcul Taxe Detaliat**: Calculează instantaneu Taxa Vamală, TVA-ul (20%) și Taxa pentru Proceduri Vamale în funcție de datele introduse.
*   **Curs Valutar în Timp Real**: Preia automat cursurile de schimb de la **Banca Națională a Moldovei (BNM)** pentru o conversie precisă (EUR, USD etc. în MDL).
*   **Dublu Scenariu Legislativ**:
    *   **Legea Curentă**: Prag de scutire de 150 EUR, cu includerea costului de transport în baza de calcul dacă pragul este depășit.
    *   **Legea Iulie 2026**: Simulează regulile viitoare unde coletele sub 150 EUR au baze de calcul diferite (de ex. excluderea transportului).
*   **Categorii de Produse**: Selectarea categoriei (de ex. Telefoane Mobile, Îmbrăcăminte) pentru a aplica cota corectă a taxei vamale prestabilite.
*   **Istoric Calcule**: Salvează detaliile coletelor (valoare, taxe, data) într-o bază de date locală pentru a urmări cheltuielile efectuate.
*   **Urmărire Colet**: Permite salvarea unui Track ID și a companiei de curierat pentru a deschide direct link-uri de urmărire.

### 🛠️ Tehnologii Utilizate

*   **Limbaj**: Kotlin
*   **UI**: Jetpack Compose (Material Design 3)
*   **Networking**: Retrofit (cu adaptor XML pentru API-ul BNM)
*   **Local Storage**:
    *   **Room Database**: Pentru stocarea istoricului de calcule.
    *   **Preferences DataStore**: Pentru stocarea setărilor (ex: modul de vizualizare).
*   **Management State**: ViewModel & Kotlin StateFlow
*   **Concurrență**: Coroutines (pentru rețea și DB)
*   **Injecție Dependențe**: AppContainer Manual

---

## 🇺🇸 Description (English)

An Android application designed to compute customs taxes for parcels imported into the Republic of Moldova. It features a User-Friendly design with accurate math based on current and upcoming legislation with real-time exchange rates.

### 🌟 Key Features

*   **Detailed Tax Calculation**: Instantly computes Customs Duty, VAT (20%), and Processing Fees berdasarkan input.
*   **Live Currency Rates**: Automatically fetches today's official rates from the **National Bank of Moldova (NBM / BNM)** for accurate conversions to MDL.
*   **Dual Legislative Scopes**:
    *   **Current Law**: Uses the 150 EUR exemption threshold; fees apply on item + shipping totals above it.
    *   **July 2026 Law**: Reflects upcoming norms with adjusted exemption flows (e.g., excluding shipping base for smaller amounts).
*   **Product Categories**: Easily select categories (e.g., Mobile Phones) with pre-defined duty percentages.
*   **Calculation History**: Keeps a secure local log of your calculated costs for later auditing.
*   **Order Meta-data**: Save details like Item Name, Logistics Carrier & Tracking ID to keep your logistics data synced.

### 🛠️ Technical Stack

*   **Language**: Kotlin
*   **UI**: Jetpack Compose (Material You 3)
*   **Networking**: Retrofit with XML Adapter (BNM Endpoint)
*   **Local Storage**:
    *   **Room Database**: For calculation caching systems.
    *   **Preferences DataStore**: App preferences/configs.
*   **State Management**: ViewModel & StateFlow
*   **Concurrency**: Kotlin Coroutines
*   **DI**: Manual container pattern

---

## 🚀 Cum se Rulează / How to Build

1. Clonează acest repository / Clone this repository.
2. Deschide proiectul în Android Studio / Open project in Android Studio.
3. Rulează comanda Gradle / Run Gradle command:
   ```bash
   ./gradlew assembleDebug
   ```
4. Instalează APK-ul format în `app/build/outputs/apk/debug/` / Install APK from the outputs folder.