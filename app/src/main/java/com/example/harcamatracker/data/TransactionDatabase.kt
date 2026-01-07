package com.example.harcamatracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [TransactionEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TransactionDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao

    companion object {
        fun build(context: Context): TransactionDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                TransactionDatabase::class.java,
                "transactions.db"
            ).build()
        }
    }
}
