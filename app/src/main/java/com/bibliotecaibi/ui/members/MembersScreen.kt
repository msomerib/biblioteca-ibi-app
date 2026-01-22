package com.bibliotecaibi.ui.members

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
import com.bibliotecaibi.data.entities.Member
import kotlinx.coroutines.launch

@Composable
fun MembersScreen() {

    val ctx = LocalContext.current
    val db = DbProvider.get(ctx)
    val scope = rememberCoroutineScope()

    val session = SessionHolder.session!!
    val branchId = session.branchId!!

    // Campos do formulário
    var name by remember { mutableStateOf("") }
    var documentId by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") } // formato dd/mm/aaaa
    var planCategory by remember { mutableStateOf("") }

    // Lista de sócios
    var members by remember { mutableStateOf(listOf<Member>()) }

    // Mensagem de status
    var message by remember { mutableStateOf<String?>(null) }

    // Carregar sócios da filial
    LaunchedEffect(Unit) {
        members = db.memberDao().listActive(branchId)
    }

    Column(Modifier.padding(16.dp)) {

        Text("👥 Cadastro de Sócios", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))

        // ---------- Formulário ----------

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nome completo") }
        )

        OutlinedTextField(
            value = documentId,
            onValueChange = { documentId = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Documento (CPF/RG)") }
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("E-mail") }
        )

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Telefone") }
        )

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Endereço completo") }
        )

        OutlinedTextField(
            value = birthDate,
            onValueChange = { birthDate = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Data de Nascimento (dd/mm/aaaa)") }
        )

        OutlinedTextField(
            value = planCategory,
            onValueChange = { planCategory = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Plano / Categoria") }
        )

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {
                scope.launch {
                    if (name.isBlank()) {
                        message = "O nome é obrigatório."
                        return@launch
                    }

                    // converter data dd/mm/aaaa para epoch millis (opcional)
                    val birthMillis = try {
                        val parts = birthDate.split("/")
                        if (parts.size == 3) {
                            val day = parts[0].toInt()
                            val month = parts[1].toInt() - 1
                            val year = parts[2].toInt()
                            val cal = java.util.Calendar.getInstance().apply {
                                set(year, month, day, 0, 0, 0)
                                set(java.util.Calendar.MILLISECOND, 0)
                            }
                            cal.timeInMillis
                        } else null
                    } catch (e: Exception) { null }

                    // Criar o sócio
                    db.memberDao().insert(
                        Member(
                            branchId = branchId,
                            name = name,
                            email = email.ifBlank { null },
                            phone = phone.ifBlank { null },
                            documentId = documentId.ifBlank { null },
                            address = address.ifBlank { null },
                            birthDate = birthMillis,
                            planCategory = planCategory.ifBlank { null }
                        )
                    )

                    // Atualizar a lista
                    members = db.memberDao().listActive(branchId)

                    // Limpar formulário
                    name = ""
                    documentId = ""
                    email = ""
                    phone = ""
                    address = ""
                    birthDate = ""
                    planCategory = ""

                    message = "Sócio cadastrado com sucesso!"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar Sócio")
        }

        Spacer(Modifier.height(16.dp))

        if (message != null) {
            Text(message!!, color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(16.dp))
        }

        Divider()

        Spacer(Modifier.height(16.dp))
        Text("Sócios da Filial", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        LazyColumn {
            items(members) { m ->
                Column(Modifier.padding(vertical = 6.dp)) {
                    Text("• ${m.name}", style = MaterialTheme.typography.bodyLarge)
                    if (m.documentId != null)
                        Text("  Doc: ${m.documentId}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
