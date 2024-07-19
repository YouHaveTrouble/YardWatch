package me.youhavetrouble.yardwatch.commands;

import me.youhavetrouble.yardwatch.Protection;
import me.youhavetrouble.yardwatch.YardWatch;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
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
        } else if (args[0].equalsIgnoreCase("query")) {
            queryProtection(sender);
            return true;
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("hooks");
            completions.add("query");
            return StringUtil.copyPartialMatches(args[0], completions, new ArrayList<>());
        }

        return completions;
    }

    private void sendDefault(CommandSender sender) {
        sender.sendMessage(String.format("YardWatch %s (Implementing YardWatch API %s)", version, YardWatch.getYardWatchApiVersion()));
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

    private void queryProtection(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(("You must be a player to use this command!"));
            return;
        }
        Location location = player.getLocation();
        sender.sendMessage("Protections at " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ());
        Collection<RegisteredServiceProvider<Protection>> protections = plugin.getServer().getServicesManager().getRegistrations(Protection.class);
        if (protections.isEmpty()) {
            sender.sendMessage("No protections registered at " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ());
            return;
        }
        for (RegisteredServiceProvider<Protection> protection : protections) {
            Component component = Component.text(protection.getPlugin().getName()).append(Component.text(" isProtected " + protection.getProvider().isProtected(location)))
                    .hoverEvent(HoverEvent.showText(
                            Component.text(protection.getProvider().toString())
                                    .append(Component.newline())
                                    .append(Component.text("canPlaceBlock " + protection.getProvider().canPlaceBlock(player, location))
                                            .append(Component.newline())
                                            .append(Component.text("canBreakBlock " + protection.getProvider().canBreakBlock(player, location.getBlock().getState())))
                                            .append(Component.newline())
                                            .append(Component.text("canInteract " + protection.getProvider().canInteract(player, location.getBlock().getState())))
                                    )));
            sender.sendMessage(component);
        }
    }
}
