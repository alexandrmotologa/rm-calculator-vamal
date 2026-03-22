package md.customs.calculator.data.remote.api

import md.customs.calculator.data.remote.dto.BnmExchangeRateResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BnmApiService {

    companion object {
        const val BASE_URL = "https://www.bnm.md/"
    }

    /**
     * Fetch daily official exchange rates.
     * The date must be provided in exactly "DD.MM.YYYY" format.
     */
    @GET("ro/official_exchange_rates")
    suspend fun getExchangeRates(
        @Query("get_xml") getXml: Int = 1,
        @Query("date") date: String
    ): BnmExchangeRateResponse
}
