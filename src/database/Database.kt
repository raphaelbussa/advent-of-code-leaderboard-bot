package com.raphaelbussa.database

import com.raphaelbussa.Configuration
import com.raphaelbussa.database.table.Sessions
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync
import org.jetbrains.exposed.sql.transactions.transaction

object Database {

    val database by lazy {
        Database.connect(
            url = databaseUrl,
            driver = "org.postgresql.Driver",
            user = Configuration.dbUser,
            password = Configuration.dbPassword
        )
    }

    fun init() {
        transaction(database) {
            SchemaUtils.create(Sessions)
        }
    }

    private val databaseUrl get() = "jdbc:postgresql://${Configuration.dbHost}:${Configuration.dbPort}/${Configuration.dbDatabase}"

}

suspend fun <T> executeTransaction(statement: suspend Transaction.() -> T): T {
    return suspendedTransactionAsync(db = com.raphaelbussa.database.Database.database) { statement(this) }.await()
}
