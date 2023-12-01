package com.tbyt;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.geysermc.api.connection.Connection;
import org.geysermc.floodgate.api.FloodgateApi;
import java.util.UUID;
import java.util.function.Predicate;
import org.geysermc.api.Geyser;
import org.geysermc.geyser.GeyserImpl;

public class BedrockParity extends JavaPlugin {
	private Predicate<UUID> playerChecker;
    @Override
    public void onEnable() {
        FileConfiguration config = getConfig();
        config.addDefault("sweeping-edge-book-anvil-fix", true);
        config.addDefault("animate-heads-for-bedrock", true);
        config.options().copyDefaults(true);
        saveDefaultConfig();

		//Predicate<UUID> playerChecker;
		try {
			Class.forName("org.geysermc.floodgate.api.FloodgateApi");
			playerChecker = uuid -> FloodgateApi.getInstance().isFloodgatePlayer(uuid);
		} catch (ClassNotFoundException e) {
			try {
				Class.forName("org.geysermc.geyser.GeyserImpl");
				playerChecker = uuid -> GeyserImpl.getInstance().connectionByUuid(uuid) != null;
			} catch (ClassNotFoundException e2) {
				getLogger().warning("Could not find Geyser or Floodgate; Bedrock Parity is disabled.");
				playerChecker = null;
			}
		}
		
		if (playerChecker != null) {
			if(this.getConfig().getBoolean("sweeping-edge-book-anvil-fix"))
			{
				Bukkit.getPluginManager().registerEvents(new SweepingEdgeFix(this), this);
				getLogger().info("Sweeping Edge Book Fix in Anvil is enabled.");
			}
			else
				getLogger().info("Sweeping Edge Book Fix in Anvil is manually disabled.");
			if(this.getConfig().getInt("animate-head-blocks-distance")!=0)
			{
				getLogger().info("Animating Heads for Bedrock Players is enabled.");
				int animateHeadBlockDistance = this.getConfig().getInt("animate-head-blocks-distance");
				AnimateHeadsForGeyser animateHeadsForGeyser = new AnimateHeadsForGeyser(this, animateHeadBlockDistance, Bukkit.getViewDistance());
				Bukkit.getPluginManager().registerEvents(animateHeadsForGeyser, this);
				BukkitScheduler scheduler = getServer().getScheduler();
			    scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
			            @Override
			            public void run() {
			            	//Player player : Bukkit.getOnlinePlayers()
			                for (Connection playerConn : Geyser.api().onlineConnections()) {
			                	// Update every 4 ticks
			                	animateHeadsForGeyser.animateForGeyserPlayer(playerConn);
			                }
			            }
			        }, 0L, 4L);
			}
			else
				getLogger().info("Animating Heads for Bedrock Players is manually disabled.");
		}
	}
}
