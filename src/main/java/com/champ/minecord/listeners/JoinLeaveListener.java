package com.champ.minecord.listeners;

import com.champ.minecord.Minecord;
import com.champ.minecord.Settings;
import com.champ.minecord.discord.DiscordJDAConnection;
import com.champ.minecord.utility.ConfigDefaults;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveListener implements Listener {

    public JoinLeaveListener() {
        Minecord.getPlugin().registerListener(this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerName = ChatColor.stripColor(event.getPlayer().getDisplayName());

        String emoji = Settings.getEmote("emojis.join").orElse(ConfigDefaults.JOIN_EMOJI.getDefault());
        String message = emoji + " " +
                playerName +
                " has joined!";
        DiscordJDAConnection.sendMessage(message, event.getPlayer());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        String playerName = ChatColor.stripColor(event.getPlayer().getDisplayName());

        String emoji = Settings.getEmote("emojis.leave").orElse(ConfigDefaults.LEAVE_EMOJI.getDefault());
        String message = emoji + " " +
                playerName +
                " has left!";
        DiscordJDAConnection.sendMessage(message, event.getPlayer());
    }
}
