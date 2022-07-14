package com.champ.minecord;

import com.champ.minecord.discord.DiscordJDAConnection;
import com.champ.minecord.listeners.ChatEventListener;
import com.champ.minecord.listeners.DeathListener;
import com.champ.minecord.listeners.JoinLeaveListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Minecord extends JavaPlugin {
    private static Minecord plugin;

    public static Minecord getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        DiscordJDAConnection.InitiateConnection(this);
        // Listeners
        new ChatEventListener();
        new JoinLeaveListener();
        new DeathListener();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        // Properly Shutdown JDA connection
        if (DiscordJDAConnection.getJda() != null)
            DiscordJDAConnection.getJda().shutdownNow();
    }
    public void disableSelf() {
        Bukkit.getPluginManager().disablePlugin(this);
    }
}
