package com.raphaelbussa.ktor

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.raphaelbussa.database.DatabaseDao
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*

suspend fun ApplicationCall.handleStatsApi(databaseDao: DatabaseDao, gson: Gson) {
    val count = databaseDao.getActiveSessionsCount()
    respondText(
        contentType = ContentType.Application.Json,
        status = HttpStatusCode.OK,
        text = gson.toJson(StatsResponse(activeSessions = count))
    )
}

data class StatsResponse(
    @SerializedName("active_sessions") val activeSessions: Long
)