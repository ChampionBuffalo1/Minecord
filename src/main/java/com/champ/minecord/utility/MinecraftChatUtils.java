package com.champ.minecord.utility;

import com.champ.minecord.discord.DiscordJDAConnection;
import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class MinecraftChatUtils {
    private final static Pattern simpleEmote = Pattern.compile(":(\\w+):");
    private final static Pattern simpleChannel = Pattern.compile("#([a-z\\d-]+)");
    private final static Pattern simpleMember = Pattern.compile("@([\\w\\d#]+)");

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
            String emote = DiscordJDAConnection.getEmote(key);
            if (emote == null) return matchResult.group();
            return emote;
        });
    }

    public static String injectChannelMentions(String inputString) {
        Matcher match = simpleChannel.matcher(inputString);
        if (!match.find()) return inputString;
        return match.replaceAll(matchResult -> {
            String name = matchResult.group(1);
            List<TextChannel> channels = DiscordJDAConnection.getJda().getTextChannelsByName(name, true);
            Optional<TextChannel> channel = channels.parallelStream().findFirst();
            return channel.map(Channel::getAsMention).orElseGet(matchResult::group);
        });
    }

    public static String injectMemberMentions(String inputString) {
        Matcher match = simpleMember.matcher(inputString);
        if (!match.find()) return inputString;
        // Doesn't work as intended rn
        return match.replaceAll(matchResult -> {
            String name = matchResult.group(1).toLowerCase();
            Stream<Member> memStream = DiscordJDAConnection.getGuild().getMemberCache()
                    .parallelStream()
                    // Removing all members whose name's length is less than what was provided for search
                    // Removing anyone whose name doesn't startsWith the name we are looking for
                    .filter(member -> member.getUser().getName().length() >= name.length() && member.getUser().getName().toLowerCase().startsWith(name));
            if (memStream.findAny().isEmpty())
                return matchResult.group();
            if (memStream.count() == 1 && memStream.findFirst().isPresent())
                return memStream.findFirst().get().getAsMention();
            for (Iterator<Member> it = memStream.iterator(); it.hasNext(); ) {
                Member m = it.next();
                if (m.getUser().getName().equalsIgnoreCase(name))
                    return m.getAsMention();
            }
            for (Iterator<Member> it = memStream.iterator(); it.hasNext(); ) {
                Member m = it.next();
                if (m.getUser().getName().toLowerCase().startsWith(name))
                    return m.getAsMention();
            }
            // Should never return null
            return null;
        });
    }
}
