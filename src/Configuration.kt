package com.raphaelbussa

object Configuration {

    val botUsername: String get() = System.getProperty("AOC_BOT_USERNAME")
    val botToken: String get() = System.getProperty("AOC_BOT_TOKEN")

    val dbHost: String get() = System.getProperty("AOC_DB_HOST")
    val dbPort: String get() = System.getProperty("AOC_DB_PORT")
    val dbDatabase: String get() = System.getProperty("AOC_DB_DATABASE")
    val dbUser: String get() = System.getProperty("AOC_DB_USER")
    val dbPassword: String get() = System.getProperty("AOC_DB_PASSWORD", "")

}