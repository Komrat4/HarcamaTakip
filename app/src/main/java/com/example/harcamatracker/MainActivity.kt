package com.example.harcamatracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.harcamatracker.ui.TransactionViewModel
import com.example.harcamatracker.ui.TransactionViewModelFactory
import com.example.harcamatracker.ui.navigation.AppNavGraph
import com.example.harcamatracker.ui.theme.HarcamaTakipTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as HarcamaTakipApplication
        setContent {
            HarcamaTakipTheme {
                val viewModel: TransactionViewModel = viewModel(
                    factory = TransactionViewModelFactory(app.repository)
                )
                AppNavGraph(viewModel = viewModel)
            }
        }
    }
}
