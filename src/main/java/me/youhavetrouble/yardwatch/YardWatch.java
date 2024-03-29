package me.youhavetrouble.yardwatch;

import me.youhavetrouble.yardwatch.hooks.FactionsUUIDProtection;
import me.youhavetrouble.yardwatch.hooks.GriefPreventionProtection;
import me.youhavetrouble.yardwatch.hooks.LWCXProtection;
import me.youhavetrouble.yardwatch.hooks.SuperiorSkyBlockProtection;
import me.youhavetrouble.yardwatch.hooks.WorldGuardProtection;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class YardWatch extends JavaPlugin {

    @Override
    public void onEnable() {
        if (shouldRegisterService("WorldGuard")) {
            getServer().getServicesManager().register(
                    Protection.class, new WorldGuardProtection(this), this, ServicePriority.Normal
            );
        }

        if (shouldRegisterService("GriefPrevention")) {
            getServer().getServicesManager().register(
                    Protection.class, new GriefPreventionProtection(this), this, ServicePriority.Normal
            );
        }

        if (shouldRegisterService("LWC")) {
            getServer().getServicesManager().register(
                    Protection.class, new LWCXProtection(this), this, ServicePriority.Normal
            );
        }

        if (shouldRegisterService("Factions")) {
            getServer().getServicesManager().register(
                    Protection.class, new FactionsUUIDProtection(this), this, ServicePriority.Normal
            );
        }

        if (shouldRegisterService("SuperiorSkyblock2")) {
            getServer().getServicesManager().register(
                    Protection.class, new SuperiorSkyBlockProtection(this), this, ServicePriority.Normal
            );
        }

        if (shouldRegisterService("Towny")) {
            getServer().getServicesManager().register(
                    Protection.class, new TownyProtection(this), this, ServicePriority.Normal
            );
        }

    }

    /**
     * Determines whether a service should be registered for a given plugin.
     * @param pluginName The name of the plugin
     * @return True if the service should be registered, false otherwise
     */
    private boolean shouldRegisterService(String pluginName) {
        Plugin plugin = getServer().getPluginManager().getPlugin(pluginName);
        if (plugin == null || !plugin.isEnabled()) return false;
        List<RegisteredServiceProvider<?>> serviceProviders = getServer().getServicesManager().getRegistrations(plugin);
        for (RegisteredServiceProvider<?> serviceProvider : serviceProviders) {
            if (serviceProvider.getService() == Protection.class) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onDisable() {
        getServer().getServicesManager().unregisterAll(this);
    }
}
