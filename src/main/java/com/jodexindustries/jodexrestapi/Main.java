package com.jodexindustries.jodexrestapi;

import com.jodexindustries.jodexrestapi.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Main instance;
    public static FileConfiguration config;


    public void onEnable() {
        instance = this;
        config = getConfig();
        new WebServer(this.getConfig().getInt("webserver.port"));
        WebServer.start();
        getCommand("jodexra").setExecutor(new MainCommand());
        getCommand("jodexra").setTabCompleter(new TabCompleter());
        saveDefaultConfig();

    }

    public void onDisable() {
        if (Utils.isServerRunning(WebServer.server)) {
            WebServer.stop();
            logger.info(ChatColor.RED + "Web сервер успешно остановлен!");
        }

    }

    public static Main getInstance() {
        return instance;
    }
}