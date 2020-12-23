# Advent of Code telegram leaderboard bot 👋

> A simple telegram bot to get a private Advent of Code leaderboard directly in your group (or chatting with it directly)

### 🏠 [Homepage](https://aoc2020.bussa.dev)

## ⚙️ Configuration

Needed properties to run the bot

```
AOC_BOT_USERNAME
AOC_BOT_TOKEN
AOC_DB_HOST
AOC_DB_PORT
AOC_DB_DATABASE
AOC_DB_USER
AOC_DB_PASSWORD
```

You also need to update the bot username in `Configuration.kt`

```kotlin
val botUsername: String get() = "AdventOfCodeLeaderboardBot"
```

## 👤 Author

**Raphaël Bussa**

* Website: [raphael.bussa.dev](https://raphael.bussa.dev)
* Twitter: [@raphaelbussa](https://twitter.com/raphaelbussa)
* Github: [@raphaelbussa](https://github.com/raphaelbussa)
* LinkedIn: [@raphaelbussa](https://linkedin.com/in/raphaelbussa)

## 🤝 Contributing

Contributions, issues and feature requests are welcome!

Feel free to check [issues page](https://github.com/raphaelbussa/advent-of-code-leaderboard-bot/issues).

## 🙏Thanks

Thanks to [Marco Dalla Santa](https://github.com/marcodallasanta) for setting up the server and to [Krzysztof Klimkiewicz](https://github.com/krzkz94) for helping me with the CSS

Thanks also to [readme-md-generator](https://github.com/kefranabg/readme-md-generator) for generating this lovely README

## 📝 License

Copyright © 2020 [Raphaël Bussa](https://github.com/raphaelbussa).

This project is [MIT](https://github.com/raphaelbussa/advent-of-code-leaderboard-bot/blob/main/LICENSE.md) licensed.

```
         |
        -+-
         A
        /=\               /\  /\    ___  _ __  _ __ __    __
      i/ O \i            /  \/  \  / _ \| '__|| '__|\ \  / /
      /=====\           / /\  /\ \|  __/| |   | |    \ \/ /
      /  i  \           \ \ \/ / / \___/|_|   |_|     \  /
    i/ O * O \i                                       / /
    /=========\        __  __                        /_/    _
    /  *   *  \        \ \/ /        /\  /\    __ _  ____  | |
  i/ O   i   O \i       \  /   __   /  \/  \  / _` |/ ___\ |_|
  /=============\       /  \  |__| / /\  /\ \| (_| |\___ \  _
  /  O   i   O  \      /_/\_\      \ \ \/ / / \__,_|\____/ |_|
i/ *   O   O   * \i
/=================\
       |___|
```
