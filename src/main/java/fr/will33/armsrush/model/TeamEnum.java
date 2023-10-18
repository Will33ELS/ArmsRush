package fr.will33.armsrush.model;

import org.bukkit.ChatColor;

public enum TeamEnum {

    WHITE(ChatColor.WHITE),
    GRAY(ChatColor.DARK_GRAY),
    RED(ChatColor.DARK_RED),
    ORANGE(ChatColor.GOLD),
    YELLOW(ChatColor.YELLOW),
    GREEN(ChatColor.DARK_GREEN),
    CYAN(ChatColor.DARK_AQUA),
    PURPLE(ChatColor.DARK_PURPLE)
    ;

    private final ChatColor color;

    TeamEnum(ChatColor color) {
        this.color = color;
    }

    public ChatColor getColor() {
        return color;
    }
}
