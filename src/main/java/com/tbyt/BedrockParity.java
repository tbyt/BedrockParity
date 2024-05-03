package com.tbyt;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.geysermc.floodgate.api.FloodgateApi;
import java.util.UUID;
import java.util.function.Predicate;

public class BedrockParity extends JavaPlugin {
	private Predicate<UUID> playerChecker;

	@Override
	public void onEnable() {
		FileConfiguration config = getConfig();
		config.addDefault("sweeping-edge-book-anvil-fix", true);
		config.addDefault("animate-heads-for-bedrock", true);
		config.options().copyDefaults(true);
		saveDefaultConfig();
		try {
			Class.forName("org.geysermc.floodgate.api.FloodgateApi");
			playerChecker = uuid -> FloodgateApi.getInstance().isFloodgatePlayer(uuid);
		} catch (ClassNotFoundException e) {
			getLogger().warning("Could not find Floodgate; Bedrock Parity is disabled.");
		}
		if (playerChecker != null) {
			if (this.getConfig().getBoolean("sweeping-edge-book-anvil-fix")) {
				Bukkit.getPluginManager().registerEvents(new SweepingEdgeFix(this), this);
				getLogger().info("Sweeping Edge Book Fix in Anvil is enabled.");
			} else
				getLogger().info("Sweeping Edge Book Fix in Anvil is manually disabled.");
			if (Bukkit.getUnsafe().getDataVersion() < 3466) {
				if (Bukkit.getPluginManager().getPlugin("Geyser-Spigot") != null) {
					int animateHeadBlockDistance = this.getConfig().getInt("animate-head-blocks-distance");
					if (animateHeadBlockDistance != 0) {
						new BedrockEvents(animateHeadBlockDistance, this);
						getLogger().info("Animating Dragon and Piglin Heads is enabled.");
						
					} else
						getLogger().info("Animating Heads for Bedrock Players is manually disabled in config.yml");
				} else
					getLogger().info("Geyser-Spigot could not be found. Animating Heads for Bedrock Players is disabled.");
			} else
				getLogger().info("Animating Heads for Bedrock Players is disabled because Geyser natively supports it Minecraft Java v1.20.2 and up.");
		}
	}
}
