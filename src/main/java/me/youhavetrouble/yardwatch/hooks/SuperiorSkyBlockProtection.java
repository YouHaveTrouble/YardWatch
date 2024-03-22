package me.youhavetrouble.yardwatch.hooks;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import me.youhavetrouble.yardwatch.Protection;
import me.youhavetrouble.yardwatch.YardWatch;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class SuperiorSkyBlockProtection implements Protection {

    private final YardWatch plugin;

    public SuperiorSkyBlockProtection(YardWatch plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isEnabled() {
        return this.plugin.getServer().getPluginManager().isPluginEnabled("SuperiorSkyBlock");
    }

    @Override
    public boolean isProtected(Location location) {
        if (!isEnabled()) return true;



        return false;
    }

    @Override
    public boolean canBreakBlock(Player player, BlockState blockState) {
        if (!isEnabled()) return true;

        return false;
    }

    @Override
    public boolean canPlaceBlock(Player player, Location location) {
        if (!isEnabled()) return true;

        return false;
    }

    @Override
    public boolean canInteract(Player player, BlockState blockState) {
        if (!isEnabled()) return true;

        return false;
    }

    @Override
    public boolean canInteract(Player player, Entity target) {
        if (!isEnabled()) return true;

        return false;
    }

    @Override
    public boolean canDamage(Entity damager, Entity target) {
        if (!isEnabled()) return true;
        if (!(damager instanceof Player attacker)) return true;
        if (!(target instanceof Player receiver)) return true;

        SuperiorPlayer attackingPlayer = SuperiorSkyblockAPI.getPlayer(attacker.getUniqueId());
        SuperiorPlayer targetPlayer = SuperiorSkyblockAPI.getPlayer(receiver.getUniqueId());

        return false;
    }
}