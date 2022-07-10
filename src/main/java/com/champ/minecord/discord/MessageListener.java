package com.champ.minecord.discord;

import com.champ.minecord.Minecord;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.isFromType(ChannelType.TEXT) || event.getAuthor().isBot() ||
                !event.getChannel().getId()
                .equals(Minecord.getPlugin()
                .getConfig().getString("channelId"))
            )
            return;
        String authorTag = event.getAuthor().getAsTag();
        String message = event.getMessage().getContentRaw();
        StringBuilder builder = new StringBuilder(ChatColor.BLUE + authorTag)
                .append(": " + ChatColor.WHITE).append(message);
        Bukkit.getServer().broadcastMessage(builder.toString());
    }
}
