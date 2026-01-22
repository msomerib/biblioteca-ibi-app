package com.bibliotecaibi.repo

import android.content.Context
import androidx.room.Room
import com.bibliotecaibi.data.AppDatabase

object DbProvider {

    @Volatile
    private var db: AppDatabase? = null

    fun get(context: Context): AppDatabase =
        db ?: synchronized(this) {
            db ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "biblioteca_ibi.db"
            )
            // .addMigrations(MIGRATION_1_2)   // futuras migrações (versões novas)
            .build()
        }.also { db = it }
}
