clientId=$(security find-generic-password -s pokeBot -a clientId -w)
clientSecret=$(security find-generic-password -s pokeBot -a clientSecret -w)
bearer=$(echo -n "${clientId}:${clientSecret}" | base64 | tr -d '\n')

curl -X POST "https://api.x.com/2/oauth2/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -u "${clientId}:${clientSecret}" \
  -d "grant_type=authorization_code" \
  -d "code=${code}" \
  -d "redirect_uri=https://haroot.net/twitter-auth" \
  -d "client_id=${clientId}" \
  -d "code_verifier=${codeVerifier}"
