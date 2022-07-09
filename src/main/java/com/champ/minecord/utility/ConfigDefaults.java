package com.champ.minecord.utility;

public enum ConfigDefaults {
    TOKEN("BotToken"),
    CHANNEL_ID("ChannelId"),
    GUILD_ID("GuildId");

    private final String defaultValue;
    ConfigDefaults(String value) {
        this.defaultValue = value;
    }

    public String getDefault() {
        return defaultValue;
    }
}
