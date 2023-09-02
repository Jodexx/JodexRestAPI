package com.jodexindustries.jodexrestapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.Nullable;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    private static final String[] COMMANDS = new String[]{"start", "stop", "url", "reloadconfig"};


    public @Nullable List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        StringUtil.copyPartialMatches(args[0], Arrays.asList(COMMANDS), completions);
        Collections.sort(completions);
        if (args.length == 0) {
            return completions;
        } else {
            return args.length > 1 ? new ArrayList<>() : completions;
        }
    }
}
