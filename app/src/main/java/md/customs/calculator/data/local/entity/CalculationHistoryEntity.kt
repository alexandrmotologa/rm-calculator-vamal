package md.customs.calculator.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity representing a saved calculation in the local Room database.
 */
@Entity(tableName = "calculation_history")
data class CalculationHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val parcelValue: Double,
    val shippingCost: Double,
    
    /**
     * E.g., EUR, USD, RON, GBP, MDL
     */
    val currency: String,
    
    /**
     * E.g., Electronics, Clothing
     */
    val category: String,
    
    val customsDuty: Double,
    val vat: Double,
    val processingFee: Double,
    val totalCost: Double,
    
    /**
     * Timestamp of the calculation.
     */
    val timestamp: Long,
    
    /**
     * Whether the July 2026 law was applied (no exemption, specific fees, etc.)
     */
    val isJuly2026LawApplied: Boolean,

    val productName: String? = null,
    val deliveryCompany: String? = null,
    val trackerId: String? = null
)
