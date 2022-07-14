package com.champ.minecord.discord;

import java.util.concurrent.ConcurrentHashMap;

import com.champ.minecord.utility.PluginLogger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.sticker.GuildSticker;

public class EntityCache {
    private final static ConcurrentHashMap<String, String> emotes = new ConcurrentHashMap<>();
    private final static ConcurrentHashMap<String, GuildSticker> stickers = new ConcurrentHashMap<>();

    public static void Setup(Guild guild) {
        PluginLogger.info("Retrieving sticker and emotes from guild");
        // RestAction<?>.queue() spawns a worker thread
        // which is why we are using ConcurrentHashMap instead of normal HashMap
        guild.retrieveEmojis()
                .queue(emoteList -> emoteList
                        .forEach(emote -> emotes.put(emote.getName().toLowerCase(), emote.getFormatted()))
                );
        guild.retrieveStickers()
                .queue(stickerList -> stickerList
                        .forEach(sticker -> stickers.put(sticker.getName().toLowerCase(), sticker))
                );
    }

    public static String getEmote(String emoteName) {
        return emotes.get(emoteName);
    }
    public static String getEmoteOrElse(String emoteName, String orElse) {
       return emotes.getOrDefault(emoteName, orElse);
    }
    public static GuildSticker getSticker(String emoteName) {
        return stickers.get(emoteName);
    }
}
