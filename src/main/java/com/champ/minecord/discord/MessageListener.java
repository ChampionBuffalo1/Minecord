package com.champ.minecord.discord;

import com.champ.minecord.Minecord;
import com.champ.minecord.utility.DiscordChatUtils;
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
        String message = DiscordChatUtils.cleanMessage(event.getMessage().getContentRaw());
        StringBuilder builder = new StringBuilder(ChatColor.BLUE + "[#")
                .append(event.getChannel().getName())
                .append("] ")
                .append(authorTag)
                .append(": " + ChatColor.WHITE).append(message);
        String str = builder.toString();
        if (str.length() >= 256) // 256 is the server chat's character limit
            str = str.substring(0, 256);

        Bukkit.getServer().broadcastMessage(str);
    }
}
