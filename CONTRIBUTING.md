# Contributing to RM Calculator Vamal

Thank you for your interest in contributing to RM Calculator Vamal! We welcome contributions of all kinds, whether you are fixing a bug, improving documentation, or proposing new features.

---

## Code of Conduct

Please treat everyone in the community with respect, kindness, and constructive feedback. Open source thrives when developers collaborate positively.

---

## How to Contribute

### 1. Reporting Bugs & Requesting Features
- **Search existing issues** first to avoid duplicates.
- **For bugs:** Open an issue describing the bug, including steps to reproduce, expected vs. actual behavior, and environment details (Android SDK (API 34+), JDK 17/21, and Android Studio).
- **For feature requests:** Describe the problem you are trying to solve and propose a solution or interface specification.

### 2. Pull Request Workflow

1. **Fork the repository** and clone your fork locally:
   ```bash
   git clone https://github.com/YOUR_USERNAME/rm-calculator-vamal.git
   cd rm-calculator-vamal
   ```

2. **Create a topic branch** from `main`:
   ```bash
   git checkout -b feat/your-feature-name
   # or: git checkout -b fix/issue-description
   ```

3. **Follow commit conventions:** We follow [Conventional Commits](https://www.conventionalcommits.org/):
   - `feat: update excise duty rate calculations for 2026`
   - `fix: resolve currency conversion rounding error`
   - `docs: add screenshots to visual showcase`
   - `perf: optimize recomposition counts in tariff screen`

4. **Ensure code quality:**
   - Keep code clean, readable, and strictly typed.
   - Verify that all existing and new unit tests pass before submitting.
   - Run the local linter/formatter if available.

5. **Push and open a Pull Request:**
   - Push your branch to your fork:
     ```bash
     git push origin feat/your-feature-name
     ```
   - Open a Pull Request against the `main` branch.
   - Provide a clear PR title and description outlining the changes made and referencing any related issues (e.g., `Closes #12`).

---

## Development Setup

RM Calculator Vamal is built with **Kotlin**, Jetpack Compose, and modern Android Clean Architecture.

1. **Clone the repository:**
   ```bash
   git clone https://github.com/alexandrmotologa/rm-calculator-vamal.git
   cd rm-calculator-vamal
   ```

2. **Run unit tests via Gradle:**
   ```bash
   ./gradlew testDebugUnitTest
   ```

3. **Run Android Lint:**
   ```bash
   ./gradlew lint
   ```

4. **Code standards:**
   - Follow official Kotlin & Android Architecture Components coding conventions.
   - Maintain Compose state immutability and Material 3 design tokens.

Refer to the **Quick Start** section in [README.md](README.md) for full configuration flags, architecture details, and usage examples.

---

## Questions & Discussions

If you have questions about architecture decisions or need guidance before submitting a large change, feel free to open a Discussion or an Issue with the `question` label.
