package com.raphaelbussa

import com.google.gson.GsonBuilder
import com.raphaelbussa.bot.BotSingleton
import com.raphaelbussa.database.Database
import com.raphaelbussa.database.RealDatabaseDao
import com.raphaelbussa.ktor.setupRouting
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import javax.print.attribute.standard.Compression

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(CORS) { anyHost() }
    install(DefaultHeaders) { header("X-Engine", "Ktor") }
    install(ContentNegotiation) { gson() }
    Database.init()
    BotSingleton.init(coroutineContext, RealDatabaseDao)
    setupRouting(databaseDao = RealDatabaseDao, gson = GsonBuilder().create())
}
