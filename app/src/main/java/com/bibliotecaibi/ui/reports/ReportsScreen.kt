package com.bibliotecaibi.ui.reports

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.bibliotecaibi.repo.DbProvider
import com.bibliotecaibi.repo.SessionHolder
import com.bibliotecaibi.util.ExcelUtil
import com.bibliotecaibi.util.PdfUtil
import com.bibliotecaibi.util.ReportFormatters
import kotlinx.coroutines.launch

@Composable
fun ReportsScreen() {

    val ctx = LocalContext.current
    val db = DbProvider.get(ctx)
    val scope = rememberCoroutineScope()
    val branchId = SessionHolder.session!!.branchId!!

    var inventoryRows by remember { mutableStateOf(listOf<List<String>>()) }
    var openLoanRows by remember { mutableStateOf(listOf<List<String>>()) }
    var lateLoanRows by remember { mutableStateOf(listOf<List<String>>()) }

    // Carregar dados
    LaunchedEffect(Unit) {
        val inv = db.inventoryDao().listByBranch(branchId)
        val open = db.loanDao().openAtBranch(branchId)

        inventoryRows = ReportFormatters.formatInventory(inv)
        openLoanRows = ReportFormatters.formatOpenLoans(open)
        lateLoanRows = ReportFormatters.formatLateLoans(open)
    }

    // --------------------- Launchers ------------------------

    val pdfLauncher = rememberLauncherForActivityResult(CreateDocument("application/pdf")) { uri: Uri? ->
        if (uri != null) {
            PdfUtil.writeTablePdf(
                ctx,
                uri,
                "Relatório de Estoque",
                listOf("ISBN", "Total", "Disponível"),
                inventoryRows
            )
        }
    }

    val xlsLauncher = rememberLauncherForActivityResult(
        CreateDocument("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    ) { uri: Uri? ->
        if (uri != null) {
            ExcelUtil.writeExcel(
                ctx,
                uri,
                "Estoque",
                listOf("ISBN", "Total", "Disponível"),
                inventoryRows
            )
        }
    }

    val pdfLoansLauncher = rememberLauncherForActivityResult(CreateDocument("application/pdf")) { uri: Uri? ->
        if (uri != null) {
            PdfUtil.writeTablePdf(
                ctx,
                uri,
                "Empréstimos Abertos",
                listOf("ISBN", "Retirada", "Devolver até", "ID Sócio"),
                openLoanRows
            )
        }
    }

    val pdfLateLauncher = rememberLauncherForActivityResult(CreateDocument("application/pdf")) { uri: Uri? ->
        if (uri != null) {
            PdfUtil.writeTablePdf(
                ctx,
                uri,
                "Empréstimos Atrasados",
                listOf("ISBN", "Retirada", "Deveria devolver", "ID Sócio"),
                lateLoanRows
            )
        }
    }

    // --------------------- UI ------------------------

    Column(Modifier.padding(16.dp)) {

        Text("📄 Relatórios", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(20.dp))

        Button(
            onClick = { pdfLauncher.launch("estoque.pdf") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Exportar Estoque (PDF)")
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = { xlsLauncher.launch("estoque.xlsx") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Exportar Estoque (Excel)")
        }

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = { pdfLoansLauncher.launch("emprestimos.pdf") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Empréstimos Abertos (PDF)")
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = { pdfLateLauncher.launch("atrasados.pdf") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Empréstimos Atrasados (PDF)")
        }
    }
}
