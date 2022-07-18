package com.champ.minecord.listeners;

import com.champ.minecord.Minecord;
import com.champ.minecord.Settings;
import com.champ.minecord.discord.DiscordJDAConnection;
import com.champ.minecord.utility.ConfigDefaults;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

public class ServerEvents implements Listener {
    private static TextChannel channel;

    public ServerEvents() {
        Minecord.getPlugin().registerListener(this);
        channel = DiscordJDAConnection.getTextChannel();
    }

    public static void serverShutdown() {
        if (channel != null) {
            String stopEmote = Settings.getEmote("emojis.server.stop").orElse(ConfigDefaults.SERVER_STOP_EMOJI.getDefault());
            channel.sendMessage(stopEmote).append(" The server has stopped!").complete();
        }
    }

    @EventHandler
    public void onServerStart(ServerLoadEvent server) {
        if (server.getType() == ServerLoadEvent.LoadType.STARTUP) {
            String startEmote = Settings.getEmote("emojis.server.start").orElse(ConfigDefaults.SERVER_START_EMOJI.getDefault());
            channel.sendMessage(startEmote).append(" The server has started!").queue();
        } else {
            // Server Reload
            String reloadEmote = Settings.getEmote("emojis.server.reload").orElse(ConfigDefaults.SERVER_RELOAD_EMOJI.getDefault());
            channel.sendMessage(reloadEmote).append(" The server has reloaded!").queue();
        }
    }
}
