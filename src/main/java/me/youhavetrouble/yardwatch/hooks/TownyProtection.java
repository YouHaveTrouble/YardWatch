package me.youhavetrouble.yardwatch.hooks;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownyPermission;
import com.palmergames.bukkit.towny.utils.CombatUtil;
import me.youhavetrouble.yardwatch.Protection;
import me.youhavetrouble.yardwatch.YardWatch;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class TownyProtection implements Protection {

    private final YardWatch plugin;

    private final TownyAPI api;

    public TownyProtection(YardWatch plugin) {
        this.plugin = plugin;

        this.api = TownyAPI.getInstance();
    }

    @Override
    public boolean isEnabled() {
        return this.plugin.getServer().getPluginManager().isPluginEnabled("Towny");
    }

    @Override
    public boolean isProtected(Location location) {
        if (!isEnabled()) return false;

        TownBlock town = this.api.getTownBlock(location);

        return town != null;
    }

    @Override
    public boolean canBreakBlock(Player player, BlockState blockState) {
        return canInteract(player, blockState);
    }

    @Override
    public boolean canPlaceBlock(Player player, Location location) {
        return canInteract(player, location.getBlock().getState(true));
    }

    @Override
    public boolean canInteract(Player player, BlockState blockState) {
        if (!isEnabled()) return true;

        Location location = blockState.getLocation();

        TownBlock town = this.api.getTownBlock(location);

        Resident resident = this.api.getResident(player.getUniqueId());

        return town == null || town.hasResident(resident) || town.hasTrustedResident(resident);
    }

    @Override
    public boolean canInteract(Player player, Entity target) {
        return canInteract(player, target.getLocation().getBlock().getState(true));
    }

    @Override
    public boolean canDamage(Entity damager, Entity target) {
        if (!isEnabled()) return true;
        if (!(damager instanceof Player)) return true;

        Location location = target.getLocation();

        TownBlock town = this.api.getTownBlock(location);

        return town == null || CombatUtil.preventPvP(town.getWorld(), town);
    }
}
