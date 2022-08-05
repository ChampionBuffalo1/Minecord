package com.champ.minecord.discord;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.champ.minecord.Settings;
import com.champ.minecord.utility.PluginLogger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.security.auth.login.LoginException;

public class JdaConnection {
    private static JDA jda = null;
    private static Guild guild = null;
    private static TextChannel textChannel = null;
    private static WebhookClient webhook = null;

    public static void InitiateConnection() {
        String token = Settings.getBotToken().orElseThrow();
        String guildId = Settings.getGuildId().orElseThrow(IllegalArgumentException::new);
        try {
            JdaConnection.jda = JDABuilder.create(token,
                            GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
                            GatewayIntent.GUILD_MESSAGES,
                            GatewayIntent.GUILD_MEMBERS
                    )
                    .setMemberCachePolicy(member -> member.getGuild().getId().equals(guildId))
                    // Disabled all cache except sticker and emoji because they're required by JDA for update events
                    .disableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS,
                            CacheFlag.MEMBER_OVERRIDES, CacheFlag.ONLINE_STATUS,
                            CacheFlag.ROLE_TAGS, CacheFlag.VOICE_STATE
                    )
                    .setActivity(Activity.playing("Minecraft" + (Settings.getShowIp() ? " at " + getHostIp() : "")))
                    .addEventListeners(new DiscordListener())
                    .setContextEnabled(false)
                    .build()
                    .awaitReady();

            String channelId = Settings.getChannelId().orElseThrow(IllegalArgumentException::new);
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
        if (Settings.useWebhook()) {
            WebhookClientBuilder wb = new WebhookClientBuilder(Settings.getWebhookURL().orElseThrow(IllegalArgumentException::new));
            wb.setHttpClient(jda.getHttpClient());
            wb.setWait(false); // We don't care about the message returned
            webhook = wb.buildJDA();
        }
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

    public static void sendWebhookMessage(String msg, Player player) {
        WebhookMessageBuilder message = new WebhookMessageBuilder()
                .setContent(msg)
                .setUsername(player.getName())
                .setAvatarUrl(Settings.getPlayerAvatarURL(player.getUniqueId().toString()));
        webhook.send(message.build());
    }

    public static void sendMessage(String message, Player player) {
        // Stop trying to send messages if guild becomes unavailable
        if (DiscordListener.isStopMessages()) return;
        message = ChatColor.stripColor(message);
        if (Settings.useWebhook())
            sendWebhookMessage(message, player);
        else
            textChannel.sendMessage(message).queue();
    }
}
