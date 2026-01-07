package com.example.harcamatracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.harcamatracker.data.CategoryTotal
import com.example.harcamatracker.ui.TransactionViewModel
import com.example.harcamatracker.ui.components.TransactionItemRow
import java.time.format.DateTimeFormatter

@Composable
fun SummaryScreen(
    viewModel: TransactionViewModel,
    onAddTransaction: () -> Unit
) {
    val selectedMonth by viewModel.selectedMonth.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    val expenseTotals by viewModel.expenseCategoryTotals.collectAsState()
    val incomeTotals by viewModel.incomeCategoryTotals.collectAsState()
    val totalExpenseCents by viewModel.totalExpenseCents.collectAsState()
    val totalIncomeCents by viewModel.totalIncomeCents.collectAsState()
    val netCents = totalIncomeCents - totalExpenseCents

    val monthLabel = selectedMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy"))

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { viewModel.previousMonth() }) {
                Text("Previous")
            }
            Text(text = monthLabel, style = MaterialTheme.typography.titleLarge)
            TextButton(onClick = { viewModel.nextMonth() }) {
                Text("Next")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Income", style = MaterialTheme.typography.labelLarge)
                Text(
                    text = formatCents(totalIncomeCents),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Column {
                Text("Expense", style = MaterialTheme.typography.labelLarge)
                Text(
                    text = formatCents(totalExpenseCents),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("Net", style = MaterialTheme.typography.labelLarge)
                Text(
                    text = formatCents(netCents),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onAddTransaction, modifier = Modifier.fillMaxWidth()) {
            Text("Add Transaction")
        }

        Spacer(modifier = Modifier.height(16.dp))

        CategoryTotalsSection(title = "Expenses by Category", totals = expenseTotals)
        Spacer(modifier = Modifier.height(12.dp))
        CategoryTotalsSection(title = "Income by Category", totals = incomeTotals)

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Transactions",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Divider(modifier = Modifier.padding(vertical = 8.dp))

        LazyColumn {
            items(transactions, key = { it.id }) { transaction ->
                TransactionItemRow(transaction = transaction)
                Divider()
            }
        }
    }
}

@Composable
private fun CategoryTotalsSection(title: String, totals: List<CategoryTotal>) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold
    )
    if (totals.isEmpty()) {
        Text(
            text = "No data yet",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    } else {
        totals.forEach { total ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(total.category)
                Text(formatCents(total.totalCents))
            }
        }
    }
}

private fun formatCents(amountCents: Long): String {
    val abs = kotlin.math.abs(amountCents)
    val major = abs / 100
    val minor = abs % 100
    val sign = if (amountCents < 0) "-" else ""
    return "$sign%d.%02d".format(major, minor)
}
