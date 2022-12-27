# twitterbot-poke
[twitter-api-java-sdk](https://github.com/twitterdev/twitter-api-java-sdk)を用いた自動ツイート、自動いいね、自動リプライ プログラム。<br>
機能の詳細は[こちら](https://haroot.net/bot)。<br>
<br>

## 技術要素

| 技術                 | バージョン | description                                  |
| -------------------- | ---------- | -------------------------------------------- |
| Java                 | 17.0.3     | 開発言語                                     |
| Spring Boot          | 3.0.1      | フレームワーク                               |
| Gradle               | 7.6        | Java用ビルドツール                           |
| SpringToolSuite      | 4.14.0     | Eclipseを拡張したSpring用開発ツール          |
| twitter-api-java-sdk | 2.0.3      | twitter-apiのJava用SDK                       |
| Ascii Tree Generator | 1.2.4      | markdownでのファイルtree生成(VSCode拡張機能) |
| Text Tables          | 0.1.5      | markdownでのテーブル編集(VSCode拡張機能)     |
<br>

## application.yaml

下記のプロパティを含む必要があります。

```
.
├── batch
│   └── cron
│       ├── like
│       └── tweet
└── file-path
    └── resources
        ├── base-url
        ├── user-info
        ├── token
        └── pokedex
```