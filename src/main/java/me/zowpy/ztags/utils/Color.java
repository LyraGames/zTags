package me.zowpy.ztags.utils;

import org.bukkit.ChatColor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Color {
    public static final String BLANK_MESSAGE = String.join("", Collections.nCopies(150, "§8 §8 §1 §3 §3 §7 §8 §r\n"));

    public static String translate(String input) {
        try {

            Replacement replacement = new Replacement(input);


            return ChatColor.translateAlternateColorCodes('&', replacement.toString(false));
        } catch (Exception ignored) {
            return ChatColor.translateAlternateColorCodes('&', input);
        }
    }

    public static List<String> translate(List<String> input) {
        return input.stream().map(Color::translate).collect(Collectors.toList());
    }
}
