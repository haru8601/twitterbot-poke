# twitterbot-poke

[twitter-api-java-sdk](https://github.com/twitterdev/twitter-api-java-sdk)を用いた自動ツイート、自動いいね、自動リプライ プログラム。<br>
機能の詳細は[こちら](https://haroot.net/poke/bot)。<br>
<br>

## 技術要素

| 技術                 | バージョン | description                                      |
| -------------------- | ---------- | ------------------------------------------------ |
| Java                 | 17.0.3     | 開発言語                                         |
| Spring Boot          | 3.0.1      | フレームワーク                                   |
| Gradle               | 7.6        | Java 用ビルドツール                              |
| SpringToolSuite      | 4.14.0     | Eclipse を拡張した Spring 用開発ツール           |
| twitter-api-java-sdk | 2.0.3      | twitter-api の Java 用 SDK                       |
| Ascii Tree Generator | 1.2.4      | markdown でのファイル tree 生成(VSCode 拡張機能) |
| Text Tables          | 0.1.5      | markdown でのテーブル編集(VSCode 拡張機能)       |

<br>

## デプロイ

STS(SpringToolSuite)の場合、<br>

1. `Gradle Tasks`> `build` > `bootJar`

`/build/libs`に jar ファイルが生成される。

## application.yaml

下記のプロパティを含む必要があります。

```sh
.
├── batch
│   └── cron
│       └── tweet # 0 0 20 * * *
├── file-path
│   └── resources
│       ├── base-url # src/main/resources/static
│       ├── token # /token.json
│       └── pokedex # /pokedex.json
├── path
│   └── log
│       ├── all # /hoge/log/app.log
│       └── error # /hoge/log/app-error.log
└── user-info
    ├── my-id # 1234567890123456789
    ├── client-id # XXXXXZZZZZaaaaa00000xxxxxYYYYXXXXX
    ├── client-secret # 略
    ├── bearer # 略
    ├── redirect-url # https://haroot.net
    └── access-scope # offline.access tweet.read tweet.write users.read like.write
```
