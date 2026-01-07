package com.example.harcamatracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.harcamatracker.data.CategoryTotal
import com.example.harcamatracker.data.TransactionEntity
import com.example.harcamatracker.data.TransactionRepository
import com.example.harcamatracker.data.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

class TransactionViewModel(
    private val repository: TransactionRepository
) : ViewModel() {
    private val _selectedMonth = MutableStateFlow(YearMonth.now())
    val selectedMonth: StateFlow<YearMonth> = _selectedMonth.asStateFlow()

    val transactions: StateFlow<List<TransactionEntity>> = selectedMonth
        .flatMapLatest { repository.observeTransactionsForMonth(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val expenseCategoryTotals: StateFlow<List<CategoryTotal>> = selectedMonth
        .flatMapLatest { repository.observeCategoryTotalsForMonth(TransactionType.EXPENSE, it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val incomeCategoryTotals: StateFlow<List<CategoryTotal>> = selectedMonth
        .flatMapLatest { repository.observeCategoryTotalsForMonth(TransactionType.INCOME, it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val totalExpenseCents: StateFlow<Long> = transactions
        .map { list -> list.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amountCents } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0L)

    val totalIncomeCents: StateFlow<Long> = transactions
        .map { list -> list.filter { it.type == TransactionType.INCOME }.sumOf { it.amountCents } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0L)

    fun previousMonth() {
        _selectedMonth.value = _selectedMonth.value.minusMonths(1)
    }

    fun nextMonth() {
        _selectedMonth.value = _selectedMonth.value.plusMonths(1)
    }

    fun setMonth(month: YearMonth) {
        _selectedMonth.value = month
    }

    fun addTransaction(
        amountCents: Long,
        category: String,
        note: String,
        type: TransactionType,
        date: LocalDate
    ) {
        viewModelScope.launch {
            repository.insert(amountCents, category, note, type, date)
        }
    }

    fun deleteTransaction(transaction: TransactionEntity) {
        viewModelScope.launch {
            repository.delete(transaction)
        }
    }
}

class TransactionViewModelFactory(
    private val repository: TransactionRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransactionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
