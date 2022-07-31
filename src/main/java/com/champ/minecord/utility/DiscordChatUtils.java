package com.champ.minecord.utility;

import com.champ.minecord.discord.JdaConnection;
import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.sticker.StickerItem;
import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiscordChatUtils {
    // 256 (exclusive) is the max number of characters allowed in minecraft chat
    public final static short maxMCMessageLength = 256 - 1;
    private final static Pattern emotePattern = Pattern.compile("<a?(:\\w+:)(\\d{15,22})>");
    private final static Pattern mentionPattern = Pattern.compile("<@!?(\\d{15,22})>");
    private final static Pattern roleMentionPattern = Pattern.compile("<@&(\\d{15,22})>");
    private final static Pattern channelMention = Pattern.compile("<#(\\d{15,22})>");

    /**
     * Removes discord specific things from message
     *
     * @param message Message to clean
     * @return Properly "cleaned" message string
     */
    public static String cleanMessage(Message message) {
        String rawMessage = message.getContentRaw();
        String withoutEmotes = removeEmotes(rawMessage);
        String afterCleaning = removeMentions(withoutEmotes);
        if (afterCleaning.length() < maxMCMessageLength)
            return tryAddOtherMeta(afterCleaning, message);
        return afterCleaning;
    }

    public static String tryAddOtherMeta(String processedMessage, Message msg) {
        StringBuilder builder = new StringBuilder(processedMessage);
        for (Message.Attachment attachment : msg.getAttachments()) {
            String toAppend = "\n" + ChatColor.ITALIC + "[Attachment]: " + attachment.getUrl();
            if (toAppend.length() + builder.length() >= maxMCMessageLength)
                return builder.toString();
            builder.append(toAppend);
        }
        for (StickerItem sticker : msg.getStickers()) {
            String toAppend = "\n" + ChatColor.ITALIC + "[Sticker]: " + sticker.getName();
            if (toAppend.length() + builder.length() >= maxMCMessageLength)
                return builder.toString();
            builder.append(toAppend);
        }
        return builder.toString();
    }

    /**
     * Removes irrelevant discord emote ids with just their name i.e, in format :{emote_name}:
     *
     * @param input Input string
     * @return Properly "cleaned" message string
     */
    public static String removeEmotes(String input) {
        Matcher match = emotePattern.matcher(input);
        if (!match.find()) return input;
        return match.replaceAll("$1"); // $1 is the first group of the regex
    }

    /**
     * @param input Input String
     * @return Properly "cleaned" message string
     */
    public static String removeMentions(String input) {
        String withoutChannelMention = removeChannel(input);
        String withoutRoleMention = removeRole(withoutChannelMention);
        Matcher match = mentionPattern.matcher(withoutRoleMention);
        if (!match.find()) return withoutRoleMention;
        return match.replaceAll(matchResult -> {
            String id = matchResult.group(1);
            String toReplaceWith = "@invalid-user";
            Member member = JdaConnection.getGuild().getMemberById(id);
            if (member != null)
                toReplaceWith = "@" + member.getUser().getName();
            return toReplaceWith;
        });
    }

    public static String removeRole(String input) {
        Matcher match = roleMentionPattern.matcher(input);
        if (!match.matches()) return input;
        return match.replaceAll(matchResult -> {
            String id = matchResult.group(1);
            String toReplaceWith = "deleted-role";
            Role role = JdaConnection.getGuild().getRoleById(id);
            if (role != null)
                toReplaceWith = "@" + role.getName();
            return toReplaceWith;
        });
    }

    public static String removeChannel(String input) {
        Matcher match = channelMention.matcher(input);
        if (!match.find()) return input;
        return match.replaceAll(matchResult -> {
            String id = matchResult.group(1);
            String toReplaceWith = "#deleted-channel";
            Channel channel = JdaConnection.getGuild().getChannelById(Channel.class, id);
            if (channel != null)
                toReplaceWith = "#" + channel.getName();
            return toReplaceWith;
        });
    }
}
