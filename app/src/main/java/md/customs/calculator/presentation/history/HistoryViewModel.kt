package md.customs.calculator.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import md.customs.calculator.data.local.entity.CalculationHistoryEntity
import md.customs.calculator.domain.repository.HistoryRepository

class HistoryViewModel(
    private val historyRepository: HistoryRepository
) : ViewModel() {

    val historyEntries: StateFlow<List<CalculationHistoryEntity>> = historyRepository.getAllHistory()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteCalculation(entity: CalculationHistoryEntity) {
        viewModelScope.launch {
            historyRepository.deleteCalculation(entity)
        }
    }
}
