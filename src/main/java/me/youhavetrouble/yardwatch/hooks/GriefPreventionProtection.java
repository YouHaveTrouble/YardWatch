package me.youhavetrouble.yardwatch.hooks;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.youhavetrouble.yardwatch.Protection;
import me.youhavetrouble.yardwatch.YardWatch;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.UUID;

public class GriefPreventionProtection implements Protection {

    private final YardWatch plugin;

    public GriefPreventionProtection(YardWatch plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isEnabled() {
        return plugin.getServer().getPluginManager().isPluginEnabled("GriefPrevention");
    }

    @Override
    public boolean isProtected(Location location) {
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, true, null);
        if (claim == null) return false;
        return claim.checkPermission(UUID.fromString("00000000-0000-0000-0000-000000000000"), ClaimPermission.Build, null) != null;
    }

    @Override
    public boolean canBreakBlock(Player player, BlockState blockState) {
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(blockState.getLocation(), true, null);
        return claim == null || claim.hasExplicitPermission(player.getUniqueId(), ClaimPermission.Build) || claim.hasExplicitPermission(player.getUniqueId(), ClaimPermission.Edit);
    }

    @Override
    public boolean canPlaceBlock(Player player, Location location) {
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, true, null);
        return claim == null || claim.hasExplicitPermission(player.getUniqueId(), ClaimPermission.Build) || claim.hasExplicitPermission(player.getUniqueId(), ClaimPermission.Edit);
    }

    @Override
    public boolean canInteract(Player player, BlockState blockState) {
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(blockState.getLocation(), true, null);
        return claim == null || claim.hasExplicitPermission(player.getUniqueId(), ClaimPermission.Access);
    }

    @Override
    public boolean canInteract(Player player, Entity target) {
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(target.getLocation(), true, null);
        return claim == null || claim.hasExplicitPermission(player.getUniqueId(), ClaimPermission.Inventory); // do not ask why it's "inventory"...
    }

    @Override
    public boolean canDamage(Entity damager, Entity target) {
        if (!(damager instanceof Player player)) return true;
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(target.getLocation(), true, null);
        return claim == null || claim.hasExplicitPermission(player.getUniqueId(), ClaimPermission.Inventory); // do not ask why it's "inventory"...
    }
}
