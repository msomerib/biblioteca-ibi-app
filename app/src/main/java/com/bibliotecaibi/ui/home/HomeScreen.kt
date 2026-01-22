package com.bibliotecaibi.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.bibliotecaibi.ui.navigation.Routes

@Composable
fun HomeScreen(nav: NavHostController) {

    Column(Modifier.padding(16.dp)) {

        Text("Biblioteca IBI", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(20.dp))

        Button(
            onClick = { nav.navigate(Routes.BOOKS) },
            modifier = Modifier.fillMaxWidth()
        ) { Text("📚 Livros e Estoque") }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = { nav.navigate(Routes.MEMBERS) },
            modifier = Modifier.fillMaxWidth()
        ) { Text("👥 Sócios") }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = { nav.navigate(Routes.LOANS) },
            modifier = Modifier.fillMaxWidth()
        ) { Text("🔄 Empréstimos") }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = { nav.navigate(Routes.REPORTS) },
            modifier = Modifier.fillMaxWidth()
        ) { Text("📄 Relatórios") }
    }
}
