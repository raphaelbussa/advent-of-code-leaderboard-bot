package com.raphaelbussa.bot

import com.raphaelbussa.Configuration
import com.raphaelbussa.database.DatabaseDao
import com.raphaelbussa.database.entity.cookieIsSet
import com.raphaelbussa.database.entity.leaderboardIdIsSet
import com.raphaelbussa.leaderboard.Leaderboard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException
import org.telegram.telegrambots.meta.updateshandlers.SentCallback
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import kotlin.coroutines.CoroutineContext

class Bot(
    coroutineContext: CoroutineContext,
    private val databaseDao: DatabaseDao
) : TelegramLongPollingBot() {

    private val scope = CoroutineScope(coroutineContext)

    override fun getBotToken() = Configuration.botToken

    override fun getBotUsername() = Configuration.botUsername

    override fun onUpdateReceived(update: Update?) {
        update?.handleStartCommand()
        update?.handleCodeCommand()
        update?.handleSessionCommand()
        update?.handleLeaderboardCommand()
        update?.handleStatsCommand()
        update?.handleRemovedBot()
    }

    private fun Update.handleStartCommand() {
        isCommand(Command.START) {
            val name = when {
                message.chat.firstName != null -> message.chat.firstName
                message.chat.title != null -> message.chat.title
                else -> "stranger"
            }
            sendMessage(
                chatId = message.chatId,
                text = "Hello *$name* to start use ${Command.CODE.value} and ${Command.SESSION.value} to init the bot"
            )
        }
    }

    private fun Update.handleCodeCommand() {
        isCommand(Command.CODE) { text ->
            val id = text.toIntOrNull()
            scope.launch {
                if (id != null) databaseDao.saveLeaderboardId(message.chatId, id)
                val session = databaseDao.getSessionById(message.chatId)
                val response = when {
                    session.leaderboardIdIsSet && session.cookieIsSet -> "All setup now you can use ${Command.LEADERBOARD.value}"
                    session.leaderboardIdIsSet && !session.cookieIsSet -> "Please now use ${Command.SESSION.value} to complete the setup"
                    else -> "Oops, something was wrong, please try again"
                }
                sendMessage(
                    chatId = message.chatId,
                    text = response
                )
            }
        }
    }

    private fun Update.handleSessionCommand() {
        isCommand(Command.SESSION) { text ->
            val id = text.trim()
            scope.launch {
                databaseDao.saveCookie(message.chatId, id)
                val session = databaseDao.getSessionById(message.chatId)
                val response = when {
                    session.leaderboardIdIsSet && session.cookieIsSet -> "All setup now you can use ${Command.LEADERBOARD.value}"
                    !session.leaderboardIdIsSet && session.cookieIsSet -> "Please now use ${Command.CODE.value} to complete the setup"
                    else -> "Oops, something was wrong, please try again"
                }
                sendMessage(
                    chatId = message.chatId,
                    text = response
                )
            }
        }
    }

    private fun Update.handleLeaderboardCommand() {
        isCommand(Command.LEADERBOARD) {
            scope.launch {
                val session = databaseDao.getSessionById(message.chatId)
                if (session.cookieIsSet && session.leaderboardIdIsSet) {
                    Leaderboard.getLeaderboardMessage(
                        id = session.leaderboardId,
                        session = session.cookie
                    ) { response ->
                        sendMessage(
                            chatId = message.chatId,
                            text = response
                        )
                    }
                } else {
                    val response = when {
                        !session.leaderboardIdIsSet && session.cookieIsSet -> "Please now use ${Command.CODE.value} to complete the setup"
                        session.leaderboardIdIsSet && !session.cookieIsSet -> "Please now use ${Command.SESSION.value} to complete the setup"
                        !session.leaderboardIdIsSet && !session.cookieIsSet -> "Please use ${Command.CODE.value} and ${Command.SESSION.value} to init the bot"
                        else -> "Oops, something was wrong, please try again"
                    }
                    sendMessage(
                        chatId = message.chatId,
                        text = response
                    )
                }
            }
        }
    }

    private fun Update.handleStatsCommand() {
        isCommand(Command.STATS) {
            scope.launch {
                val count = databaseDao.getActiveSessionsCount()
                sendMessage(
                    chatId = message.chatId,
                    text = "Currently we have $count active sessions"
                )
            }
        }
    }

    private fun Update.handleRemovedBot() {
        message.leftChatMember?.let {
            if (it.isBot && it.userName == Configuration.botUsername) {
                //remove chat data from the DB
                scope.launch {
                    databaseDao.deleteSession(message.chatId)
                }
            }
        }
    }

}

enum class Command(val value: String) {
    START("/start"),
    CODE("/code"),
    SESSION("/session"),
    LEADERBOARD("/leaderboard"),
    STATS("/stats")
}

private fun Update.isCommand(command: Command, block: Update.(String) -> Unit) {
    if (message.isCommand && message.text.startsWith(command.value)) {
        block(this, message.text.replace(command.value, "").trimStart())
    }
}

private fun TelegramLongPollingBot.sendMessage(chatId: Long, text: String, result: (Boolean) -> Unit = {}) {
    val sendMessage = SendMessage().apply {
        setChatId(chatId.toString())
        setText(text)
        enableMarkdown(true)
    }
    executeAsync(sendMessage, object : SentCallback<Message> {
        override fun onResult(method: BotApiMethod<Message>?, response: Message?) {
            result(true)
        }

        override fun onError(method: BotApiMethod<Message>?, apiException: TelegramApiRequestException?) {
            result(false)
        }

        override fun onException(method: BotApiMethod<Message>?, exception: Exception?) {
            result(false)
        }
    })
}

object BotSingleton {

    private val botsApi = TelegramBotsApi(DefaultBotSession::class.java)

    fun init(coroutineContext: CoroutineContext, databaseDao: DatabaseDao) {
        val bot = Bot(coroutineContext, databaseDao)
        botsApi.registerBot(bot)
    }

}