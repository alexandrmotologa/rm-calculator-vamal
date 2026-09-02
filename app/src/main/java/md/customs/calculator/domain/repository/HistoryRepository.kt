package md.customs.calculator.domain.repository

import kotlinx.coroutines.flow.Flow
import md.customs.calculator.data.local.entity.CalculationHistoryEntity

/**
 * Domain repository contract for managing stored calculation history.
 */
interface HistoryRepository {
    fun getAllHistory(): Flow<List<CalculationHistoryEntity>>
    suspend fun saveCalculation(entity: CalculationHistoryEntity)
    suspend fun deleteCalculation(entity: CalculationHistoryEntity)
}
