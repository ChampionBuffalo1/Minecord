package com.champ.minecord.discord;

import com.champ.minecord.Minecord;
import com.champ.minecord.utility.ConfigDefaults;
import com.champ.minecord.utility.PluginLogger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;

public class DiscordJDAConnection {
    private static JDA jda = null;
    private static Guild guild = null;
    private static TextChannel textChannel = null;

    public static void InitiateConnection(Minecord plugin) {
        String token = plugin.getConfig().getString("token");

        if (token == null || token.equalsIgnoreCase(ConfigDefaults.TOKEN.getDefault())) {
            PluginLogger.unrecoverable("Bot Token not found in config.yml, disabling plugin");
            return; // Maybe redundant?
        }
        String guildId = plugin.getConfig().getString("guildId");
        try {
            DiscordJDAConnection.jda = JDABuilder.create(token,
                            GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
                            GatewayIntent.GUILD_MESSAGES,
                            GatewayIntent.GUILD_MEMBERS
                    )
                    .setMemberCachePolicy(member -> member.getGuild().getId().equals(guildId))
                    .disableCache(CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE,
                            CacheFlag.CLIENT_STATUS, CacheFlag.ONLINE_STATUS)
                    .setActivity(Activity.playing("Minecraft" + (plugin.getConfig().getBoolean("showIp") ? " at " + getHostIp() : "")))
                    .build()
                    .awaitReady();

            String channelId = plugin.getConfig().getString("channelId");
            textChannel = jda.getTextChannelById(channelId);
            if (textChannel == null)
                PluginLogger.unrecoverable("TextChannel not found using the id provided in config.yml, disabling plugin");

            guild = jda.getGuildById(guildId);
            if (guild == null)
                PluginLogger.unrecoverable("Guild Id not found in config.yml which is used for caching");
            else
                EntityCache.Setup(guild);
        } catch (LoginException except) {
            PluginLogger.unrecoverable("Exception encountered during login: " + except.getMessage());
        } catch (InterruptedException except) {
            PluginLogger.unrecoverable("Interrupt encountered during bot login: " + except.getMessage());
        }
        jda.addEventListener(new MessageListener());
    }

    private static String getHostIp() {
        String ip = "localhost";
        if (org.bukkit.Bukkit.getIp().length() != 0)
            ip = org.bukkit.Bukkit.getIp();
        return ip;
    }

    public static JDA getJda() {
        return jda;
    }

    public static TextChannel getTextChannel() {
        return textChannel;
    }

    public static Guild getGuild() {
        return guild;
    }
}
