package com.champ.minecord.listeners;

import com.champ.minecord.Minecord;
import com.champ.minecord.discord.JdaConnection;
import com.champ.minecord.utility.MinecraftChatUtils;
import com.champ.minecord.utility.Utils;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ChatEventListener implements Listener {
    public ChatEventListener() {
        Minecord.getPlugin().registerListener(this);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChatMessage(AsyncChatEvent event) {
        Player plyr = event.getPlayer();
        String message = Utils.toPlainText(event.message());
        String builder = Utils.toPlainText(plyr.displayName()) +
                ": " +
                message;
        JdaConnection.sendMessage(MinecraftChatUtils.inject(builder), event.getPlayer());
    }
}
