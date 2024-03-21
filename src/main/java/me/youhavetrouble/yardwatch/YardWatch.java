package me.youhavetrouble.yardwatch;

import me.youhavetrouble.yardwatch.hooks.WorldGuardProtection;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class YardWatch extends JavaPlugin {

    @Override
    public void onEnable() {

            if (getServer().getPluginManager().isPluginEnabled("WorldGuard")) {
                getServer().getServicesManager().register(
                        Protection.class, new WorldGuardProtection(this), this, ServicePriority.Normal
                );
            }


    }
}
