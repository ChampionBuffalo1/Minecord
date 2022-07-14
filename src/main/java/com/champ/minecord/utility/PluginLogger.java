package com.champ.minecord.utility;

import com.champ.minecord.Minecord;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PluginLogger {
    private final static Minecord plugin = Minecord.getPlugin();
    private final static Logger pluginLogger = plugin.getLogger();

    public static void info(String message) {
        pluginLogger.info(message);
    }
    public static void warn(String message) {
        log(Level.WARNING, message);
    }
    public static void severe(String message) {
        log(Level.SEVERE, message);
    }
    public static void unrecoverable(String message) {
        log(Level.SEVERE, message);
        plugin.disableSelf();
    }
    public static void log(Level level, String message) {
        pluginLogger.log(level, message);
    }
}
