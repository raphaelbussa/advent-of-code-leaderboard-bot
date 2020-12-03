package com.raphaelbussa.database

import com.raphaelbussa.database.entity.Session
import com.raphaelbussa.database.table.Sessions
import org.jetbrains.exposed.sql.and

interface DatabaseDao {

    suspend fun getActiveSessionsCount(): Long

    suspend fun getSessionById(chatId: Long): Session

    suspend fun saveLeaderboardId(chatId: Long, leaderboardId: Int)

    suspend fun saveCookie(chatId: Long, cookie: String)

    suspend fun deleteSession(chatId: Long)

}

object RealDatabaseDao : DatabaseDao {

    override suspend fun getActiveSessionsCount(): Long {
        return executeTransaction {
            return@executeTransaction Session.find {
                Sessions.leaderboardId neq "" and (Sessions.cookie neq "")
            }.count()
        }
    }

    override suspend fun getSessionById(chatId: Long): Session {
        return executeTransaction {
            return@executeTransaction Session.findById(chatId) ?: Session.new(chatId) {
                leaderboardId = ""
                cookie = ""
            }
        }
    }

    override suspend fun saveLeaderboardId(chatId: Long, leaderboardId: Int) {
        executeTransaction {
            val session = getSessionById(chatId)
            session.leaderboardId = leaderboardId.toString()
        }
    }

    override suspend fun saveCookie(chatId: Long, cookie: String) {
        executeTransaction {
            val session = getSessionById(chatId)
            session.cookie = cookie
        }
    }

    override suspend fun deleteSession(chatId: Long) {
        executeTransaction {
            Session.findById(chatId)?.delete()
        }
    }

}