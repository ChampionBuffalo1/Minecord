package com.champ.minecord;

import com.champ.minecord.discord.DiscordJDAConnection;
import com.champ.minecord.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
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
        new Settings();
        DiscordJDAConnection.InitiateConnection();
        // Listeners
        new ServerEvents();
        new DeathListener();
        new ChatEventListener();
        new JoinLeaveListener();
        new AdvancementListener();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        // Properly Shutdown JDA connection
        if (DiscordJDAConnection.getJda() != null) {
            ServerEvents.serverShutdown();
            DiscordJDAConnection.getJda().shutdownNow();
        }
    }

    public void disableSelf() {
        Bukkit.getPluginManager().disablePlugin(this);
    }

    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }
}
