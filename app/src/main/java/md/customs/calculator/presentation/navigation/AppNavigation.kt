package md.customs.calculator.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import md.customs.calculator.di.AppViewModelProvider
import md.customs.calculator.presentation.calculator.CalculatorScreen
import md.customs.calculator.presentation.calculator.CalculatorViewModel
import md.customs.calculator.presentation.history.HistoryScreen
import md.customs.calculator.presentation.history.HistoryViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "calculator") {
        composable("calculator") {
            val calculatorViewModel: CalculatorViewModel = viewModel(factory = AppViewModelProvider.Factory)
            CalculatorScreen(
                viewModel = calculatorViewModel,
                onNavigateToHistory = { navController.navigate("history") }
            )
        }
        composable("history") {
            val historyViewModel: HistoryViewModel = viewModel(factory = AppViewModelProvider.Factory)
            HistoryScreen(
                viewModel = historyViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
