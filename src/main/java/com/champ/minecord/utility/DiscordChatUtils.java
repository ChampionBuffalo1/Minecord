package com.champ.minecord.utility;

import com.champ.minecord.discord.DiscordJDAConnection;
import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiscordChatUtils {
    private final static Pattern emotePattern = Pattern.compile("<(:\\w+:)(\\d{15,22})>");
    private final static Pattern mentionPattern = Pattern.compile("<@!?(\\d{15,22})>");
    private final static Pattern roleMentionPattern = Pattern.compile("<@&(\\d{15,22})>");
    private final static Pattern channelMention = Pattern.compile("<#(\\d{15,22})>");

    /**
     * Removes discord specific things from message
     *
     * @param discordMessage Input String to clean
     * @return Properly "cleaned" message string
     */
    public static String cleanMessage(String discordMessage) {
        String withoutEmotes = removeEmotes(discordMessage);
        return removeMentions(withoutEmotes);
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
        String id = match.group(1);
        String toReplaceWith = "@invalid-user";
        Member member = DiscordJDAConnection.getGuild().getMemberById(id);
        if (member != null)
            toReplaceWith = "@" + member.getUser().getName();
        return match.replaceAll(toReplaceWith);
    }

    public static String removeRole(String input) {
        Matcher match = roleMentionPattern.matcher(input);
        if (!match.find()) return input;
        String id = match.group(1);
        String toReplaceWith = "deleted-role";
        Role role = DiscordJDAConnection.getGuild().getRoleById(id);
        if (role != null)
            toReplaceWith = "@" + role.getName();
        return match.replaceAll(toReplaceWith);
    }

    public static String removeChannel(String input) {
        Matcher match = channelMention.matcher(input);
        if (!match.find()) return input;
        String id = match.group(1);
        String toReplaceWith = "#deleted-channel";
        Channel channel = DiscordJDAConnection.getGuild().getChannelById(Channel.class, id);
        if (channel != null)
            toReplaceWith = "#" + channel.getName();
        return match.replaceAll(toReplaceWith);
    }

    /**
     * StringBuilder overload for cleanMessage method
     *
     * @param messageBuilder
     * @return Properly "cleaned" message string
     */
    public static String cleanMessage(StringBuilder messageBuilder) {
        return cleanMessage(messageBuilder.toString());
    }
}
