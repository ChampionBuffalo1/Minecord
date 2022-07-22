package com.champ.minecord;

import com.champ.minecord.commands.StickerCommand;
import com.champ.minecord.discord.JdaConnection;
import com.champ.minecord.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
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
        JdaConnection.InitiateConnection();
        // Listeners
        new ServerEvents();
        new DeathListener();
        new ChatEventListener();
        new JoinLeaveListener();
        new AdvancementListener();
        // Commands
        new StickerCommand();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        // Properly Shutdown JDA connection
        if (JdaConnection.getJda() != null) {
            ServerEvents.serverShutdown();
            JdaConnection.getJda().shutdownNow();
        }
    }

    public void disableSelf() {
        Bukkit.getPluginManager().disablePlugin(this);
    }

    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    public void registerCommand(String commandName, CommandExecutor command) {
        getCommand(commandName).setExecutor(command);
    }
}
