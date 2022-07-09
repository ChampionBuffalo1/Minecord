package com.champ.minecord;

import com.champ.minecord.discord.DiscordJDAConnection;
import org.bukkit.plugin.java.JavaPlugin;

public final class Minecord extends JavaPlugin {
    private static Minecord plugin;
    @Override
    public void onEnable() {
        plugin = this;
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        DiscordJDAConnection.InitiateConnection(this);
    }

    public static Minecord getPlugin() {
        return plugin;
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        // Properly Shutdown JDA connection
        if (DiscordJDAConnection.getJda() != null)
	  DiscordJDAConnection.getJda().shutdownNow();
    }
}
