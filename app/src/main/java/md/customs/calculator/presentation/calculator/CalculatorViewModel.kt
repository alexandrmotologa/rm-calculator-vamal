package md.customs.calculator.presentation.calculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import md.customs.calculator.domain.repository.ExchangeRateRepository
import md.customs.calculator.domain.usecase.CalculateTaxesUseCase
import md.customs.calculator.domain.usecase.CalculationResult
import md.customs.calculator.data.local.dao.CalculationHistoryDao
import md.customs.calculator.data.local.entity.CalculationHistoryEntity
import md.customs.calculator.presentation.util.AppLanguage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class CalculatorUiState(
    val parcelValue: String = "",
    val shippingCost: String = "",
    val selectedCurrency: String = "MDL",
    val selectedCategory: String = "cat_phones",
    val dutyPercentage: Double = 0.0,
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
    private val historyDao: CalculationHistoryDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    fun updateLanguage(lang: AppLanguage) {
        _uiState.update { it.copy(currentLanguage = lang) }
    }

    init {
        // Pre-fetch BNM rates when app opens
        viewModelScope.launch {
            try {
                val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                val todayStr = dateFormat.format(Date())
                exchangeRateRepository.getRates(todayStr)
            } catch (e: Exception) {
                // Ignore silent pre-fetch errors
            }
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

    fun updateCurrency(newCurrency: String) {
        _uiState.update { it.copy(selectedCurrency = newCurrency) }
    }

    fun updateCategory(newCategory: String, dutyPct: Double) {
        _uiState.update { it.copy(selectedCategory = newCategory, dutyPercentage = dutyPct) }
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
            currency = state.selectedCurrency,
            category = state.selectedCategory,
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
            historyDao.insertCalculation(entity)
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
                rateMap["MDL"] = 1.0f // Ensure intrinsic MDL conversion is 1:1
                
                val selectedCurrencyRate = rateMap[currentState.selectedCurrency]?.toDouble() ?: 1.0
                val eurRate = rateMap["EUR"]?.toDouble() ?: 20.0

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
                selectedCurrency = "MDL",
                selectedCategory = "cat_phones",
                dutyPercentage = 0.0,
                isJuly2026LawEnabled = false,
                calculationResult = null,
                errorMessage = null
            )
        }
    }
}
