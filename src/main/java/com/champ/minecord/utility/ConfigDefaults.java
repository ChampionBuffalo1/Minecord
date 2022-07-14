package com.champ.minecord.utility;

public enum ConfigDefaults {
    TOKEN("BotToken"),
    // CHANNEL_ID("ChannelId"),
    // GUILD_ID("GuildId"),
    JOIN_EMOJI(":arrow_right:"),
    LEAVE_EMOJI(":arrow_left:"),
    DEATH_EMOJI(":skull:"),
    SERVER_START_EMOJI(":white_check_mark:"),
    SERVER_RELOAD_EMOJI(":arrows_clockwise:"),
    SERVER_STOP_EMOJI(":octagonal_sign:");

    private final String defaultValue;

    ConfigDefaults(String value) {
        this.defaultValue = value;
    }

    public String getDefault() {
        return defaultValue;
    }
}
