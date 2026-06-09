cd /root/pokeBot
java \
  -javaagent:/opt/datadog-agent/client-agent/dd-java-agent.jar \
  -Ddd.service=pokebot \
  -Ddd.env=production \
  -jar ./PokeBot-1.0.0-SNAPSHOT.jar --spring.profiles.active=production \
  >> /var/log/pokeBot/pokeBot_sh.log 2>> /var/log/pokeBot/pokeBot_sh-error.log
