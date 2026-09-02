package md.customs.calculator.domain.repository

/**
 * Domain repository contract for obtaining exchange rates.
 */
interface ExchangeRateRepository {
    /**
     * Obtains exchange rates (EUR, USD, RON, GBP) for the specified date.
     *
     * @param currentDate Date string in format "DD.MM.YYYY".
     * @return Map of currency code to its parsed float rate in MDL.
     */
    suspend fun getRates(currentDate: String): Map<String, Float>
}
