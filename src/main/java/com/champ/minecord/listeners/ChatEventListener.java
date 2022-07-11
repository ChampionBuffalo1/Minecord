package com.champ.minecord.listeners;

import com.champ.minecord.Minecord;
import com.champ.minecord.discord.DiscordJDAConnection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEventListener implements Listener {
    public ChatEventListener() {
        Bukkit.getPluginManager().registerEvents(this, Minecord.getPlugin());
    }

    @EventHandler
    public void onChatMessage(AsyncPlayerChatEvent event) {
        Player plyr = event.getPlayer();
        String message = event.getMessage();
        StringBuilder builder = new StringBuilder(ChatColor.stripColor(plyr.getDisplayName()))
                .append(": ")
                .append(message);
        // Just for a test run
        Bukkit.getScheduler().runTaskAsynchronously(Minecord.getPlugin(),
                () -> DiscordJDAConnection.getTextChannel().sendMessage(builder).queue()
        );
    }
}
