package com.champ.minecord.discord;

import com.champ.minecord.utility.PluginLogger;
import net.dv8tion.jda.api.entities.Guild;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class EntityCache {
    private final static Map<String, String> emotes = new ConcurrentHashMap<>();
    private final static Map<String, String> stickers = new ConcurrentHashMap<>();

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
                        .forEach(sticker -> stickers.put(sticker.getName(), sticker.getId()))
                );
    }

    /**
     * updates or puts emote onto the emote Map
     *
     * @param emoteName      Emoji name
     * @param formattedEmote Emoji in formatted form
     */
    public static void updateEmote(String emoteName, String formattedEmote) {
        emotes.put(emoteName.toLowerCase(), formattedEmote);
    }

    /**
     * Removes the given emote name from the emote map
     *
     * @param emoteName emoji to remove from the map
     */
    public static void removeEmote(String emoteName) {
        emotes.remove(emoteName.toLowerCase());
    }

    /**
     * updates of puts sticker onto the stickers map
     *
     * @param stickerName sticker to update
     * @param stickerId   stickerId to set
     */
    public static void updateSticker(String stickerName, String stickerId) {
        stickers.put(stickerName, stickerId);
    }

    /**
     * Removes sticker from the stickers map
     *
     * @param stickerName sticker to remove
     */
    public static void removeSticker(String stickerName) {
        stickers.remove(stickerName);
    }

    /**
     * A special method to find and delete key whose value is same as the provided stickerId
     *
     * @param stickerId Value to find in stickers map and delete the key
     */
    public static void removeStickerById(String stickerId) {
        for (Map.Entry<String, String> entry : stickers.entrySet()) {
            if (stickerId.equals(entry.getValue()))
                stickers.remove(entry.getKey());
        }
    }

    /**
     * @return Set of names of all stickers in stickers map
     */
    public static Set<String> getStickerKeys() {
        return stickers.keySet();
    }

    /**
     * Retrieves an emote from the emote map
     *
     * @param emoteName emote to retrieve
     * @param orElse    return this value if value isn't present in map
     * @return Formatted emote
     */
    public static String getEmoteOrElse(String emoteName, String orElse) {
        return emotes.getOrDefault(emoteName.toLowerCase(), orElse);
    }

    /**
     * Retrieves an sticker from stickers map
     *
     * @param stickerName sticker to retrieve
     * @return StickerId
     */
    public static String getSticker(String stickerName) {
        return stickers.get(stickerName);
    }
}
