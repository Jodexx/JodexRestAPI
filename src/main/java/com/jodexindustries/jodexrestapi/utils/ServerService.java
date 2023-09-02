package com.jodexindustries.jodexrestapi.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class ServerService {
    public static void sendBroadcast(String command) {
        Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&8[&cRestAPI Server&8] " + command));
    }
}
