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

## 開発

### ローカルから実行

[application.yaml](./src/main/resources/application.yaml)の`batch.cron`を直後の時間になるよう設定し、 \
VSCode拡張の `Spring Boot Dashboard` > `PokeBot` > `Run` > `PokeBotApplication`

## 運用

### twitter(X)API


macOSでのキーチェーンアクセス(tips)

```sh
# パスワード登録
security add-generic-password -s pokeBot -a clientSecret -w

# パスワード更新
security add-generic-password -s pokeBot -a clientSecret -U -w

# パスワード取得
security find-generic-password -s pokeBot -a clientId -w
```

### 認可取得

Developer Console > Agent から `generate_oauth2_user_token`を依頼

### 認可取り消し

Developer Console > Agent から
`revoke_oauth2_user_token`を依頼

### tweet検索

```sh
bearerToken=$(security find-generic-password -s pokeBot -a bearerToken -w)
curl "https://api.x.com/2/tweets/search/recent?query=from%3Aharoot_net" -H "Authorization: Bearer ${bearerToken}"  | jq
```

### code取得用URL生成

1. ブラウザからXに**bot用アカウントで**ログインしておく
2. 下記を実行

```sh
# ローカルPCにclientIdを設定
security add-generic-password -s pokeBot -a clientId -U -w
# コード取得用URLを生成
sh script/generate-code-url.sh
```

### accessToken取得

Developer Console と ツイートアカウントが異なる場合、
Developer ConsoleのAgentでは難しいため手動でアクセストークン取得を行う必要がある。

```sh
# 環境変数設定
security add-generic-password -s pokeBot -a clientId -U -w
security add-generic-password -s pokeBot -a clientSecret -U -w

# 認可取得で生成されたパラメータの設定
codeVerifier=code取得時に生成された値 \
code=code取得時に生成された値 \
./generate-oauth2-token.sh
```

### 以下古いtwitterAPIのデータ取得方法メモ

refreshToken生成

```sh
curl --location --request POST 'https://api.twitter.com/2/oauth2/token' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'refresh_token=${refreshToken}' \
--data-urlencode 'grant_type=refresh_token' \
--data-urlencode 'client_id=${clientId}'
```

auth認証(client credential)
```sh
curl --location --request POST 'https://api.twitter.com/oauth2/token' \
--basic -u '${clientId}:${clientSecret}' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'code=${code}' \
--data-urlencode 'grant_type=authorization_code' \
--data-urlencode 'client_id=${clientId}' \
--data-urlencode 'redirect_uri=http://localhost:8080/test' \
--data-urlencode 'code_verifier=${codeVerifier}'
```

auth認証(auth)

```sh
curl --location --request POST 'https://api.twitter.com/2/oauth2/token' \
--basic -u '${clientId}:${clientSecret}'\
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'code=${code}' \
--data-urlencode 'grant_type=authorization_code' \
--data-urlencode 'client_id=${clientId}' \
--data-urlencode 'redirect_uri=http://localhost:8080/test' \
--data-urlencode 'code_verifier=${codeVerifier}'
```

tweet

```sh
curl -X POST https://api.twitter.com/2/tweets \
-H "Authorization: Bearer ${clientId}" \
-H "Content-type: application/json" \
-d '{"text": "api test"}'
```

beaderトークン取得

```sh
clientId=$(security find-generic-password -s pokeBot -a clientId -w)
clientSecret=$(security find-generic-password -s pokeBot -a clientSecret -w)
curl --request POST -u${clientId}:${clientSecret} \
  --url 'https://api.twitter.com/oauth2/token?grant_type=client_credentials'
```

## デプロイ

VSCodeの`Gradle for Java`拡張で、
Tasks > build > bootJar を実行

[build/libs](./build/libs/)の`PokeBot-0.0.1-SNAPSHOT.jar`が更新される。

本番環境の`/root/pokeBot/PokeBot-0.0.1-SNAPSHOT.jar`を上書きする。

([pokeBot.sh](./pokeBot.sh)がcronでjarを実行する)

## application.yaml

下記のプロパティを含む必要があります。

```sh
.
├── batch
│   └── cron
│       └── tweet
├── file-path
│   └── resources
│       ├── base-url
│       ├── token
│       └── pokedex
├── path
│   └── log
│       ├── all
│       └── error
└── user-info
    ├── my-id
    ├── client-id
    ├── client-secret
    ├── bearer
    ├── redirect-url
    └── access-scope
```
