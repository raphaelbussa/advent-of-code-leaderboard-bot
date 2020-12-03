package com.raphaelbussa.ktor

import com.google.gson.Gson
import com.raphaelbussa.database.DatabaseDao
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.content.*
import io.ktor.routing.*

internal fun Application.setupRouting(databaseDao: DatabaseDao, gson: Gson) {
    routing {
        get("/") {
            call.respondHtml { renderHome() }
        }
        get("/stats") {
            call.handleStatsApi(databaseDao, gson)
        }
        static("static") {
            static("css") { resources("css") }
            static("images") { resources("images") }
        }
    }
}