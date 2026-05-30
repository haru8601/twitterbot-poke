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

tweet検索

```sh
bearerToken=$(security find-generic-password -s pokeBot -a bearerToken -w)
curl "https://api.x.com/2/tweets/search/recent?query=from%3Aharoot_net" -H "Authorization: Bearer ${bearerToken}"  | jq
```

code取得用URL生成

```sh
sh script/generate-code-url.sh
```

accessToken取得
Developer Console > Agent で`generate_oauth2_user_token`を実行させる

### 以下古いtwitterAPIのデータ取得方法メモ

accessToken取得
```sh
codeVerifier=code取得時に生成されたランダムな文字列
code=code取得で得た値

clientId=$(security find-generic-password -s pokeBot -a clientId -w)
clientSecret=$(security find-generic-password -s pokeBot -a clientSecret -w)
echo "curl -X POST 'https://api.x.com/2/oauth2/token' \
-u '${clientId}:${clientSecret}' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'code=${code}' \
--data-urlencode 'grant_type=authorization_code' \
--data-urlencode 'client_id=${clientId}' \
--data-urlencode 'redirect_uri=https://haroot.net/twitter-auth' \
--data-urlencode 'code_verifier=${codeVerifier}'"
```

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
