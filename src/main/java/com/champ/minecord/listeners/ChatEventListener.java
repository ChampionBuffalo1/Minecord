package com.champ.minecord.listeners;

import com.champ.minecord.Minecord;
import com.champ.minecord.discord.JdaConnection;
import com.champ.minecord.utility.MinecraftChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEventListener implements Listener {
    public ChatEventListener() {
        Minecord.getPlugin().registerListener(this);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChatMessage(AsyncPlayerChatEvent event) {
        Player plyr = event.getPlayer();
        String message = event.getMessage();
        StringBuilder builder = new StringBuilder(ChatColor.stripColor(plyr.getDisplayName()))
                .append(": ")
                .append(message);
        // Just for a test run
        JdaConnection.sendMessage(MinecraftChatUtils.inject(builder), event.getPlayer());
    }
}
