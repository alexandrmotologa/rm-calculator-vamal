package md.customs.calculator.data.repository

import kotlinx.coroutines.flow.firstOrNull
import md.customs.calculator.data.local.datastore.SettingsManager
import md.customs.calculator.data.remote.api.BnmApiService
import md.customs.calculator.domain.repository.ExchangeRateRepository

class ExchangeRateRepositoryImpl(
    private val apiService: BnmApiService,
    private val settingsManager: SettingsManager
) : ExchangeRateRepository {

    override suspend fun getRates(currentDate: String): Map<String, Float> {
        val lastSync = settingsManager.lastSyncDate.firstOrNull()

        // 1. If synced today, return cached values
        if (lastSync == currentDate) {
            val cached = settingsManager.cachedRates.firstOrNull()
            if (cached != null && cached.values.all { it > 0f }) {
                return cached
            }
        }

        // 2. Fetch from network
        try {
            val response = apiService.getExchangeRates(date = currentDate)
            var eur = 0f
            var usd = 0f
            var ron = 0f
            var gbp = 0f

            response.valutes.forEach { valute ->
                val rate = valute.value.replace(",", ".").toFloatOrNull() ?: 0f
                // Apply the nominal value correctly
                val unitRate = if (valute.nominal > 0) rate / valute.nominal else rate

                when (valute.charCode.uppercase()) {
                    "EUR" -> eur = unitRate
                    "USD" -> usd = unitRate
                    "RON" -> ron = unitRate
                    "GBP" -> gbp = unitRate
                }
            }

            // Verify rates are fetched correctly
            if (eur > 0f && usd > 0f && ron > 0f && gbp > 0f) {
                // Save the new rates
                settingsManager.saveExchangeRates(eur = eur, usd = usd, ron = ron, gbp = gbp)
                settingsManager.saveLastSyncDate(currentDate)

                return mapOf(
                    "EUR" to eur,
                    "USD" to usd,
                    "RON" to ron,
                    "GBP" to gbp
                )
            } else {
                throw Exception("Rates for EUR, USD, RON, or GBP missing from BNM response.")
            }
        } catch (e: Exception) {
            // 3. Fallback to cache if network fails (e.g. no internet)
            val cached = settingsManager.cachedRates.firstOrNull()
            if (cached != null && cached.values.all { it > 0f }) {
                return cached
            }
            throw Exception("Network error and no local cached rates available.", e)
        }
    }
}
