package com.raphaelbussa.ktor

import com.raphaelbussa.Configuration
import kotlinx.html.*

fun HTML.renderHome() {
    head {
        title { +"Advent of Code Leaderboard" }
        styleLink("/static/css/aoc.css")
        link(rel = "shortcut icon", href = "/static/images/favicon.png")
    }
    body {
        div("content") {
            h1 { +"Advent of Code Leaderboard" }
            h2 { +"A Telegram Bot" }
            div("icons") {
                a("https://t.me/s/${Configuration.botUsername}") {
                    img(src = "/static/images/telegram.png")
                }
                a("/stats") {
                    img(src = "/static/images/api.png")
                }
            }
        }
    }
}