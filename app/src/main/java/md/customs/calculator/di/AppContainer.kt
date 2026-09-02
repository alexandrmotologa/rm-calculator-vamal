package md.customs.calculator.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.Room
import md.customs.calculator.CalculatorApplication
import md.customs.calculator.data.local.AppDatabase
import md.customs.calculator.data.local.datastore.SettingsManager
import md.customs.calculator.data.remote.api.BnmApiService
import md.customs.calculator.data.repository.ExchangeRateRepositoryImpl
import md.customs.calculator.data.repository.HistoryRepositoryImpl
import md.customs.calculator.domain.repository.ExchangeRateRepository
import md.customs.calculator.domain.repository.HistoryRepository
import md.customs.calculator.domain.usecase.CalculateTaxesUseCase
import md.customs.calculator.presentation.calculator.CalculatorViewModel
import md.customs.calculator.presentation.history.HistoryViewModel
import nl.adaptivity.xmlutil.serialization.XML
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

interface AppContainer {
    val bnmApiService: BnmApiService
    val appDatabase: AppDatabase
    val settingsManager: SettingsManager
    val exchangeRateRepository: ExchangeRateRepository
    val historyRepository: HistoryRepository
    val calculateTaxesUseCase: CalculateTaxesUseCase
}

class DefaultAppContainer(private val context: Context) : AppContainer {

    override val settingsManager: SettingsManager by lazy {
        SettingsManager(context)
    }

    override val appDatabase: AppDatabase by lazy {
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration()
         .build()
    }

    private val retrofit: Retrofit by lazy {
        val contentType = MediaType.parse("application/xml")!!
        val xmlFormat = XML {
            defaultPolicy { ignoreUnknownChildren() }
        }
        Retrofit.Builder()
            .baseUrl(BnmApiService.BASE_URL)
            .addConverterFactory(xmlFormat.asConverterFactory(contentType))
            .build()
    }

    override val bnmApiService: BnmApiService by lazy {
        retrofit.create(BnmApiService::class.java)
    }

    override val exchangeRateRepository: ExchangeRateRepository by lazy {
        ExchangeRateRepositoryImpl(bnmApiService, settingsManager)
    }

    override val historyRepository: HistoryRepository by lazy {
        HistoryRepositoryImpl(appDatabase.calculationHistoryDao)
    }

    override val calculateTaxesUseCase: CalculateTaxesUseCase by lazy {
        CalculateTaxesUseCase()
    }
}

/**
 * Extension function to initialize view models with ViewModelProvider.Factory.
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CalculatorApplication)
            CalculatorViewModel(
                calculateTaxesUseCase = app.container.calculateTaxesUseCase,
                exchangeRateRepository = app.container.exchangeRateRepository,
                historyRepository = app.container.historyRepository,
                settingsManager = app.container.settingsManager
            )
        }
        initializer {
            val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CalculatorApplication)
            HistoryViewModel(
                historyRepository = app.container.historyRepository
            )
        }
    }
}
