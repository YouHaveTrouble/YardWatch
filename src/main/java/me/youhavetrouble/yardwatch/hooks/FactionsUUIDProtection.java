package me.youhavetrouble.yardwatch.hooks;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;

import com.massivecraft.factions.listeners.FactionsBlockListener;
import com.massivecraft.factions.listeners.FactionsEntityListener;
import com.massivecraft.factions.listeners.FactionsPlayerListener;
import com.massivecraft.factions.perms.PermissibleActions;
import me.youhavetrouble.yardwatch.Protection;
import me.youhavetrouble.yardwatch.YardWatch;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class FactionsUUIDProtection implements Protection {

    private final YardWatch plugin;

    public FactionsUUIDProtection(YardWatch plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isEnabled() {
        return plugin.getServer().getPluginManager().isPluginEnabled("Factions");
    }

    @Override
    public boolean isProtected(Location location) {
        if (!isEnabled()) return false;
        if (!FactionsPlugin.getInstance().worldUtil().isEnabled(location.getWorld())) return false;
        FLocation fLocation = new FLocation(location);
        Faction faction = Board.getInstance().getFactionAt(fLocation);
        return faction != null;
    }

    @Override
    public boolean canBreakBlock(Player player, BlockState blockState) {
        if (!isEnabled()) return true;
        return FactionsBlockListener.playerCanBuildDestroyBlock(
                player,
                blockState.getLocation(),
                PermissibleActions.DESTROY,
                true
        );
    }

    @Override
    public boolean canPlaceBlock(Player player, Location location) {
        if (!isEnabled()) return true;
        return FactionsBlockListener.playerCanBuildDestroyBlock(
                player,
                location,
                PermissibleActions.BUILD,
                true
        );
    }

    @Override
    public boolean canInteract(Player player, BlockState blockState) {
        if (!isEnabled()) return true;
        return FactionsPlayerListener.canInteractHere(player, blockState.getLocation());
    }

    @Override
    public boolean canInteract(Player player, Entity target) {
        if (!isEnabled()) return true;
        return FactionsEntityListener.canInteractHere(player, target.getLocation());
    }

    @Override
    public boolean canDamage(Entity damager, Entity target) {
        if (!isEnabled()) return true;
        return FactionsEntityListener.canDamage(damager, target, false);
    }
}
