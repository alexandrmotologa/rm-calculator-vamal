package md.customs.calculator.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import md.customs.calculator.data.local.entity.CalculationHistoryEntity

@Dao
interface CalculationHistoryDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalculation(calculation: CalculationHistoryEntity)

    @androidx.room.Delete
    suspend fun deleteCalculation(calculation: CalculationHistoryEntity)

    @Query("SELECT * FROM calculation_history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<CalculationHistoryEntity>>
}
