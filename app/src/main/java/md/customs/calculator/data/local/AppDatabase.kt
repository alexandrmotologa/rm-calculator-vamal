package md.customs.calculator.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import md.customs.calculator.data.local.dao.CalculationHistoryDao
import md.customs.calculator.data.local.entity.CalculationHistoryEntity

@Database(
    entities = [CalculationHistoryEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract val calculationHistoryDao: CalculationHistoryDao
    
    companion object {
        const val DATABASE_NAME = "customs_calculator_db"
    }
}
