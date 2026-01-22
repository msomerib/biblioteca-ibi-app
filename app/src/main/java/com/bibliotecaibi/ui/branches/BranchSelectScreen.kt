package com.bibliotecaibi.ui.branches

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.bibliotecaibi.repo.DbProvider
import com.bibliotecaibi.repo.SessionHolder
import com.bibliotecaibi.repo.Session
import com.bibliotecaibi.ui.navigation.Routes

@Composable
fun BranchSelectScreen(nav: NavHostController) {

    val ctx = LocalContext.current
    val db = DbProvider.get(ctx)
    val session = SessionHolder.session!!

    var branches by remember { mutableStateOf(listOf<com.bibliotecaibi.data.entities.Branch>()) }

    LaunchedEffect(Unit) {
        branches = db.branchDao().listByOwner(session.ownerId)
    }

    Column(Modifier.padding(16.dp)) {
        Text("Selecione a Filial", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        branches.forEach { branch ->
            Button(
                onClick = {
                    SessionHolder.session = session.copy(branchId = branch.branchId)

                    nav.navigate(Routes.HOME) {
                        popUpTo(Routes.BRANCH) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Text(branch.name)
            }
        }
    }
}
