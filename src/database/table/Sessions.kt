package com.raphaelbussa.database.table

import org.jetbrains.exposed.dao.id.LongIdTable

object Sessions : LongIdTable() {
    val cookie = text(name = "cookie")
    val leaderboardId = text(name = "leaderboard_id")
}
