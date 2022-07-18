package com.champ.minecord;

import com.champ.minecord.utility.ConfigDefaults;
import com.champ.minecord.utility.PluginLogger;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Optional;

public class Settings {
    private final static FileConfiguration config = Minecord.getPlugin().getConfig();

    public static Optional<String> getBotToken() {
        String token = config.getString("token");
        if (token == null || token.length() == 0 || token.equalsIgnoreCase(ConfigDefaults.TOKEN.getDefault())) {
            PluginLogger.unrecoverable("Bot Token not found in config.yml, disabling plugin");
            return Optional.ofNullable(null);
        }
        return Optional.ofNullable(token);
    }

    public static Optional<String> getChannelId() {
        String id = config.getString("channelId");
        if (id == null || id.length() == 0 || id.equalsIgnoreCase(ConfigDefaults.CHANNEL_ID.getDefault()))
            return Optional.ofNullable(null);
        return Optional.ofNullable(id);
    }

    public static Optional<String> getGuildId() {
        String id = config.getString("guildId");
        if (id == null || id.length() == 0 || id.equalsIgnoreCase(ConfigDefaults.GUILD_ID.getDefault()))
            return Optional.ofNullable(null);
        return Optional.ofNullable(id);
    }

    public static Optional<String> getEmote(String path) {
        String emote = config.getString(path);
        if (emote == null || emote.length() == 0 || !emote.matches("<:\\w+:\\d{15,22}>"))
            return Optional.ofNullable(null);
        return Optional.ofNullable(emote);
    }

    public static boolean getShowIp() {
        return config.getBoolean("showIp");
    }

    public static boolean useWebhook() {
        return config.getBoolean("useWebhook");
    }

    public static Optional<String> getWebhookURL() {
        return Optional.ofNullable(config.getString("webhookURL"));
    }

    public static String getPlayerAvatarURL(String uuid) {
        return String.format("https://crafthead.net/helm/{uuid}", uuid);
    }
}
