package md.customs.calculator.data.repository

import kotlinx.coroutines.flow.Flow
import md.customs.calculator.data.local.dao.CalculationHistoryDao
import md.customs.calculator.data.local.entity.CalculationHistoryEntity
import md.customs.calculator.domain.repository.HistoryRepository

class HistoryRepositoryImpl(
    private val calculationHistoryDao: CalculationHistoryDao
) : HistoryRepository {

    override fun getAllHistory(): Flow<List<CalculationHistoryEntity>> {
        return calculationHistoryDao.getAllHistory()
    }

    override suspend fun saveCalculation(entity: CalculationHistoryEntity) {
        calculationHistoryDao.insertCalculation(entity)
    }

    override suspend fun deleteCalculation(entity: CalculationHistoryEntity) {
        calculationHistoryDao.deleteCalculation(entity)
    }
}
