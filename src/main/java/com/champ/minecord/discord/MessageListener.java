package com.champ.minecord.discord;

import com.champ.minecord.Settings;
import com.champ.minecord.utility.DiscordChatUtils;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class MessageListener extends ListenerAdapter {
    private final static String channelId = Settings.getChannelId().orElseThrow();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.isFromType(ChannelType.TEXT) || event.getAuthor().isBot() ||
                !event.getChannel().getId().equals(channelId)
        )
            return;
        String authorTag = event.getAuthor().getAsTag();
        String message = DiscordChatUtils.cleanMessage(event.getMessage().getContentRaw());
        String str = ChatColor.GOLD + "[#" +
                event.getChannel().getName() +
                "] " + ChatColor.DARK_AQUA +
                authorTag +
                ChatColor.GRAY + ": " + ChatColor.WHITE + message;
        if (str.length() >= 256) // 256 is the server chat's character limit
            str = str.substring(0, 256);

        Bukkit.getServer().broadcastMessage(str);
    }
}
