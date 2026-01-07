package com.example.harcamatracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transaction: TransactionEntity)

    @Query(
        """
        SELECT * FROM transactions
        WHERE date >= :startInclusive AND date < :endExclusive
        ORDER BY date DESC, id DESC
        """
    )
    fun observeTransactionsBetween(
        startInclusive: LocalDate,
        endExclusive: LocalDate
    ): Flow<List<TransactionEntity>>

    @Query(
        """
        SELECT category, SUM(amountCents) AS totalCents
        FROM transactions
        WHERE type = :type AND date >= :startInclusive AND date < :endExclusive
        GROUP BY category
        ORDER BY totalCents DESC
        """
    )
    fun observeCategoryTotalsBetween(
        type: TransactionType,
        startInclusive: LocalDate,
        endExclusive: LocalDate
    ): Flow<List<CategoryTotal>>
}
