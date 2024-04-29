package com.tbyt;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

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
			if(this.getConfig().getBoolean("sweeping-edge-book-anvil-fix"))
			{
				Bukkit.getPluginManager().registerEvents(new SweepingEdgeFix(this), this);
				getLogger().info("Sweeping Edge Book Fix in Anvil is enabled.");
			}
			else
				getLogger().info("Sweeping Edge Book Fix in Anvil is manually disabled.");
			if(this.getConfig().getInt("animate-head-blocks-distance")!=0&&Bukkit.getPluginManager().getPlugin("Geyser-Spigot")!=null)
			{
				getLogger().info("Animating Dragon and Piglin Heads is enabled.");
				int animateHeadBlockDistance = this.getConfig().getInt("animate-head-blocks-distance");
				AnimateHeadsForGeyser animateHeadsForGeyser = new AnimateHeadsForGeyser(this, animateHeadBlockDistance);
				Bukkit.getPluginManager().registerEvents(animateHeadsForGeyser, this);
				BukkitScheduler scheduler = getServer().getScheduler();
				scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
					@Override
					public void run() {
					//Player player : Bukkit.getOnlinePlayers()
					    for (FloodgatePlayer player : FloodgateApi.getInstance().getPlayers()) {
					        // Update every 4 ticks
					            animateHeadsForGeyser.animateForBedrockPlayer(player);
				            }
				        }
				    }, 0L, 4L);
				}
			else
				getLogger().info("Animating Heads for Bedrock Players is manually disabled, or Geyser-Spigot could not be found.");
//				getLogger().info("Animating Heads for Bedrock Players is disabled in BedrockParity because Geyser natively supports it Minecraft Java 1.20.2 and up.");
		}
	}
}
