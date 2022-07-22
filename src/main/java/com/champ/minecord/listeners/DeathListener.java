package com.champ.minecord.listeners;

import com.champ.minecord.Minecord;
import com.champ.minecord.Settings;
import com.champ.minecord.discord.JdaConnection;
import com.champ.minecord.utility.ConfigDefaults;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {
    public DeathListener() {
        Minecord.getPlugin().registerListener(this);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        String message = event.getDeathMessage();
        if (message == null) return;
        String emote = Settings.getEmote("emojis.death")
                .orElse(ConfigDefaults.DEATH_EMOJI.getDefault());
        String builder = emote +
                " " +
                message;
        JdaConnection.sendMessage(builder, event.getPlayer());
    }
}
