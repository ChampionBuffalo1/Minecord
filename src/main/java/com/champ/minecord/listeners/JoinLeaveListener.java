package com.champ.minecord.listeners;

import com.champ.minecord.Minecord;
import com.champ.minecord.discord.DiscordJDAConnection;
import com.champ.minecord.utility.ConfigDefaults;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveListener implements Listener {

    public JoinLeaveListener() {
        Bukkit.getPluginManager().registerEvents(this, Minecord.getPlugin());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        TextChannel channel = DiscordJDAConnection.getTextChannel();
        String playerName = ChatColor.stripColor(event.getPlayer().getDisplayName());

        String emoji = Minecord.getPlugin().getConfig().getString("emojis.join");
        if (emoji == null || emoji.length() == 0)
            emoji = ConfigDefaults.JOIN_EMOJI.getDefault();
        String finalEmoji = emoji;
        Bukkit.getScheduler().runTaskAsynchronously(Minecord.getPlugin(), () ->
                channel.sendMessage(finalEmoji).append(" ")
                        .append(playerName)
                        .append(" has joined!")
                        .queue()
        );
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        TextChannel channel = DiscordJDAConnection.getTextChannel();
        String playerName = ChatColor.stripColor(event.getPlayer().getDisplayName());

        String emoji = Minecord.getPlugin().getConfig().getString("emojis.leave");
        if (emoji == null || emoji.length() == 0)
            emoji = ConfigDefaults.LEAVE_EMOJI.getDefault();
        String finalEmoji = emoji;
        Bukkit.getScheduler().runTaskAsynchronously(Minecord.getPlugin(), () ->
                channel.sendMessage(finalEmoji).append(" ")
                        .append(playerName)
                        .append(" has left!")
                        .queue()
        );
    }
}
