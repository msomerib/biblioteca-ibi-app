package com.bibliotecaibi.ui.auth

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.bibliotecaibi.repo.AuthRepository
import com.bibliotecaibi.repo.DbProvider
import com.bibliotecaibi.repo.SessionHolder
import com.bibliotecaibi.ui.navigation.Routes
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(nav: NavHostController) {

    val ctx = LocalContext.current
    val db = DbProvider.get(ctx)
    val scope = rememberCoroutineScope()

    var owners by remember { mutableStateOf(listOf<com.bibliotecaibi.data.entities.Owner>()) }
    var selectedOwner by remember { mutableStateOf<com.bibliotecaibi.data.entities.Owner?>(null) }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    // carregar owners
    LaunchedEffect(Unit) {
        owners = db.ownerDao().list()
        if (owners.isNotEmpty()) selectedOwner = owners.first()
    }

    Column(Modifier.padding(16.dp)) {

        Text("Biblioteca IBI — Login", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        Text("Selecione o Dono (Owner)", style = MaterialTheme.typography.labelMedium)

        owners.forEach { owner ->
            Button(
                onClick = { selectedOwner = owner },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                colors = if (selectedOwner == owner)
                    ButtonDefaults.buttonColors()
                else
                    ButtonDefaults.outlinedButtonColors()
            ) {
                Text(owner.name)
            }
        }

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Usuário") }
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Senha") }
        )

        if (error != null) {
            Text(error!!, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                val owner = selectedOwner ?: return@Button
                scope.launch {
                    val auth = AuthRepository(ctx)
                    val session = auth.login(owner.ownerId, username, password)

                    if (session != null) {
                        SessionHolder.session = session
                        nav.navigate(Routes.BRANCH) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    } else {
                        error = "Usuário ou senha inválidos."
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar")
        }
    }
}
