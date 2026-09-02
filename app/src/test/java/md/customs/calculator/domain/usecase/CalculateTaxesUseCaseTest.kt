package md.customs.calculator.domain.usecase

import md.customs.calculator.domain.model.TaxConstants
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class CalculateTaxesUseCaseTest {

    private lateinit var useCase: CalculateTaxesUseCase

    @Before
    fun setUp() {
        useCase = CalculateTaxesUseCase()
    }

    @Test
    fun `current law - parcel under 150 EUR threshold should be exempt from all taxes`() {
        // Parcel: 100 EUR, EUR rate = 20 MDL -> 2000 MDL
        val result = useCase(
            parcelValue = 100.0,
            shippingCost = 10.0,
            selectedCurrencyRateToMdl = 20.0,
            eurRateToMdl = 20.0,
            dutyPercentage = 0.10,
            applyJuly2026Rules = false
        )

        assertEquals(0.0, result.totalMdl, 0.001)
        assertEquals(0.0, result.dutyMdl, 0.001)
        assertEquals(0.0, result.vatMdl, 0.001)
        assertEquals(0.0, result.procedureFeeMdl, 0.001)
        assertNotNull(result.exemptionMessage)
        assertEquals(TaxConstants.LEGISLATION_URL, result.exemptionLink)
    }

    @Test
    fun `current law - parcel over 150 EUR should calculate duty, vat and procedure fee`() {
        // Parcel: 200 EUR (4000 MDL), Shipping: 20 EUR (400 MDL)
        // Base = 4400 MDL
        // Duty (10%) = 440 MDL
        // VAT (20% of Base + Duty) = (4400 + 440) * 0.20 = 968 MDL
        // Fee = 50 MDL
        // Total = 440 + 968 + 50 = 1458 MDL
        val result = useCase(
            parcelValue = 200.0,
            shippingCost = 20.0,
            selectedCurrencyRateToMdl = 20.0,
            eurRateToMdl = 20.0,
            dutyPercentage = 0.10,
            applyJuly2026Rules = false
        )

        assertEquals(4400.0, result.baseMdl, 0.001)
        assertEquals(440.0, result.dutyMdl, 0.001)
        assertEquals(968.0, result.vatMdl, 0.001)
        assertEquals(50.0, result.procedureFeeMdl, 0.001)
        assertEquals(1458.0, result.totalMdl, 0.001)
        assertNull(result.exemptionMessage)
    }

    @Test
    fun `upcoming law - parcel under 150 EUR applies 20 percent VAT on parcel value and 12 MDL fee`() {
        // Parcel: 100 EUR (2000 MDL), Shipping: 10 EUR
        // Under upcoming law <= 150 EUR:
        // Base = 2000 MDL (shipping not included)
        // Duty = 0 MDL
        // VAT = 2000 * 0.20 = 400 MDL
        // Fee = 12 MDL
        // Total = 412 MDL
        val result = useCase(
            parcelValue = 100.0,
            shippingCost = 10.0,
            selectedCurrencyRateToMdl = 20.0,
            eurRateToMdl = 20.0,
            dutyPercentage = 0.10,
            applyJuly2026Rules = true
        )

        assertEquals(2000.0, result.baseMdl, 0.001)
        assertEquals(0.0, result.dutyMdl, 0.001)
        assertEquals(400.0, result.vatMdl, 0.001)
        assertEquals(12.0, result.procedureFeeMdl, 0.001)
        assertEquals(412.0, result.totalMdl, 0.001)
    }

    @Test
    fun `upcoming law - parcel over 150 EUR applies standard calculation with 50 MDL fee`() {
        // Parcel: 200 EUR (4000 MDL), Shipping: 20 EUR (400 MDL)
        // Base = 4400 MDL
        // Duty (10%) = 440 MDL
        // VAT (20%) = 968 MDL
        // Fee = 50 MDL
        // Total = 1458 MDL
        val result = useCase(
            parcelValue = 200.0,
            shippingCost = 20.0,
            selectedCurrencyRateToMdl = 20.0,
            eurRateToMdl = 20.0,
            dutyPercentage = 0.10,
            applyJuly2026Rules = true
        )

        assertEquals(4400.0, result.baseMdl, 0.001)
        assertEquals(440.0, result.dutyMdl, 0.001)
        assertEquals(968.0, result.vatMdl, 0.001)
        assertEquals(50.0, result.procedureFeeMdl, 0.001)
        assertEquals(1458.0, result.totalMdl, 0.001)
    }

    @Test
    fun `zero or negative EUR rate should not crash or divide by zero`() {
        val result = useCase(
            parcelValue = 100.0,
            shippingCost = 0.0,
            selectedCurrencyRateToMdl = 1.0,
            eurRateToMdl = 0.0, // Invalid rate test
            dutyPercentage = 0.0,
            applyJuly2026Rules = false
        )

        assertNotNull(result)
    }
}
