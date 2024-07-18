package me.youhavetrouble.yardwatch.hooks;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.Association;
import com.sk89q.worldguard.protection.association.Associables;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.youhavetrouble.yardwatch.Protection;
import me.youhavetrouble.yardwatch.YardWatch;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class WorldGuardProtection implements Protection {

    private final YardWatch plugin;

    public WorldGuardProtection(YardWatch plugin) {
       this.plugin = plugin;
    }

    @Override
    public boolean isEnabled() {
        return plugin.getServer().getPluginManager().isPluginEnabled("WorldGuard");
    }

    @Override
    public boolean isProtected(Location location) {
        if (!isEnabled()) return false;
        if (location.getWorld() == null) return false;
        com.sk89q.worldedit.util.Location wgLocation = BukkitAdapter.adapt(location);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        return query.testBuild(wgLocation, Associables.constant(Association.NON_MEMBER));
    }

    @Override
    public boolean canBreakBlock(Player player, BlockState blockState) {
        if (!isEnabled()) return true;
        Location location = blockState.getLocation();
        com.sk89q.worldedit.util.Location wgLocation = BukkitAdapter.adapt(location);
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player, true);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        return query.testState(wgLocation, localPlayer, Flags.BUILD);
    }

    @Override
    public boolean canPlaceBlock(Player player, Location location) {
        if (!isEnabled()) return true;
        com.sk89q.worldedit.util.Location wgLocation = BukkitAdapter.adapt(location);
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player, true);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        return query.testState(wgLocation, localPlayer, Flags.BUILD);
    }

    @Override
    public boolean canInteract(Player player, BlockState blockState) {
        if (!isEnabled()) return true;
        com.sk89q.worldedit.util.Location wgLocation = BukkitAdapter.adapt(blockState.getLocation());
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player, true);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        return query.testState(wgLocation, localPlayer, Flags.INTERACT);
    }

    @Override
    public boolean canInteract(Player player, Entity entity) {
        if (!isEnabled()) return true;
        com.sk89q.worldedit.util.Location wgLocation = BukkitAdapter.adapt(entity.getLocation());
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player, true);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        return query.testState(wgLocation, localPlayer, Flags.INTERACT);
    }

    @Override
    public boolean canDamage(Entity attacker, Entity target) {
        if (!isEnabled()) return true;
        if (!(attacker instanceof Player player)) return true;
        com.sk89q.worldedit.util.Location wgLocation = BukkitAdapter.adapt(target.getLocation());
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player, true);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        return query.testState(wgLocation, localPlayer, Flags.INTERACT);
    }
}
