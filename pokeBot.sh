cd /root/pokeBot
java -jar ./PokeBot-0.0.1-SNAPSHOT.jar --spring.profiles.active=production >> ./logs/pokeBot_sh.log 2>> ./logs/pokeBot_sh-error.log &
