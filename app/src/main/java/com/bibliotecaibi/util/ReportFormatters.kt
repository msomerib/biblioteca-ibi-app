package com.bibliotecaibi.util

import com.bibliotecaibi.data.entities.Inventory
import com.bibliotecaibi.data.entities.Loan
import java.text.SimpleDateFormat
import java.util.*

object ReportFormatters {

    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    fun formatInventory(inventory: List<Inventory>): List<List<String>> {
        return inventory.map {
            listOf(
                it.isbn,
                it.quantityTotal.toString(),
                it.quantityAvailable.toString()
            )
        }
    }

    fun formatOpenLoans(loans: List<Loan>): List<List<String>> {
        return loans.map {
            listOf(
                it.isbn,
                sdf.format(Date(it.loanDate)),
                sdf.format(Date(it.dueDate)),
                it.memberId.toString()
            )
        }
    }

    fun formatLateLoans(loans: List<Loan>): List<List<String>> {
        val now = System.currentTimeMillis()
        return loans.filter { it.status == "OPEN" && it.dueDate < now }
            .map {
                listOf(
                    it.isbn,
                    sdf.format(Date(it.loanDate)),
                    sdf.format(Date(it.dueDate)),
                    it.memberId.toString()
                )
            }
    }
}
