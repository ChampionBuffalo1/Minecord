package com.champ.minecord;

import com.champ.minecord.utility.ConfigDefaults;
import com.champ.minecord.utility.PluginLogger;
import net.dv8tion.jda.api.entities.Webhook;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Optional;

public class Settings {
    private final static FileConfiguration config = Minecord.getPlugin().getConfig();

    public static Optional<String> getBotToken() {
        String token = config.getString("token");
        if (commonErrors(token) || token.equalsIgnoreCase(ConfigDefaults.TOKEN.getDefault())) {
            PluginLogger.unrecoverable("Bot Token not found in config.yml, disabling plugin");
            return Optional.empty();
        }
        return Optional.of(token);
    }

    public static Optional<String> getChannelId() {
        String id = config.getString("channelId");
        if (commonErrors(id) || id.equalsIgnoreCase(ConfigDefaults.CHANNEL_ID.getDefault()))
            return Optional.empty();
        return Optional.of(id);
    }

    public static Optional<String> getGuildId() {
        String id = config.getString("guildId");
        if (commonErrors(id) || id.equalsIgnoreCase(ConfigDefaults.GUILD_ID.getDefault()))
            return Optional.empty();
        return Optional.of(id);
    }

    public static Optional<String> getEmote(String path) {
        String emote = config.getString(path);
        if (commonErrors(emote) || !emote.matches("<a?:\\w+:\\d{15,22}>"))
            return Optional.empty();
        return Optional.of(emote);
    }

    public static boolean getShowIp() {
        return config.getBoolean("showIp");
    }

    public static boolean useWebhook() {
        return config.getBoolean("useWebhook");
    }

    public static Optional<String> getWebhookURL() {
        String webhook = config.getString("webhookURL");
        if (commonErrors(webhook) || !Webhook.WEBHOOK_URL.matcher(webhook).matches())
            return Optional.empty();
        return Optional.of(webhook);
    }

    public static String getPlayerAvatarURL(String uuid) {
        return String.format("https://crafthead.net/helm/%s", uuid);
    }

    private static boolean commonErrors(String toCheck) {
        return toCheck == null || toCheck.length() == 0;
    }
}
