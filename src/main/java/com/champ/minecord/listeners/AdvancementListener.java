package com.champ.minecord.listeners;

import com.champ.minecord.Minecord;
import com.champ.minecord.Settings;
import com.champ.minecord.discord.JdaConnection;
import com.champ.minecord.utility.ConfigDefaults;
import com.champ.minecord.utility.Utils;
import io.papermc.paper.advancement.AdvancementDisplay;
import org.bukkit.advancement.Advancement;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class AdvancementListener implements Listener {
    public AdvancementListener() {
        Minecord.getPlugin().registerListener(this);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerAdvancement(PlayerAdvancementDoneEvent event) {
        Advancement advancement = event.getAdvancement();
        AdvancementDisplay display = advancement.getDisplay();
        if (display == null) return;
        String title = Utils.toPlainText(display.title());
        String name = Utils.toPlainText(event.getPlayer().displayName());
        String emote = Settings.getEmote("emojis.advancement").orElse(ConfigDefaults.ADVANCEMENT_EMOJI.getDefault());
        String builder = emote +
                " " +
                name +
                " has completed the advancement **" +
                title + "**!";
        JdaConnection.sendMessage(builder, event.getPlayer());
    }
}
