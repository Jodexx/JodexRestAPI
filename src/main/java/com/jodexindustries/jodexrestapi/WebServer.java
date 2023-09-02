package com.jodexindustries.jodexrestapi;

import com.jodexindustries.jodexrestapi.utils.ServerService;
import com.sun.net.httpserver.HttpServer;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.jodexindustries.jodexrestapi.Main.config;

public class WebServer {
    public static HttpServer server;

    public WebServer(Integer port) {
        try {
            server = HttpServer.create();
            server.setExecutor(null);
            server.bind(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public static void start() {
        initPlayerAPI();
    }
    public static void stop() {
        try {
            server.stop(1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Main.getInstance().logger.info(ChatColor.RED + "Web сервер успешно остановлен!");
    }

    public HttpServer getServer() {
        return server;
    }

    public static String checkServerTime() {
        Server server = Bukkit.getServer();
        World world = server.getWorld(Objects.requireNonNull(Main.config.getString("CheckForWorldTime")));
        long time = world != null ? world.getTime() : 0;
        return time > 0L && time < 12300L ? "Day" : "Night";
    }


    private static void initPlayerAPI() {
        if (Main.getInstance().getConfig().getBoolean("PlayerRequest")) {
            server.createContext("/api/player/", exchange ->  {
                String[] args = exchange.getRequestURI().getPath().split("/api/player/");
                if(exchange.getRequestURI().getPath().equalsIgnoreCase("/api/player/")) {
                    exchange.sendResponseHeaders(404, "404 - Enter the player's name".length());
                    OutputStream outputStream = exchange.getResponseBody();
                    outputStream.write("404 - Enter the player's name".getBytes());
                    outputStream.close();
                } else {
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                    JSONObject jsonObject;
                    OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
                    if (!player.hasPlayedBefore()) {
                        jsonObject = new JSONObject();
                        jsonObject.put("success", false);
                        exchange.sendResponseHeaders(200, jsonObject.toJSONString().getBytes().length);

                        OutputStream outputStream = exchange.getResponseBody();
                        outputStream.write(jsonObject.toJSONString().getBytes());
                        outputStream.close();
                    } else {
                        jsonObject = new JSONObject();

                        for (String settings : config.getConfigurationSection("Api.Player").getKeys(true)) {
                            jsonObject.put(settings, PlaceholderAPI.setPlaceholders(player, config.getString("Api.Player." + settings)));
                        }
                        exchange.sendResponseHeaders(200, jsonObject.toJSONString().getBytes().length);

                        OutputStream outputStream = exchange.getResponseBody();
                        outputStream.write(jsonObject.toJSONString().getBytes());
                        outputStream.close();
                    }
                }
            });
            if (Main.getInstance().getConfig().getBoolean("PlayTimeRequest")) {
                server.createContext("/api/server/playtime/", exchange ->  {
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                    JSONObject jsonObject = new JSONObject();
                    for (int i = 1; i < 11; i++) {
                        jsonObject.put("top_" + i + "_name", PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer("_Jodex__"), "%playtime_top_" + i + "_name%"));
                        jsonObject.put("top_" + i + "_time", PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer("_Jodex__"), "%playtime_top_" + i + "_time%"));

                    }
                    exchange.sendResponseHeaders(200, jsonObject.toJSONString().getBytes().length);

                    OutputStream outputStream = exchange.getResponseBody();
                    outputStream.write(jsonObject.toJSONString().getBytes());
                    outputStream.close();
                });
            }

            if (Main.getInstance().getConfig().getBoolean("ServerRequest")) {
                server.createContext("/api/server/", exchange ->  {
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                    JSONObject jsonObject = new JSONObject();
                    if (config.getBoolean("Api.Server.worldTimeStatus")) {
                        jsonObject.put("worldTimeStatus", checkServerTime());
                    }

                    if (config.getBoolean("Api.Server.playerList")) {
                        List<String> list = new ArrayList<>();
                        if(!Bukkit.getOnlinePlayers().isEmpty()) {
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                list.add(p.getName());
                            }
                            Collections.sort(list);
                        }
                        jsonObject.put("playerList", list.toString().replace("[", "").replace("]", "").replace(",", ""));
                    }

                    if (config.getBoolean("Api.Server.serverOnline")) {
                        jsonObject.put("serverOnline", PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer("_Jodex__"), "%server_online%"));
                    }

                    if (config.getBoolean("Api.Server.serverUptime")) {
                        jsonObject.put("serverUptime", PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer("_Jodex__"), "%server_uptime%"));
                    }

                    if (config.getBoolean("Api.Server.serverTPS")) {
                        jsonObject.put("serverTPS", PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer("_Jodex__"), "%server_tps_1%"));
                    }

                    if (config.getBoolean("Api.Server.serverRamMax")) {
                        jsonObject.put("serverRamMax", PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer("_Jodex__"), "%server_ram_max%"));
                    }

                    if (config.getBoolean("Api.Server.serverRamUsed")) {
                        jsonObject.put("serverRamUsed", PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer("_Jodex__"), "%server_ram_used%"));
                    }
                    if (config.getBoolean("Api.Server.pingAll")) {
                        // pingAll
                        int ping = 0;
                        int pingAll = 0;
                        if(!Bukkit.getOnlinePlayers().isEmpty()) {
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                ping += player.getPing();
                            }
                            pingAll = ping / Bukkit.getOnlinePlayers().size();
                        }

                        jsonObject.put("pingAll", pingAll);
                    }
                    if (config.getBoolean("Api.Server.status")) {
                        // status
                        double[] tpss = Bukkit.getTPS();
                        int tps = (int) tpss[0];
                        String status = "ok";
                        if (tps <= 15) {
                            status = "badtps";
                        }
                        jsonObject.put("status", status);
                    }
                    exchange.sendResponseHeaders(200, jsonObject.toJSONString().getBytes().length);

                    OutputStream outputStream = exchange.getResponseBody();
                    outputStream.write(jsonObject.toJSONString().getBytes());
                    outputStream.close();
                });
            }

            if (Main.getInstance().getConfig().getBoolean("PostRequest")) {
                server.createContext("/api/server/command", exchange -> {
                    if(exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
                        BufferedReader br = new BufferedReader(isr);
                        StringBuilder requestBody = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            requestBody.append(line);
                        }
                        br.close();
                        isr.close();
                        exchange.getResponseHeaders().set("Content-Type", "text/plain");

                        ServerService.sendBroadcast(requestBody.toString());
                        String responseData = "Received POST data: " + requestBody;

                        exchange.sendResponseHeaders(200, responseData.getBytes().length);

                        // Write the response data to the response body
                        OutputStream outputStream = exchange.getResponseBody();
                        outputStream.write(responseData.getBytes());
                        outputStream.close();
                    } else {
                        exchange.sendResponseHeaders(405, -1);
                        exchange.close();
                    }
                });
            }

            server.start();
        }
        Main.getInstance().logger.info(ChatColor.GREEN + "Web сервер успешно запущен!");
    }
}
