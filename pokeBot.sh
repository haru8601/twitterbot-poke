cd /root/pokeBot
java -javaagent:/opt/datadog-packages/datadog-apm-library-java/stable/dd-java-agent.jar -jar ./PokeBot-0.0.1-SNAPSHOT.jar --spring.profiles.active=production >> ./logs/pokeBot_sh.log 2>> ./logs/pokeBot_sh-error.log &
