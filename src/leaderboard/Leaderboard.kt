package com.raphaelbussa.leaderboard

import com.raphaelbussa.bot.Command
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking

object Leaderboard {

    fun getLeaderboardMessage(id: String, session: String, result: (String) -> Unit) {
        HttpClient(OkHttp) {
            engine {
                preconfigured
            }
            install(JsonFeature) {
                serializer = GsonSerializer()
                accept(ContentType.Application.Json)
            }
        }.use {
            runBlocking {
                try {
                    val response: Welcome = it.get(urlString = "https://adventofcode.com/2020/leaderboard/private/view/$id.json") {
                        header("Cookie", "session=$session")
                    }
                    val stringBuilder = StringBuilder()
                    response.sortedMembers.forEachIndexed { index, member ->
                        stringBuilder.append(index + 1)
                        stringBuilder.append(" - ")
                        stringBuilder.append("*")
                        stringBuilder.append(member.name)
                        stringBuilder.append("*")
                        stringBuilder.append(" - ")
                        stringBuilder.append("[${member.localScore}:${member.globalScore}]")
                        stringBuilder.append(" - ")
                        member.sortedCompletionDayLevel.forEach { day ->
                            when (day.values.size) {
                                0 -> stringBuilder.append(" ")
                                1 -> stringBuilder.append("☆")
                                2 -> stringBuilder.append("★")
                            }
                        }
                        stringBuilder.append("\n")
                    }
                    result(stringBuilder.toString())
                } catch (e: Exception) {
                    result("Please use ${Command.SESSION.value} to update your session cookie")
                }
            }
        }
    }

    private val Welcome.sortedMembers get() = members.toList().sortedBy { it.first }.sortedBy { -it.second.stars }.map { it.second }

    private val Member.sortedCompletionDayLevel get() = completionDayLevel.toList().sortedBy { it.first }.map { it.second }

}