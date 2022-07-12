package com.champ.minecord.discord;

import com.champ.minecord.Minecord;
import com.champ.minecord.utility.ConfigDefaults;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.bukkit.Bukkit;

import javax.security.auth.login.LoginException;
import java.util.logging.Level;

public class DiscordJDAConnection {
    private static JDA jda = null;
    private static Guild guild = null;
    private static TextChannel textChannel = null;

    public static void InitiateConnection(Minecord plugin) {
        String token = plugin.getConfig().getString("token");

        if (token == null || token.equalsIgnoreCase(ConfigDefaults.TOKEN.getDefault())) {
            Bukkit.getLogger().log(Level.SEVERE, "Bot Token not found in config.yml, disabling plugin");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return; // Maybe redundant?
        }
        String guildId = plugin.getConfig().getString("guildId");
        try {
            DiscordJDAConnection.jda = JDABuilder.create(token,
                            GatewayIntent.GUILD_EMOJIS_AND_STICKERS,
                            GatewayIntent.GUILD_MESSAGES,
                            GatewayIntent.GUILD_MEMBERS
                    )
                    .setChunkingFilter(ChunkingFilter.NONE)
                    .setMemberCachePolicy(member -> member.getGuild().getId().equals(guildId))
                    .enableCache(CacheFlag.EMOJI, CacheFlag.STICKER)
                    .disableCache(CacheFlag.ACTIVITY, CacheFlag.VOICE_STATE, 
                                  CacheFlag.CLIENT_STATUS, CacheFlag.ONLINE_STATUS)
                    .setActivity(Activity.playing("Minecraft" + (plugin.getConfig().getBoolean("showIp") ? " at " + getHostIp() : "")))
                    .build()
                    .awaitReady();

            String channelId = plugin.getConfig().getString("channelId");
            textChannel = jda.getTextChannelById(channelId);
            if (textChannel == null) {
                Bukkit.getLogger().log(Level.SEVERE, "TextChannel not found using the id provided in config.yml, disabling plugin");
                Bukkit.getPluginManager().disablePlugin(Minecord.getPlugin());
                return;
            }
            guild = jda.getGuildById(guildId);
            if (guild == null) {
                Bukkit.getLogger().log(Level.SEVERE, "Guild Id not found in config.yml which is used for caching");
                Bukkit.getPluginManager().disablePlugin(Minecord.getPlugin());
            } else {
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
//                    DiscordJDAConnection.guild.retrieveEmojis();
//                    DiscordJDAConnection.guild.retrieveStickers();
                });
            }
        } catch (LoginException except) {
            Bukkit.getLogger().log(Level.SEVERE, "Exception encountered during login: " + except.getMessage());
            Bukkit.getPluginManager().disablePlugin(Minecord.getPlugin());
        } catch (InterruptedException except) {
            Bukkit.getLogger().log(Level.SEVERE, "Interrupt encountered during bot login: " + except.getMessage());
            Bukkit.getPluginManager().disablePlugin(Minecord.getPlugin());
        }
        jda.addEventListener(new MessageListener());
    }

    private static String getHostIp() {
        String ip = "localhost";
        if (Bukkit.getIp().length() != 0)
            ip = Bukkit.getIp();
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
