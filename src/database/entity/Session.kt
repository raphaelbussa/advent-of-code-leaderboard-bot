package com.raphaelbussa.database.entity

import com.raphaelbussa.database.table.Sessions
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.telegram.telegrambots.meta.api.objects.Update

class Session(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Session>(Sessions)
    var cookie by Sessions.cookie
    var leaderboardId by Sessions.leaderboardId
}

val Session.cookieIsSet: Boolean get() = cookie.isNotEmpty()
val Session.leaderboardIdIsSet: Boolean get() = leaderboardId.isNotEmpty()