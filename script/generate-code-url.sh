clientId=$(security find-generic-password -s pokeBot -a clientId -w)
# CSRFを防ぐための任意の文字列
state=request-by-haroot8601
# ランダムな文字列
codeVerifier=$(openssl rand -base64 32 | tr -d '=' | tr '/+' '_-')
# codeVerifierのハッシュ化
codeChallenge=$(printf '%s' "$codeVerifier" | openssl dgst -sha256 -binary | base64 | tr -d '=' | tr '/+' '_-')
echo codeVerifier=${codeVerifier}
echo "https://x.com/i/oauth2/authorize?response_type=code&client_id=${clientId}&redirect_uri=https://haroot.net/twitter-auth&scope=tweet.write%20tweet.read%20users.read%20offline.access&state=${state}&code_challenge=${codeChallenge}&code_challenge_method=S256"
