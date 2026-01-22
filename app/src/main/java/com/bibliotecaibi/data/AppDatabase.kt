package com.bibliotecaibi.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bibliotecaibi.data.dao.*
import com.bibliotecaibi.data.entities.*

@Database(
    version = 1,
    entities = [
        Owner::class,
        Branch::class,
        User::class,
        Config::class,
        Book::class,
        Inventory::class,
        Member::class,
        Loan::class
    ],
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ownerDao(): OwnerDao
    abstract fun branchDao(): BranchDao
    abstract fun userDao(): UserDao
    abstract fun configDao(): ConfigDao
    abstract fun bookDao(): BookDao
    abstract fun inventoryDao(): InventoryDao
    abstract fun memberDao(): MemberDao
    abstract fun loanDao(): LoanDao
}
