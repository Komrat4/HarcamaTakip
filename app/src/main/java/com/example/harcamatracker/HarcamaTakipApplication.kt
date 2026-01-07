package com.example.harcamatracker

import android.app.Application
import com.example.harcamatracker.data.TransactionDatabase
import com.example.harcamatracker.data.TransactionRepository

class HarcamaTakipApplication : Application() {
    val database: TransactionDatabase by lazy {
        TransactionDatabase.build(this)
    }

    val repository: TransactionRepository by lazy {
        TransactionRepository(database.transactionDao())
    }
}
