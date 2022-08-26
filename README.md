# Minecord

Minecord is a simple Minecraft plugin that connects a Minecraft server's in-game chat with a Discord channel so that the
messages can be interchanged between the two.

## Building

To build Minecord, you need JDK 16 or higher installed on your system.
<br />
Minecord uses [Maven](https://maven.apache.org/) as its build automation tool

```bash
git clone https://github.com/ChampionBuffalo1/Minecord.git
cd Minecord
mvn clean install
```

Minecord outputs the plugin as a [UberJar](https://imagej.net/develop/uber-jars)

## Privileged Intents

The plugin requires `Server Members` and `Message Content` Intents to be enabled from
[Discord Developer Portal](https://discord.com/developers/applications).

## Dependencies

* [JDA (Java Discord API)](https://github.com/DV8FromTheWorld/JDA)
* [PaperMC API](https://jd.papermc.io/paper/1.19/)