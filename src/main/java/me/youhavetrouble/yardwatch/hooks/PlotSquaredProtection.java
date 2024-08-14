package me.youhavetrouble.yardwatch.hooks;

import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.flag.implementations.PvpFlag;
import me.youhavetrouble.yardwatch.Protection;
import me.youhavetrouble.yardwatch.YardWatch;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

public class PlotSquaredProtection implements Protection {

    private final YardWatch plugin;

    public PlotSquaredProtection(YardWatch plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isEnabled() {
        return this.plugin.getServer().getPluginManager().isPluginEnabled("PlotSquared");
    }

    @Override
    public boolean isProtected(final Location location) {
        if (!isEnabled()) return false;

        final com.plotsquared.core.location.Location arg1 = getLocation(location);

        return arg1.isPlotArea() || arg1.isPlotRoad() || arg1.isUnownedPlotArea() || arg1.getOwnedPlot() != null;
    }

    @Override
    public boolean canBreakBlock(final Player player, final BlockState blockState) {
        if (!isEnabled()) return true;

        final Location blockLocation = blockState.getLocation();
        final com.plotsquared.core.location.@NonNull Location location = getLocation(blockLocation);

        final Plot plot = location.getOwnedPlot();

        if (plot == null) return isProtected(blockLocation);

        return plot.isAdded(player.getUniqueId());
    }

    @Override
    public boolean canPlaceBlock(final Player player, final Location location) {
        return canBreakBlock(player, location.getBlock().getState());
    }

    @Override
    public boolean canInteract(final Player player, final BlockState blockState) {
        if (!isEnabled()) return true;

        final Location location = blockState.getLocation();

        com.plotsquared.core.location.@NonNull Location plotLocation = getLocation(location);

        final Plot plot = plotLocation.getOwnedPlot();

        if (plot == null) return isProtected(location);

        return plot.isAdded(player.getUniqueId());
    }

    @Override
    public boolean canInteract(final Player player, final Entity target) {
        return canInteract(player, target.getLocation().getBlock().getState());
    }

    @Override
    public boolean canDamage(final Entity damager, final Entity target) {
        if (!isEnabled() || !(damager instanceof Player)) return true;

        final Location location = target.getLocation();

        com.plotsquared.core.location.@NonNull Location plotLocation = getLocation(location);

        final Plot plot = plotLocation.getOwnedPlot();

        if (plot == null) return isProtected(location);

        return plot.getFlag(PvpFlag.class);
    }

    private com.plotsquared.core.location.@NonNull Location getLocation(final Location location) {
        final String world = location.getWorld().getName();
        final int x = (int) location.getX();
        final int y = (int) location.getY();
        final int z = (int) location.getZ();

        return com.plotsquared.core.location.Location.at(world, x, y, z);
    }
}