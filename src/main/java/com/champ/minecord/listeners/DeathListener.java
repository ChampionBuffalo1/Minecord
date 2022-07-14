package com.champ.minecord.listeners;

import com.champ.minecord.Minecord;
import com.champ.minecord.discord.DiscordJDAConnection;
import com.champ.minecord.utility.ConfigDefaults;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {
    public DeathListener() {
        Minecord.getPlugin().registerListener(this);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        String message = event.getDeathMessage();
        if (message == null) return;
        String emoji = Minecord.getPlugin().getConfig().getString("emojis.death");
        if (emoji == null || emoji.length() == 0)
            emoji = ConfigDefaults.DEATH_EMOJI.getDefault();
        String finalEmoji = emoji;
        Bukkit.getScheduler().runTaskAsynchronously(Minecord.getPlugin(), () ->
                DiscordJDAConnection.getTextChannel()
                        .sendMessage(finalEmoji)
                        .append(" ")
                        .append(message)
                        .queue()
        );
    }
}
