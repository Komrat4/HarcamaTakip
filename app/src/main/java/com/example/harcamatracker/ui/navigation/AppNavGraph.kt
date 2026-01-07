package com.example.harcamatracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.harcamatracker.ui.TransactionViewModel
import com.example.harcamatracker.ui.screens.AddTransactionScreen
import com.example.harcamatracker.ui.screens.SummaryScreen

@Composable
fun AppNavGraph(viewModel: TransactionViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Destinations.SUMMARY) {
        composable(Destinations.SUMMARY) {
            SummaryScreen(
                viewModel = viewModel,
                onAddTransaction = { navController.navigate(Destinations.ADD) }
            )
        }
        composable(Destinations.ADD) {
            AddTransactionScreen(
                onSave = { amountCents, category, note, type, date ->
                    viewModel.addTransaction(amountCents, category, note, type, date)
                    navController.popBackStack()
                },
                onCancel = { navController.popBackStack() }
            )
        }
    }
}
