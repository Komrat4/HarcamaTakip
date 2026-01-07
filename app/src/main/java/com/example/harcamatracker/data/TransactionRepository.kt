package com.example.harcamatracker.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.YearMonth

class TransactionRepository(
    private val dao: TransactionDao
) {
    fun observeTransactionsForMonth(month: YearMonth): Flow<List<TransactionEntity>> {
        val start = month.atDay(1)
        val end = month.plusMonths(1).atDay(1)
        return dao.observeTransactionsBetween(start, end)
    }

    fun observeCategoryTotalsForMonth(
        type: TransactionType,
        month: YearMonth
    ): Flow<List<CategoryTotal>> {
        val start = month.atDay(1)
        val end = month.plusMonths(1).atDay(1)
        return dao.observeCategoryTotalsBetween(type, start, end)
    }

    suspend fun insert(
        amountCents: Long,
        category: String,
        note: String,
        type: TransactionType,
        date: LocalDate
    ) {
        dao.insert(
            TransactionEntity(
                amountCents = amountCents,
                category = category,
                note = note,
                type = type,
                date = date
            )
        )
    }

    suspend fun delete(transaction: TransactionEntity) {
        dao.delete(transaction)
    }
}
