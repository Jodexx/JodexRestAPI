package com.jodexindustries.jodexrestapi.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jodexindustries.jodexrestapi.Main;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import javax.net.ssl.HttpsURLConnection;

import com.jodexindustries.jodexrestapi.WebServer;
import com.sun.net.httpserver.HttpServer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Utils {
    Main plugin;

    public Utils(Main plugin) {
        this.plugin = plugin;
    }

    public static String color(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static void sendMessage(CommandSender sender, String input) {
        sender.sendMessage(color(input));
    }

    public static String getServerIP() {
        JsonObject root = getJSON();
        return root == null ? "-1" : root.get("message").getAsString();
    }

    private static JsonObject getJSON() {
        try {
            HttpURLConnection connection = (HttpsURLConnection)(new URL("https://verify.minetopiasdb.nl/reqip.php")).openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", "MTWAPENS");
            connection.setRequestProperty("Version", Main.getInstance().getDescription().getVersion());
            connection.connect();
            return JsonParser.parseReader(new InputStreamReader((InputStream) connection.getContent())).getAsJsonObject();
        } catch (IOException var3) {
            return null;
        }
    }

    public static boolean isServerRunning(HttpServer server) {
        try {
            WebServer.server = HttpServer.create(server.getAddress(), 0);
            return false;
        } catch (IOException e) {
            return true;
        }
    }
}
