package com.bibliotecaibi.ui.loans

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bibliotecaibi.repo.DbProvider
import com.bibliotecaibi.repo.SessionHolder
import com.bibliotecaibi.data.entities.Loan
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun LoansScreen() {

    val ctx = LocalContext.current
    val db = DbProvider.get(ctx)
    val scope = rememberCoroutineScope()

    val session = SessionHolder.session!!
    val branchId = session.branchId!!
    val ownerId = session.ownerId

    // Sócios
    var members by remember { mutableStateOf(db.memberDao().listActive(branchId)) }
    var selectedMember by remember { mutableStateOf(members.firstOrNull()) }

    // ISBN informado
    var isbn by remember { mutableStateOf("") }

    // Lista de empréstimos abertos
    var openLoans by remember { mutableStateOf(db.loanDao().openAtBranch(branchId)) }

    // Configurações do dono
    var config by remember { mutableStateOf(db.configDao().getByOwner(ownerId)) }

    var message by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        members = db.memberDao().listActive(branchId)
        selectedMember = members.firstOrNull()
        openLoans = db.loanDao().openAtBranch(branchId)
        config = db.configDao().getByOwner(ownerId)
    }

    Column(Modifier.padding(16.dp)) {

        Text("🔄 Empréstimos", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        // ---------- Seleção de Sócio ----------
        if (selectedMember != null) {
            Text("Sócio Selecionado: ${selectedMember!!.name}")
        } else {
            Text("Nenhum sócio cadastrado!")
            return@Column
        }

        Spacer(Modifier.height(12.dp))

        // ---------- ISBN ----------
        OutlinedTextField(
            value = isbn,
            onValueChange = { isbn = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("ISBN (digite ou escaneie)") }
        )

        Spacer(Modifier.height(12.dp))

        Row {

            // ---------------------------------------------------
            // BOTÃO: REALIZAR EMPRÉSTIMO
            // ---------------------------------------------------
            Button(
                onClick = {
                    scope.launch {

                        val member = selectedMember ?: return@launch

                        // 1) Verificar limite do sócio
                        val openForMember = db.loanDao().openForMember(member.memberId)
                        if (openForMember.size >= (config?.memberLoanLimit ?: 3)) {
                            message = "O sócio atingiu o limite de empréstimos."
                            return@launch
                        }

                        // 2) Verificar estoque
                        val inv = db.inventoryDao().get(branchId, isbn)
                        if (inv == null || inv.quantityAvailable <= 0) {
                            message = "Livro indisponível na filial."
                            return@launch
                        }

                        // 3) Criar empréstimo
                        val now = System.currentTimeMillis()
                        val dueDate =
                            now + (config?.defaultLoanDays ?: 7) * 24L * 60L * 60L * 1000L

                        db.loanDao().create(
                            Loan(
                                isbn = isbn,
                                memberId = member.memberId,
                                branchId = branchId,
                                loanDate = now,
                                dueDate = dueDate
                            )
                        )

                        // 4) Atualizar estoque
                        db.inventoryDao().upsert(
                            inv.copy(quantityAvailable = inv.quantityAvailable - 1)
                        )

                        // 5) Atualizar lista
                        openLoans = db.loanDao().openAtBranch(branchId)

                        message = "Empréstimo registrado com sucesso!"
                        isbn = ""
                    }
                }
            ) {
                Text("Emprestar")
            }

            Spacer(Modifier.width(8.dp))

            // ---------------------------------------------------
            // BOTÃO: DEVOLUÇÃO
            // ---------------------------------------------------
            Button(
                onClick = {
                    scope.launch {
                        // procura empréstimos abertos com o ISBN informado
                        val loan = openLoans.firstOrNull { it.isbn == isbn }

                        if (loan == null) {
                            message = "Nenhum empréstimo aberto encontrado para este ISBN."
                            return@launch
                        }

                        // marcar devolução
                        val updated = loan.copy(
                            status = "RETURNED",
                            returnDate = System.currentTimeMillis()
                        )
                        db.loanDao().update(updated)

                        // devolver ao estoque
                        val inv = db.inventoryDao().get(branchId, isbn)
                        if (inv != null) {
                            db.inventoryDao().upsert(
                                inv.copy(quantityAvailable = inv.quantityAvailable + 1)
                            )
                        }

                        openLoans = db.loanDao().openAtBranch(branchId)
                        message = "Livro devolvido com sucesso!"
                        isbn = ""
                    }
                }
            ) {
                Text("Devolver")
            }
        }

        Spacer(Modifier.height(16.dp))

        if (message != null) {
            Text(message!!, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(16.dp))
        }

        Divider()
        Spacer(Modifier.height(16.dp))

        // ---------- LISTA DE EMPRÉSTIMOS EM ABERTO ----------
        Text("📌 Empréstimos em Aberto", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        LazyColumn {
            items(openLoans) { loan ->
                Column(Modifier.padding(vertical = 6.dp)) {

                    Text("ISBN: ${loan.isbn}")
                    Text("Sócio ID: ${loan.memberId}")

                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val date = sdf.format(Date(loan.dueDate))

                    Text("Devolver até: $date")
                }
            }
        }
    }
}
