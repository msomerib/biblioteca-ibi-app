package com.bibliotecaibi.repo

import android.content.Context
import com.bibliotecaibi.data.entities.*
import com.bibliotecaibi.util.Security
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object Seed {

    suspend fun ensureInitialData(ctx: Context) = withContext(Dispatchers.IO) {
        val db = DbProvider.get(ctx)

        // Se já houver Owner, não cria nada
        if (db.ownerDao().list().isNotEmpty()) return@withContext

        // Dono padrão
        val ownerId = db.ownerDao().insert(
            Owner(name = "Biblioteca IBI")
        )

        // Filial padrão
        val branchId = db.branchDao().insert(
            Branch(
                ownerId = ownerId,
                name = "Matriz",
                addressLine1 = "Endereço padrão"
            )
        )

        // Configurações iniciais do owner
        db.configDao().upsert(
            Config(
                ownerId = ownerId,
                defaultLoanDays = 7,
                memberLoanLimit = 3
            )
        )

        // Criação do usuário admin (login)
        val salt = Security.genSalt()
        val hash = Security.hashPassword("admin123", salt)

        db.userDao().insert(
            User(
                ownerId = ownerId,
                username = "admin",
                passwordHash = hash,
                salt = salt,
                role = "ADMIN"
            )
        )
    }
}
