package me.youhavetrouble.yardwatch.hooks;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.BlockChangeResult;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.island.IslandPrivilege;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import me.youhavetrouble.yardwatch.Protection;
import me.youhavetrouble.yardwatch.YardWatch;
import org.bukkit.Location;
import org.bukkit.block.Block;
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
        return this.plugin.getServer().getPluginManager().isPluginEnabled("SuperiorSkyblock2");
    }

    @Override
    public boolean isProtected(Location location) {
        if (!isEnabled()) return false;

        Island island = SuperiorSkyblockAPI.getIslandAt(location) == null ? SuperiorSkyblockAPI.getSpawnIsland() : SuperiorSkyblockAPI.getIslandAt(location);

        if (island == null) return false;

        Block block = location.getBlock();

        return island.handleBlockPlaceWithResult(block) == BlockChangeResult.SUCCESS || island.handleBlockBreakWithResult(block) == BlockChangeResult.SUCCESS;
    }

    @Override
    public boolean canBreakBlock(Player player, BlockState blockState) {
        if (!isEnabled()) return true;

        Location location = blockState.getLocation();

        Island island = SuperiorSkyblockAPI.getIslandAt(location) == null ? SuperiorSkyblockAPI.getSpawnIsland() : SuperiorSkyblockAPI.getIslandAt(location);

        return island == null || island.handleBlockBreakWithResult(blockState.getBlock()) == BlockChangeResult.SUCCESS;
    }

    @Override
    public boolean canPlaceBlock(Player player, Location location) {
        if (!isEnabled()) return true;

        Island island = SuperiorSkyblockAPI.getIslandAt(location) == null ? SuperiorSkyblockAPI.getSpawnIsland() : SuperiorSkyblockAPI.getIslandAt(location);

        return island == null || island.handleBlockPlaceWithResult(location.getBlock()) == BlockChangeResult.SUCCESS;
    }

    @Override
    public boolean canInteract(Player player, BlockState blockState) {
        if (!isEnabled()) return true;

        Location location = blockState.getLocation();

        Island island = SuperiorSkyblockAPI.getIslandAt(location) == null ? SuperiorSkyblockAPI.getSpawnIsland() : SuperiorSkyblockAPI.getIslandAt(location);

        return island == null || island.hasPermission(player, IslandPrivilege.getByName("INTERACT"));
    }

    @Override
    public boolean canInteract(Player player, Entity target) {
        if (!isEnabled()) return true;

        Location location = target.getLocation();

        Island island = SuperiorSkyblockAPI.getIslandAt(location) == null ? SuperiorSkyblockAPI.getSpawnIsland() : SuperiorSkyblockAPI.getIslandAt(location);

        return island == null || island.hasPermission(player, IslandPrivilege.getByName("INTERACT")) || island.hasPermission(player, IslandPrivilege.getByName("USE"));
    }

    @Override
    public boolean canDamage(Entity damager, Entity target) {
        if (!isEnabled()) return true;
        if (!(damager instanceof Player attacker)) return true;

        Location location = target.getLocation();

        Island island = SuperiorSkyblockAPI.getIslandAt(location) == null ? SuperiorSkyblockAPI.getSpawnIsland() : SuperiorSkyblockAPI.getIslandAt(location);

        return island == null || island.hasPermission(attacker, IslandPrivilege.getByName("INTERACT"));
    }
}