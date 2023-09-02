package com.jodexindustries.jodexrestapi;

import com.jodexindustries.jodexrestapi.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§6Jodex§bRestAPI §7by §c_Jodex__");
            sender.sendMessage("§7/jodexra §astart §f- §7запустить webapi сервер");
            sender.sendMessage("§7/jodexra §astop §f- §7остановить webapi сервер");
            sender.sendMessage("§7/jodexra §aurl §f- §7показать ссылку на webapi сервер");
            sender.sendMessage("§7/jodexra §areloadconfig §f- §7перезагрузить конфиг");
            return true;
        } else {
            if (args[0].equalsIgnoreCase("start")) {
                if (sender.hasPermission("jodexrestapi.start")) {
                    if (!Utils.isServerRunning(WebServer.server)) {
                        try {
                            WebServer.start();
                            sender.sendMessage("§aWeb сервер успешно запущен!");
                        } catch (IllegalAccessError var6) {
                            var6.printStackTrace();
                        }
                    } else {
                        sender.sendMessage("§cWeb сервер уже запущен!");
                    }
                } else {
                    sender.sendMessage("§cУ вас недостаточно прав!");
                }
            }

            if (args[0].equalsIgnoreCase("stop")) {
                if (sender.hasPermission("jodexrestapi.stop")) {
                    if (Utils.isServerRunning(WebServer.server)) {
                        WebServer.stop();
                        sender.sendMessage("§aWeb сервер успешно остановлен!");
                    } else {
                        sender.sendMessage("§cWeb сервер уже остановлен!");
                    }
                } else {
                    sender.sendMessage("§cУ вас недостаточно прав!");
                }
            }

            if (args[0].equalsIgnoreCase("url")) {
                if (sender.hasPermission("jodexrestapi.url")) {
                    Utils.sendMessage(sender, "&cWeb сервер запущен: \n&bhttp://" + Utils.getServerIP() + ":" + Main.getInstance().getConfig().getInt("webserver.port") + "/api/player/" + sender.getName());
                } else {
                    sender.sendMessage("§cУ вас недостаточно прав!");
                }
            }

            if (args[0].equalsIgnoreCase("reloadconfig")) {
                if (sender.hasPermission("jodexrestapi.reloadconfig")) {
                    sender.sendMessage("§aКонфиг успешно перезагружен!");
                    Main.getInstance().reloadConfig();
                } else {
                    sender.sendMessage("§cУ вас недостаточно прав!");
                }
            }

            return true;
        }
    }
}
