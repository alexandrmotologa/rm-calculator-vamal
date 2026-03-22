package md.customs.calculator.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsManager(private val context: Context) {

    companion object {
        val LAST_SYNC_DATE = stringPreferencesKey("last_sync_date")
        
        val RATE_EUR = floatPreferencesKey("rate_eur")
        val RATE_USD = floatPreferencesKey("rate_usd")
        val RATE_RON = floatPreferencesKey("rate_ron")
        val RATE_GBP = floatPreferencesKey("rate_gbp")
    }

    val lastSyncDate: Flow<String?> = context.dataStore.data.map { prefs ->
        prefs[LAST_SYNC_DATE]
    }

    val cachedRates: Flow<Map<String, Float>> = context.dataStore.data.map { prefs ->
        mapOf(
            "EUR" to (prefs[RATE_EUR] ?: 0f),
            "USD" to (prefs[RATE_USD] ?: 0f),
            "RON" to (prefs[RATE_RON] ?: 0f),
            "GBP" to (prefs[RATE_GBP] ?: 0f)
        )
    }

    suspend fun saveLastSyncDate(dateStr: String) {
        context.dataStore.edit { prefs ->
            prefs[LAST_SYNC_DATE] = dateStr
        }
    }

    suspend fun saveExchangeRates(eur: Float, usd: Float, ron: Float, gbp: Float) {
        context.dataStore.edit { prefs ->
            prefs[RATE_EUR] = eur
            prefs[RATE_USD] = usd
            prefs[RATE_RON] = ron
            prefs[RATE_GBP] = gbp
        }
    }
}
