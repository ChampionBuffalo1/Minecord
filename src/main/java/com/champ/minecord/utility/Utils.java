package com.champ.minecord.utility;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class Utils {
    public static String toPlainText(Component component) {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }

    public static String toTextWithColorCodes(Component component) {
        return LegacyComponentSerializer.legacyAmpersand().serialize(component);
    }
}
