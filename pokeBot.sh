cd /root/pokeBot
java \
  -javaagent:/opt/datadog-packages/datadog-apm-library-java/stable/dd-java-agent.jar \
  -Ddd.service=pokebot \
  -Ddd.env=production \
  -jar ./PokeBot-0.0.1-SNAPSHOT.jar --spring.profiles.active=production \
  >> /var/log/pokeBot/pokeBot_sh.log 2>> /var/log/pokeBot/pokeBot_sh-error.log &
