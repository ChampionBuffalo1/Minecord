package com.champ.minecord.utility;

import com.champ.minecord.discord.DiscordJDAConnection;
import com.champ.minecord.discord.EntityCache;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.utils.cache.MemberCacheView;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MinecraftChatUtils {
    private final static Pattern simpleEmote = Pattern.compile(":(\\w+):");
    private final static Pattern simpleChannel = Pattern.compile("#([a-z\\d-]+)");
    private final static Pattern simpleMember = Pattern.compile("@([\\w\\d-]+(#\\d{4})?)");

    public static String inject(StringBuilder builder) {
        return inject(builder.toString());
    }

    public static String inject(String inputString) {
        String withEmotes = injectEmotes(inputString);
        String withChannelMention = injectChannelMentions(withEmotes);
        return injectMemberMentions(withChannelMention);
    }

    public static String injectEmotes(String inputString) {
        Matcher match = simpleEmote.matcher(inputString);
        if (!match.find()) return inputString;
        return match.replaceAll(matchResult -> {
            String key = matchResult.group(1);
            return EntityCache.getEmoteOrElse(key, matchResult.group());
        });
    }

    public static String injectChannelMentions(String inputString) {
        Matcher match = simpleChannel.matcher(inputString);
        if (!match.find()) return inputString;
        return match.replaceAll(matchResult -> {
            String name = matchResult.group(1);
            List<TextChannel> channels = DiscordJDAConnection.getGuild().getTextChannelsByName(name, true);
            if (channels.isEmpty()) return matchResult.group();
            return channels.get(0).getAsMention();
        });
    }

    public static String injectMemberMentions(String inputString) {
        Matcher match = simpleMember.matcher(inputString);
        if (!match.find()) return inputString;
        MemberCacheView cacheView = DiscordJDAConnection.getGuild().getMemberCache();
        // Doesn't work as intended rn
        return match.replaceAll(matchResult -> {
            String name = matchResult.group(1).toLowerCase();
            boolean isWithTag = matchResult.group(2) != null;
            List<Member> members = cacheView
                    .parallelStream()
                    // Removing all members whose name's length is less than what was provided for search
                    // Removing anyone whose name doesn't startsWith the name we are looking for
                    .filter(member -> (isWithTag ?
                            member.getUser().getAsTag().length() >= name.length() :
                            member.getUser().getName().length() >= name.length()
                    ) && member.getUser().getName().toLowerCase().startsWith(name))
                    .toList();
            if (members.isEmpty())
                return matchResult.group();
            if (members.size() == 1) {
                PluginLogger.info(members.get(0).getAsMention());
                return members.get(0).getAsMention();
            }
            // Priority to finding a user with exact match
            // If no exact match is found then we try to find a user with username Starting with the query(?)
            for (Member m : members) {
                if (m.getUser().getAsTag().equalsIgnoreCase(name) ||
                        m.getUser().getName().equalsIgnoreCase(name))
                    return m.getAsMention();
            }
            for (Member m : members) {
                if (m.getUser().getName().toLowerCase().startsWith(name))
                    return m.getAsMention();
            }
            PluginLogger.severe("Unexpected error in trying to find user, identify dev");
            // Since we are filtering users and returning if no user could be determined we should, In theory never actually return null
            // Matcher#replaceAll() will throw an error if we ever actually return null
            return null;
        });
    }
}
