package com.champ.minecord;

import org.bukkit.plugin.java.JavaPlugin;

public final class Minecord extends JavaPlugin {
    private static Minecord plugin;
    @Override
    public void onEnable() {
        plugin = this;
        getConfig().options().copyDefaults();
        saveDefaultConfig();
    }

    public static Minecord getPlugin() {
        return plugin;
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
