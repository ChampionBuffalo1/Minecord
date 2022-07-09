# Minecord

Minecord is a simple Minecraft plugin that connects a Minecraft server's in-game chat with a Discord channel so that the messages can be interchanged between the two.

## Building
 To build Minecord, you need JDK 16 or higher installed on your system.
 <br />
 Minecord uses [Maven](https://maven.apache.org/) as its build automation tool

```bash
git clone https://github.com/ChampionBuffalo1/Minecord.git
cd Minecord
mvn clean install
```

Minecord outputs the plugin as a [Fat Jar](https://imagej.net/develop/uber-jars)

## Dependencies 
  * [JDA (Java Discord API)](https://mvnrepository.com/artifact/net.dv8tion/JDA)
  * [SpigotMC API](https://hub.spigotmc.org/)