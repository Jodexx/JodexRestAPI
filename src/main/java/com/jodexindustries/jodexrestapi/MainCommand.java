package com.jodexindustries.jodexrestapi;

import com.jodexindustries.jodexrestapi.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§6Jodex§bRestAPI §7by §c_Jodex__");
            sender.sendMessage("§7/jodexra §astart §f- §7start webapi server");
            sender.sendMessage("§7/jodexra §astop §f- §7stop webapi server");
            sender.sendMessage("§7/jodexra §aurl §f- §7show link webapi server");
            sender.sendMessage("§7/jodexra §areloadconfig §f- §7reload config");
            return true;
        } else {
            if (args[0].equalsIgnoreCase("start")) {
                if (sender.hasPermission("jodexrestapi.start")) {
                    if (!Utils.isServerRunning(WebServer.server)) {
                        try {
                            WebServer.start();
                            sender.sendMessage("§aWeb server successful started!");
                        } catch (IllegalAccessError var6) {
                            var6.printStackTrace();
                        }
                    } else {
                        sender.sendMessage("§cWeb already started!");
                    }
                } else {
                    sender.sendMessage("§cYou don't have permissions!");
                }
            }

            if (args[0].equalsIgnoreCase("stop")) {
                if (sender.hasPermission("jodexrestapi.stop")) {
                    if (Utils.isServerRunning(WebServer.server)) {
                        WebServer.stop();
                        sender.sendMessage("§aWeb server successful stopped!");
                    } else {
                        sender.sendMessage("§cWeb server already stopped!");
                    }
                } else {
                    sender.sendMessage("§cYou don't have permissions!");
                }
            }

            if (args[0].equalsIgnoreCase("url")) {
                if (sender.hasPermission("jodexrestapi.url")) {
                    Utils.sendMessage(sender, "&cWeb server started: \n&bhttp://" + Utils.getServerIP() + ":" + Main.getInstance().getConfig().getInt("webserver.port") + "/api/player/" + sender.getName());
                } else {
                    sender.sendMessage("§cYou don't have permissions!");
                }
            }

            if (args[0].equalsIgnoreCase("reloadconfig")) {
                if (sender.hasPermission("jodexrestapi.reloadconfig")) {
                    sender.sendMessage("§aConfig successful reloaded!");
                    Main.getInstance().reloadConfig();
                } else {
                    sender.sendMessage("§cYou don't have permissions!");
                }
            }

            return true;
        }
    }
}
