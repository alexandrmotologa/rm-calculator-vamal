package md.customs.calculator.domain.usecase

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
        // Safe division check for eurRateToMdl
        val safeEurRate = if (eurRateToMdl > 0.0) eurRateToMdl else 1.0
        val valueEur = valueMdl / safeEurRate

        return if (!applyJuly2026Rules) {
            // == Scenario A: Current Law ==
            if (valueEur <= 150.0) {
                CalculationResult(
                    baseMdl = 0.0,
                    dutyMdl = 0.0,
                    vatMdl = 0.0,
                    procedureFeeMdl = 0.0,
                    totalMdl = 0.0,
                    exemptionMessage = "Scutire aplicată: Coletele cu o valoare de până la 150 EUR sunt scutite de taxe vamale și TVA.",
                    exemptionLink = "https://customs.gov.md/ro/articles/bunuri-expediate-prin-intermediul-trimiterilor-postale-internationale"
                )
            } else {
                val base = valueMdl + shippingMdl
                val duty = base * dutyPercentage
                val vat = (base + duty) * 0.20
                val procedureFee = 50.0
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
            // == Scenario B: July 2026 Law ==
            if (valueEur <= 150.0) {
                // If <= 150 EUR, shipping is not included in the base.
                val base = valueMdl
                val duty = 0.0
                val vat = base * 0.20
                val procedureFee = 20.0
                val total = vat + procedureFee

                CalculationResult(
                    baseMdl = base,
                    dutyMdl = duty,
                    vatMdl = vat,
                    procedureFeeMdl = procedureFee,
                    totalMdl = total
                )
            } else {
                // If > 150 EUR, it's the exact same math as the Current Law > 150 EUR.
                val base = valueMdl + shippingMdl
                val duty = base * dutyPercentage
                val vat = (base + duty) * 0.20
                val procedureFee = 50.0
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
