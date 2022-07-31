package com.champ.minecord.discord;

import com.champ.minecord.Settings;
import com.champ.minecord.utility.DiscordChatUtils;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.emoji.EmojiAddedEvent;
import net.dv8tion.jda.api.events.emoji.EmojiRemovedEvent;
import net.dv8tion.jda.api.events.emoji.update.EmojiUpdateNameEvent;
import net.dv8tion.jda.api.events.guild.GuildAvailableEvent;
import net.dv8tion.jda.api.events.guild.GuildUnavailableEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.sticker.GuildStickerAddedEvent;
import net.dv8tion.jda.api.events.sticker.GuildStickerRemovedEvent;
import net.dv8tion.jda.api.events.sticker.update.GuildStickerUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class DiscordListener extends ListenerAdapter {
    private final static String channelId = Settings.getChannelId().orElseThrow();
    private static boolean stopMessages = false;

    public static boolean isStopMessages() {
        return stopMessages;
    }

    public static void setStopMessages(boolean stopMessages) {
        DiscordListener.stopMessages = stopMessages;
    }

    private static boolean isFromGuild(String guildId) {
        return guildId.equals(Settings.getGuildId().orElseThrow());
    }

    @Override
    public void onGuildUnavailable(GuildUnavailableEvent event) {
        if (!isFromGuild(event.getGuild().getId())) return;
        broadcast(
                ChatColor.GOLD + "[" + ChatColor.DARK_RED + "Server" + ChatColor.RESET + ChatColor.GOLD + "]: "
                        + "Discord Server became unavailable. Messages won't be received or sent to the channel anymore"
        );
        DiscordListener.setStopMessages(true);
    }

    @Override
    public void onGuildAvailable(GuildAvailableEvent event) {
        if (!isFromGuild(event.getGuild().getId())) return;
        broadcast(
                ChatColor.GOLD + "[" + ChatColor.RED + "Server" + ChatColor.RESET + ChatColor.GOLD + "]: " +
                        "Discord server is now available and the message interchange can continue as before."
        );
        DiscordListener.setStopMessages(false);
    }

    // Emojis
    @Override
    public void onEmojiAdded(EmojiAddedEvent event) {
        if (!isFromGuild(event.getGuild().getId())) return;
        EntityCache.updateEmote(event.getEmoji().getName(), event.getEmoji().getFormatted());
    }

    @Override
    public void onEmojiRemoved(EmojiRemovedEvent event) {
        if (!isFromGuild(event.getGuild().getId())) return;
        EntityCache.removeEmote(event.getEmoji().getName());
    }

    @Override
    public void onEmojiUpdateName(EmojiUpdateNameEvent event) {
        if (!isFromGuild(event.getGuild().getId())) return;
        EntityCache.removeEmote(event.getOldName());
        EntityCache.updateEmote(event.getNewName(), event.getEmoji().getFormatted());
    }

    // Stickers
    @Override
    public void onGuildStickerAdded(GuildStickerAddedEvent event) {
        if (!isFromGuild(event.getSticker().getGuildId())) return;
        EntityCache.updateSticker(event.getSticker().getName(), event.getSticker().getId());
    }

    @Override
    public void onGuildStickerRemoved(GuildStickerRemovedEvent event) {
        if (!isFromGuild(event.getSticker().getGuildId())) return;
        EntityCache.removeSticker(event.getSticker().getName());
    }

    @Override
    public void onGuildStickerUpdateName(GuildStickerUpdateNameEvent event) {
        if (!isFromGuild(event.getSticker().getGuildId())) return;
        String stickerId = event.getSticker().getId();
        EntityCache.removeStickerById(stickerId);
        EntityCache.updateSticker(event.getSticker().getName(), stickerId);
    }

    // Message
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.isFromType(ChannelType.TEXT) || event.getAuthor().isBot() ||
                !event.getChannel().getId().equals(channelId)
        )
            return;
        String authorTag = event.getAuthor().getAsTag();
        String message = DiscordChatUtils.cleanMessage(event.getMessage());
        String str = ChatColor.GOLD + "[#" +
                event.getChannel().getName() +
                "] " + ChatColor.DARK_AQUA +
                authorTag +
                ChatColor.GRAY + ": " + ChatColor.WHITE + message;
        if (str.length() >= DiscordChatUtils.maxMCMessageLength + 1) // 256 is the server chat's character limit
            str = str.substring(0, DiscordChatUtils.maxMCMessageLength + 1);
        broadcast(str);
    }

    private void broadcast(String message) {
        Bukkit.getServer().broadcastMessage(message);
    }
}
