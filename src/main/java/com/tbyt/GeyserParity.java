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
                .defaultOptions(opts -> opts.header("Geyser "))
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
				getLogger().warning("Could not find Geyser or Floodgate; sweeping edge fix will not be applied.");
				playerChecker = null;
			}
		}
		if (playerChecker != null) {
			if (config.sweepingEdgeFix()) {
				Bukkit.getPluginManager().registerEvents(new SweepingEdgeFix(this), this);
				getLogger().info("Sweeping Edge fix enabled.");
			}
			if (config.bannerOnShieldFix()) {
				//Bukkit.getPluginManager().registerEvents(new BannerShieldFix(this), this);
				getLogger().info("Banner Shields fix enabled.");
			}
		}
	}
}
