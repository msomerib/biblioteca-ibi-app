package com.bibliotecaibi.repo

import android.content.Context
import com.bibliotecaibi.util.Security
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(private val ctx: Context) {

    private val db = DbProvider.get(ctx)

    suspend fun login(ownerId: Long, username: String, password: String): Session? =
        withContext(Dispatchers.IO) {

            val user = db.userDao().findByUsername(ownerId, username)
                ?: return@withContext null

            return@withContext if (Security.verify(password, user.salt, user.passwordHash)) {
                Session(
                    ownerId = user.ownerId,
                    userId = user.userId,
                    role = user.role
                )
            } else null
        }
}
