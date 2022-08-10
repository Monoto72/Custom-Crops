package me.monoto.customcrops.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.apache.commons.lang.math.IntRange;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class Formatters {
    public static @NotNull Component mini(String message) {
        return MiniMessage.miniMessage().deserialize("<!italic>" + message);
    }

    public static Component mini(String message, String placeholder, Component replacement) {
        return MiniMessage.miniMessage().deserialize("<!italic>" + message, Placeholder.component(placeholder, replacement));
    }

    // TODO: Add support for multiple placeholders - Potentially done...
    public static Component miniMulti(String message, List<String> placeholders, List<Component> replacements) {
        Iterable<? extends TagResolver> placeholdersIterable = placeholders.stream().map(placeholder -> Placeholder.component(placeholder, replacements.get(placeholders.indexOf(placeholder)))).collect(Collectors.toList());

        return MiniMessage.miniMessage().deserialize("<!italic>" + message, TagResolver.resolver(placeholdersIterable));
    }

    public static Component time(Integer seconds) {
        if (seconds < 60) {
            return Component.text(seconds + " seconds");
        } else if (seconds < 3600) {
            return Component.text(seconds / 60 + " minutes");
        } else if (seconds < 86400) {
            return Component.text(seconds / 3600 + " hours");
        } else if (seconds < 604800) {
            return Component.text(seconds / 86400 + " days");
        } else if (seconds < 2419200) {
            return Component.text(seconds / 604800 + " weeks");
        } else if (seconds < 29030400) {
            return Component.text(seconds / 2419200 + " months");
        } else {
            return Component.text(seconds / 29030400 + " years");
        }
    }

    public static IntRange stringToRange(String range) {
        String[] split = range.split("-");
        return new IntRange(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
    }
}
