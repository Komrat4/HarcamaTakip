package com.example.harcamatracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.harcamatracker.data.TransactionEntity
import com.example.harcamatracker.data.TransactionType
import java.time.format.DateTimeFormatter

@Composable
fun TransactionItemRow(transaction: TransactionEntity, modifier: Modifier = Modifier) {
    val sign = if (transaction.type == TransactionType.INCOME) "+" else "-"
    val dateText = transaction.date.format(DateTimeFormatter.ISO_LOCAL_DATE)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.category,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            if (transaction.note.isNotBlank()) {
                Text(
                    text = transaction.note,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                text = dateText,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Text(
            text = "$sign${formatCents(transaction.amountCents)}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private fun formatCents(amountCents: Long): String {
    val abs = kotlin.math.abs(amountCents)
    val major = abs / 100
    val minor = abs % 100
    return "%d.%02d".format(major, minor)
}
