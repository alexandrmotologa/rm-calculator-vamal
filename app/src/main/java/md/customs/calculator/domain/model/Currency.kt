package md.customs.calculator.domain.model

/**
 * Domain model representing supported fiat currencies.
 */
enum class Currency(val code: String) {
    MDL("MDL"),
    EUR("EUR"),
    USD("USD"),
    RON("RON"),
    GBP("GBP");

    companion object {
        fun fromCode(code: String): Currency {
            return entries.find { it.code.equals(code, ignoreCase = true) } ?: MDL
        }
    }
}
