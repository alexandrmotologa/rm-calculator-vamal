package md.customs.calculator

import android.app.Application
import md.customs.calculator.di.AppContainer
import md.customs.calculator.di.DefaultAppContainer

class CalculatorApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
