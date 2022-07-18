package com.champ.minecord.listeners;

import com.champ.minecord.Minecord;
import com.champ.minecord.Settings;
import com.champ.minecord.discord.DiscordJDAConnection;
import com.champ.minecord.utility.ConfigDefaults;
import io.papermc.paper.advancement.AdvancementDisplay;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.advancement.Advancement;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

/**
 * This class might be a problem for backwards compatibility
 * because the AdvancementDisplay class's usage is specific to Paper's API
 * and will probably not work for spigot servers
 */
public class AdvancementListener implements Listener {
    public AdvancementListener() {
        Minecord.getPlugin().registerListener(this);
    }

    @EventHandler
    public void onPlayerAdvancement(PlayerAdvancementDoneEvent event) {
        Advancement advancement = event.getAdvancement();
        AdvancementDisplay display = advancement.getDisplay();
        if (display == null) return;
        String title = PlainTextComponentSerializer.plainText().serialize(display.title());
        String name = ChatColor.stripColor(event.getPlayer().getDisplayName());
        String emote = Settings.getEmote("emojis.advancement").orElse(ConfigDefaults.ADVANCEMENT_EMOJI.getDefault());
        String builder = emote +
                " " +
                name +
                "has completed the advancement **" +
                title + "**!";
        DiscordJDAConnection.sendMessage(builder, event.getPlayer());
    }
}
