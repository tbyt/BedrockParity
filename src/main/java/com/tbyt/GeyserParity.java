package com.tbyt;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.geyser.GeyserImpl;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.util.UUID;
import java.util.function.Predicate;

public final class GeyserParity extends JavaPlugin {

    @Override
    public void onEnable() {
        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .path(getDataFolder().toPath().resolve("geyserparity.conf"))
                .defaultOptions(opts -> opts.header("Geyser Parity"))
                .build();

        final GeyserParityConfiguration config;
        try {
            final CommentedConfigurationNode node = loader.load();
            config = node.get(GeyserParityConfiguration.class);
            loader.save(node);
        } catch (ConfigurateException e) {
            getLogger().warning("Could not load config!");
            e.printStackTrace();
            return;
        }

		Predicate<UUID> playerChecker;
		try {
			Class.forName("org.geysermc.floodgate.api.FloodgateApi");
			playerChecker = uuid -> FloodgateApi.getInstance().isFloodgatePlayer(uuid);
		} catch (ClassNotFoundException e) {
			try {
				Class.forName("org.geysermc.geyser.GeyserImpl");
				playerChecker = uuid -> GeyserImpl.getInstance().connectionByUuid(uuid) != null;
			} catch (ClassNotFoundException e2) {
				getLogger().warning("Could not find Geyser or Floodgate; Geyser Parity fixes will not be enabled.");
				playerChecker = null;
			}
		}
		if (playerChecker != null) {
			if (config.sweepingEdgeLore()) 
			{
				Bukkit.getPluginManager().registerEvents(new SweepingEdgeFix(this,config.sweepingEdgeAnvilBookFix()), this);
				getLogger().info("Sweeping Edge Lore is enabled.");
				if(config.sweepingEdgeAnvilBookFix())
					getLogger().info("Sweeping Edge Book Fix in Anvil is enabled.");
				else
					getLogger().info("Sweeping Edge Book Fix in Anvil is disabled.");
			}
			else getLogger().info("Sweeping Edge Fixes are is disabled.");
			/*
			 * if (config.bannerDetachFromShield()) {
			 * Bukkit.getPluginManager().registerEvents(new BannerDetachFromShield(this),
			 * this); getLogger().info("Banner detach from Shield Feature is enabled."); }
			 * else getLogger().info("Banner detach from Shield Feature is disabled.");
			 */
		}
	}
}
