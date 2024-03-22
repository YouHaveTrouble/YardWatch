package me.youhavetrouble.yardwatch.hooks;

import com.griefcraft.lwc.LWC;
import com.griefcraft.lwc.LWCPlugin;
import me.youhavetrouble.yardwatch.Protection;
import me.youhavetrouble.yardwatch.YardWatch;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class LWCXProtection implements Protection {

    private final YardWatch plugin;

    public LWCXProtection(YardWatch plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isEnabled() {
        return plugin.getServer().getPluginManager().isPluginEnabled("LWC");
    }

    @Override
    public boolean isProtected(Location location) {
        if (!isEnabled()) return false;
        return LWCPlugin.getPlugin(LWCPlugin.class).getLWC().findProtection(location) != null;
    }

    @Override
    public boolean canBreakBlock(Player player, BlockState blockState) {
        if (!isEnabled()) return true;
        return LWCPlugin.getPlugin(LWCPlugin.class)
                .getLWC()
                .canAccessProtection(
                        player,
                        blockState.getX(),
                        blockState.getY(),
                        blockState.getZ()
                );
    }

    @Override
    public boolean canPlaceBlock(Player player, Location location) {
        return true;
    }

    @Override
    public boolean canInteract(Player player, BlockState blockState) {
        if (!isEnabled()) return true;
        return LWCPlugin.getPlugin(LWCPlugin.class)
                .getLWC()
                .canAccessProtection(
                        player,
                        blockState.getX(),
                        blockState.getY(),
                        blockState.getZ()
                );
    }

    @Override
    public boolean canInteract(Player player, Entity target) {
        if (!isEnabled()) return true;
        LWC lwc = LWCPlugin.getPlugin(LWCPlugin.class).getLWC();
        if (!lwc.isProtectable(target.getType())) return true;
        // the following is extraction of lwcx internal logic. I have no clue what's happening here.
        int a = 50000 + target.getUniqueId().hashCode();
        com.griefcraft.model.Protection protection = lwc.getPhysicalDatabase()
                .loadProtection(target.getWorld().getName(), a, a, a);
        if (protection == null) return true;
        return lwc.canAccessProtection(player, protection);
    }

    @Override
    public boolean canDamage(Entity damager, Entity target) {
        if (!isEnabled()) return true;
        LWC lwc = LWCPlugin.getPlugin(LWCPlugin.class).getLWC();
        if (!lwc.isProtectable(target.getType())) return true;
        // the following is extraction of lwcx internal logic. I have no clue what's happening here.
        int a = 50000 + target.getUniqueId().hashCode();
        com.griefcraft.model.Protection protection = lwc.getPhysicalDatabase()
                .loadProtection(target.getWorld().getName(), a, a, a);
        if (protection == null) return true;
        if (damager instanceof Player player) {
            return lwc.canAccessProtection(player, protection);
        }
        return false;
    }
}
