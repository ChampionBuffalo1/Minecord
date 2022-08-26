package com.champ.minecord.commands;

import com.champ.minecord.Minecord;
import com.champ.minecord.discord.EntityCache;
import com.champ.minecord.discord.JdaConnection;
import com.champ.minecord.utility.Utils;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.sticker.StickerSnowflake;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class StickerCommand implements TabExecutor {
    public StickerCommand() {
        Minecord.getPlugin().registerCommand("sticker", this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player && args.length >= 1) {
            String stickerName = String.join(" ", args);
            String stickerId = EntityCache.getSticker(stickerName);
            if (stickerId == null) {
                player.sendMessage(ChatColor.RED + "No sticker found with name: " + stickerName);
            } else {
                Message message = new MessageBuilder()
                        .setContent(Utils.toPlainText(player.displayName()) + ": ")
                        .setStickers(StickerSnowflake.fromId(stickerId))
                        .build();
                JdaConnection.getTextChannel().sendMessage(message).queue();
                player.sendMessage(ChatColor.GREEN + "" + ChatColor.ITALIC + "Sticker Sent!");
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return EntityCache.getStickerKeys()
                    .parallelStream()
                    .filter(name -> name.startsWith(args[0]))
                    .toList();
        }
        return null;
    }
}
