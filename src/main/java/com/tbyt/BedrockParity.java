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

public final class BedrockParity extends JavaPlugin {

    @Override
    public void onEnable() {
        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .path(getDataFolder().toPath().resolve("bedrockparity.conf"))
                .defaultOptions(opts -> opts.header("Bedrock Parity for GeyserMC"))
                .build();

        final BedrockParityConfiguration config;
        try {
            final CommentedConfigurationNode node = loader.load();
            config = node.get(BedrockParityConfiguration.class);
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
				getLogger().warning("Could not find Geyser or Floodgate; Bedrock Parity for GeyserMC fixes will not be enabled.");
				playerChecker = null;
			}
		}
		
		if (playerChecker != null) {
			if (config.sweepingEdgeBookAnvilFix()) 
			{
				Bukkit.getPluginManager().registerEvents(new SweepingEdgeFix(this), this);
				getLogger().info("Sweeping Edge Book Fix in Anvil is enabled.");
			}
			else
				getLogger().info("Sweeping Edge Book Fix in Anvil is disabled.");
			
			if (config.viaVersionLegacySmithing()) 
			{
				Bukkit.getPluginManager().registerEvents(new ViaVersionLegacySmithing(this), this);
				getLogger().info("ViaVersion Legacy Smithing is enabled.");
			}
			else
				getLogger().info("ViaVersion Legacy Smithing is disabled.");
		}
	}
}
