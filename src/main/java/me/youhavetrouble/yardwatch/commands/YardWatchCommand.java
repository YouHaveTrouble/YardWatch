package me.youhavetrouble.yardwatch.commands;

import me.youhavetrouble.yardwatch.Protection;
import me.youhavetrouble.yardwatch.YardWatch;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class YardWatchCommand implements TabExecutor {

    private final YardWatch plugin;

    private final String version;

    public YardWatchCommand(YardWatch plugin) {
        this.plugin = plugin;
        this.version = plugin.getDescription().getVersion();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length == 0) {
            sendDefault(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("hooks")) {
            sendHooks(sender);
            return true;
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("hooks");
            return StringUtil.copyPartialMatches(args[0], completions, new ArrayList<>());
        }

        return completions;
    }

    private void sendDefault(CommandSender sender) {
        sender.sendMessage("YardWatch " + version);
    }

    private void sendHooks(CommandSender sender) {
        Map<String, Integer> hooks = new HashMap<>();
        Collection<RegisteredServiceProvider<Protection>> protections = plugin.getServer().getServicesManager().getRegistrations(Protection.class);
        for (RegisteredServiceProvider<Protection> protection : protections) {
            hooks.merge(protection.getPlugin().getName(), 1, Integer::sum);
        }
        if (hooks.isEmpty()) {
            sender.sendMessage("No hooks registered.");
            return;
        }
        sender.sendMessage("Hooks:");
        for (Map.Entry<String, Integer> entry : hooks.entrySet()) {
            String hook = entry.getValue() == 1 ? "hook" : "hooks";
            sender.sendMessage(String.format("%s -> %s %s registered", entry.getKey(), entry.getValue(), hook));
        }

    }
}
