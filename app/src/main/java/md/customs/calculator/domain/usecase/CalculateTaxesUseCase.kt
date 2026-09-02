package md.customs.calculator.domain.usecase

import md.customs.calculator.domain.model.TaxConstants

data class CalculationResult(
    val baseMdl: Double,
    val dutyMdl: Double,
    val vatMdl: Double,
    val procedureFeeMdl: Double,
    val totalMdl: Double,
    val exemptionMessage: String? = null,
    val exemptionLink: String? = null
)

class CalculateTaxesUseCase {

    /**
     * Executes the mathematical calculation for Customs Duty, VAT, and Fees.
     *
     * @param parcelValue Value of the parcel in selected currency
     * @param shippingCost Shipping cost in selected currency
     * @param selectedCurrencyRateToMdl MDL rate of the input currency
     * @param eurRateToMdl MDL rate for EUR (crucial for checking the 150 EUR limit)
     * @param dutyPercentage Standard duty based on product category (e.g., 0.10 for 10%)
     * @param applyJuly2026Rules Whether to use the upcoming law rules
     */
    operator fun invoke(
        parcelValue: Double,
        shippingCost: Double,
        selectedCurrencyRateToMdl: Double,
        eurRateToMdl: Double,
        dutyPercentage: Double,
        applyJuly2026Rules: Boolean
    ): CalculationResult {

        // Step 1: Convert values to MDL
        val valueMdl = parcelValue * selectedCurrencyRateToMdl
        val shippingMdl = shippingCost * selectedCurrencyRateToMdl

        // Step 2: Find EUR equivalent to check thresholds
        val safeEurRate = if (eurRateToMdl > 0.0) eurRateToMdl else TaxConstants.DEFAULT_MDL_RATE
        val valueEur = valueMdl / safeEurRate

        return if (!applyJuly2026Rules) {
            // == Scenario A: Current Law ==
            if (valueEur <= TaxConstants.EXEMPTION_THRESHOLD_EUR) {
                CalculationResult(
                    baseMdl = 0.0,
                    dutyMdl = 0.0,
                    vatMdl = 0.0,
                    procedureFeeMdl = 0.0,
                    totalMdl = 0.0,
                    exemptionMessage = "exemption_msg",
                    exemptionLink = TaxConstants.LEGISLATION_URL
                )
            } else {
                val base = valueMdl + shippingMdl
                val duty = base * dutyPercentage
                val vat = (base + duty) * TaxConstants.STANDARD_VAT_RATE
                val procedureFee = TaxConstants.STANDARD_PROCEDURE_FEE_MDL
                val total = duty + vat + procedureFee

                CalculationResult(
                    baseMdl = base,
                    dutyMdl = duty,
                    vatMdl = vat,
                    procedureFeeMdl = procedureFee,
                    totalMdl = total
                )
            }
        } else {
            // == Scenario B: Upcoming Law ==
            if (valueEur <= TaxConstants.EXEMPTION_THRESHOLD_EUR) {
                // If <= 150 EUR, shipping is not included in the base.
                val base = valueMdl
                val duty = 0.0
                val vat = base * TaxConstants.STANDARD_VAT_RATE
                val procedureFee = TaxConstants.REDUCED_PROCEDURE_FEE_MDL
                val total = vat + procedureFee

                CalculationResult(
                    baseMdl = base,
                    dutyMdl = duty,
                    vatMdl = vat,
                    procedureFeeMdl = procedureFee,
                    totalMdl = total
                )
            } else {
                // If > 150 EUR, shipping is included, standard duty & fee apply.
                val base = valueMdl + shippingMdl
                val duty = base * dutyPercentage
                val vat = (base + duty) * TaxConstants.STANDARD_VAT_RATE
                val procedureFee = TaxConstants.STANDARD_PROCEDURE_FEE_MDL
                val total = duty + vat + procedureFee

                CalculationResult(
                    baseMdl = base,
                    dutyMdl = duty,
                    vatMdl = vat,
                    procedureFeeMdl = procedureFee,
                    totalMdl = total
                )
            }
        }
    }
}
