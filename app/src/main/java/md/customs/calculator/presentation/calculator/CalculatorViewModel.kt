package md.customs.calculator.presentation.calculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import md.customs.calculator.data.local.datastore.SettingsManager
import md.customs.calculator.data.local.entity.CalculationHistoryEntity
import md.customs.calculator.domain.model.Currency
import md.customs.calculator.domain.model.ProductCategory
import md.customs.calculator.domain.model.TaxConstants
import md.customs.calculator.domain.repository.ExchangeRateRepository
import md.customs.calculator.domain.repository.HistoryRepository
import md.customs.calculator.domain.usecase.CalculateTaxesUseCase
import md.customs.calculator.domain.usecase.CalculationResult
import md.customs.calculator.presentation.util.AppLanguage
import md.customs.calculator.presentation.util.LanguageManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class CalculatorUiState(
    val parcelValue: String = "",
    val shippingCost: String = "",
    val selectedCurrency: Currency = Currency.MDL,
    val selectedCategory: ProductCategory = ProductCategory.PHONES,
    val dutyPercentage: Double = ProductCategory.PHONES.defaultDutyRate,
    val isJuly2026LawEnabled: Boolean = false,
    val calculationResult: CalculationResult? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val productName: String = "",
    val deliveryCompany: String = "",
    val trackerId: String = "",
    val currentLanguage: AppLanguage = AppLanguage.RO
)

class CalculatorViewModel(
    private val calculateTaxesUseCase: CalculateTaxesUseCase,
    private val exchangeRateRepository: ExchangeRateRepository,
    private val historyRepository: HistoryRepository,
    private val settingsManager: SettingsManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    init {
        // Load saved language preference
        viewModelScope.launch {
            val savedLang = settingsManager.selectedLanguage.firstOrNull() ?: AppLanguage.RO
            LanguageManager.currentLanguage = savedLang
            _uiState.update { it.copy(currentLanguage = savedLang) }
        }

        // Pre-fetch BNM rates when app opens
        viewModelScope.launch {
            try {
                val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                val todayStr = dateFormat.format(Date())
                exchangeRateRepository.getRates(todayStr)
            } catch (e: Exception) {
                // Silent catch for initial warm-up
            }
        }
    }

    fun updateLanguage(lang: AppLanguage) {
        LanguageManager.currentLanguage = lang
        _uiState.update { it.copy(currentLanguage = lang) }
        viewModelScope.launch {
            settingsManager.saveSelectedLanguage(lang)
        }
    }

    fun updateProductName(newValue: String) {
        _uiState.update { it.copy(productName = newValue) }
    }

    fun updateDeliveryCompany(newValue: String) {
        _uiState.update { it.copy(deliveryCompany = newValue) }
    }

    fun updateTrackerId(newValue: String) {
        _uiState.update { it.copy(trackerId = newValue) }
    }

    fun updateParcelValue(newValue: String) {
        _uiState.update { it.copy(parcelValue = newValue) }
    }

    fun updateShippingCost(newValue: String) {
        _uiState.update { it.copy(shippingCost = newValue) }
    }

    fun updateCurrency(newCurrency: Currency) {
        _uiState.update { it.copy(selectedCurrency = newCurrency) }
    }

    fun updateCategory(newCategory: ProductCategory) {
        _uiState.update {
            it.copy(
                selectedCategory = newCategory,
                dutyPercentage = newCategory.defaultDutyRate
            )
        }
    }

    fun toggleJuly2026Law(enabled: Boolean) {
        _uiState.update { it.copy(isJuly2026LawEnabled = enabled) }
    }

    fun clearResult() {
        _uiState.update { it.copy(calculationResult = null) }
    }

    fun saveCalculationToHistory() {
        val state = _uiState.value
        val result = state.calculationResult ?: return

        val entity = CalculationHistoryEntity(
            parcelValue = state.parcelValue.toDoubleOrNull() ?: 0.0,
            shippingCost = state.shippingCost.toDoubleOrNull() ?: 0.0,
            currency = state.selectedCurrency.code,
            category = state.selectedCategory.key,
            customsDuty = result.dutyMdl,
            vat = result.vatMdl,
            processingFee = result.procedureFeeMdl,
            totalCost = result.totalMdl,
            timestamp = System.currentTimeMillis(),
            isJuly2026LawApplied = state.isJuly2026LawEnabled,
            productName = state.productName.takeIf { it.isNotBlank() },
            deliveryCompany = state.deliveryCompany.takeIf { it.isNotBlank() },
            trackerId = state.trackerId.takeIf { it.isNotBlank() }
        )

        viewModelScope.launch {
            historyRepository.saveCalculation(entity)
            clearResult()
        }
    }

    fun calculateTaxes() {
        val currentState = _uiState.value
        val parcelVal = currentState.parcelValue.toDoubleOrNull() ?: 0.0
        val shipping = currentState.shippingCost.toDoubleOrNull() ?: 0.0

        if (parcelVal <= 0.0) {
            _uiState.update { it.copy(errorMessage = "error_parcel_value") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                // Formatting date for BNM API call
                val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                val todayStr = dateFormat.format(Date())

                // Retrieve mapping from Repository
                val rates = exchangeRateRepository.getRates(todayStr)

                val rateMap = rates.toMutableMap()
                rateMap[Currency.MDL.code] = TaxConstants.DEFAULT_MDL_RATE.toFloat()

                val selectedCurrencyRate = rateMap[currentState.selectedCurrency.code]?.toDouble()
                    ?: TaxConstants.DEFAULT_MDL_RATE
                val eurRate = rateMap[Currency.EUR.code]?.toDouble()
                    ?: TaxConstants.DEFAULT_EUR_RATE_FALLBACK

                // Perform the Math logic
                val result = calculateTaxesUseCase(
                    parcelValue = parcelVal,
                    shippingCost = shipping,
                    selectedCurrencyRateToMdl = selectedCurrencyRate,
                    eurRateToMdl = eurRate,
                    dutyPercentage = currentState.dutyPercentage,
                    applyJuly2026Rules = currentState.isJuly2026LawEnabled
                )

                _uiState.update { it.copy(calculationResult = result, isLoading = false) }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Eroare la obținerea cursurilor valutare: ${e.message}"
                    )
                }
            }
        }
    }

    fun resetInputs() {
        _uiState.update {
            it.copy(
                parcelValue = "",
                shippingCost = "",
                deliveryCompany = "",
                trackerId = "",
                productName = "",
                selectedCurrency = Currency.MDL,
                selectedCategory = ProductCategory.PHONES,
                dutyPercentage = ProductCategory.PHONES.defaultDutyRate,
                isJuly2026LawEnabled = false,
                calculationResult = null,
                errorMessage = null
            )
        }
    }
}
