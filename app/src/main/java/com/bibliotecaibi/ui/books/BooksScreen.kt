package com.bibliotecaibi.ui.books

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bibliotecaibi.repo.DbProvider
import com.bibliotecaibi.data.entities.Book
import com.bibliotecaibi.data.entities.Inventory
import com.bibliotecaibi.repo.SessionHolder
import com.bibliotecaibi.repo.BookMetaRepository
import kotlinx.coroutines.launch

@Composable
fun BooksScreen() {

    val ctx = LocalContext.current
    val db = DbProvider.get(ctx)
    val scope = rememberCoroutineScope()
    val session = SessionHolder.session!!
    val branchId = session.branchId!!

    // State
    var isbn by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var publisher by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var qty by remember { mutableStateOf("1") }
    var message by remember { mutableStateOf<String?>(null) }

    Column(Modifier.padding(16.dp)) {

        Text("📚 Livros & Estoque", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))

        // -------- Scanner ----------
        Text("Ler ISBN:")
        Spacer(Modifier.height(8.dp))
        IsbnScanner { detected ->
            isbn = detected
        }

        Spacer(Modifier.height(16.dp))

        // ---------- Entrada manual ----------
        OutlinedTextField(
            value = isbn,
            onValueChange = { isbn = it },
            label = { Text("ISBN") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(value = title, onValueChange = { title = it },
            label = { Text("Título") }, modifier = Modifier.fillMaxWidth())

        OutlinedTextField(value = author, onValueChange = { author = it },
            label = { Text("Autor") }, modifier = Modifier.fillMaxWidth())

        OutlinedTextField(value = publisher, onValueChange = { publisher = it },
            label = { Text("Editora") }, modifier = Modifier.fillMaxWidth())

        OutlinedTextField(value = year, onValueChange = { year = it },
            label = { Text("Ano") }, modifier = Modifier.fillMaxWidth())

        OutlinedTextField(value = qty, onValueChange = { qty = it.filter { c -> c.isDigit() } },
            label = { Text("Quantidade (entrada)") }, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(16.dp))

        Row {
            // ---------- Botão buscar metadados ----------
            Button(onClick = {
                scope.launch {
                    if (isbn.isBlank()) {
                        message = "Informe ou leia um ISBN."
                        return@launch
                    }
                    val meta = BookMetaRepository.fetch(isbn)
                    if (meta == null) {
                        message = "Livro não encontrado nas bases online."
                    } else {
                        title = meta.title ?: ""
                        author = meta.author ?: ""
                        publisher = meta.publisher ?: ""
                        year = meta.year?.toString() ?: ""
                        message = "Metadados carregados!"
                    }
                }
            }) {
                Text("Buscar Metadados")
            }

            Spacer(Modifier.width(8.dp))

            // ---------- Botão salvar ----------
            Button(onClick = {
                scope.launch {
                    if (isbn.isBlank() || title.isBlank()) {
                        message = "ISBN e Título são obrigatórios."
                        return@launch
                    }

                    val book = Book(
                        isbn = isbn,
                        title = title,
                        author = author.ifBlank { null },
                        publisher = publisher.ifBlank { null },
                        year = year.toIntOrNull()
                    )

                    db.bookDao().upsert(book)

                    val inv = db.inventoryDao().get(branchId, isbn)
                    val quantity = qty.toIntOrNull() ?: 0

                    val newInv = if (inv == null) {
                        Inventory(
                            isbn = isbn,
                            branchId = branchId,
                            quantityTotal = quantity,
                            quantityAvailable = quantity
                        )
                    } else {
                        inv.copy(
                            quantityTotal = inv.quantityTotal + quantity,
                            quantityAvailable = inv.quantityAvailable + quantity
                        )
                    }

                    db.inventoryDao().upsert(newInv)
                    message = "Livro salvo e estoque atualizado!"
                }
            }) {
                Text("Salvar / Entrada")
            }
        }

        Spacer(Modifier.height(16.dp))

        if (message != null) {
            Text(message!!, color = MaterialTheme.colorScheme.primary)
        }
    }
}
