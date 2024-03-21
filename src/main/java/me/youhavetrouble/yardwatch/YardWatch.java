package me.youhavetrouble.yardwatch;

import me.youhavetrouble.yardwatch.hooks.GriefPreventionProtection;
import me.youhavetrouble.yardwatch.hooks.WorldGuardProtection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class YardWatch extends JavaPlugin {

    @Override
    public void onEnable() {
        PluginManager pluginManager = getServer().getPluginManager();

        if (pluginManager.isPluginEnabled("WorldGuard")) {
            getServer().getServicesManager().register(
                    Protection.class, new WorldGuardProtection(this), this, ServicePriority.Normal
            );
        }

        if (pluginManager.isPluginEnabled("GriefPrevention")) {
            getServer().getServicesManager().register(
                    Protection.class, new GriefPreventionProtection(this), this, ServicePriority.Normal
            );
        }

    }
}
