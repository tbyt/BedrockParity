package com.tbyt;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.geyser.GeyserImpl;
import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.geyser.api.event.EventRegistrar;
import org.geysermc.geyser.api.event.bedrock.SessionJoinEvent;
import org.geysermc.api.Geyser;
import org.geysermc.api.connection.Connection;

import java.util.UUID;
import java.util.function.Predicate;

public class BedrockParity extends JavaPlugin implements EventRegistrar {
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
				//https://wiki.geysermc.org/geyser/events/
				//commented out because floodgate currently breaks this. Have to manually subscribe instead of using @subscribe
				//GeyserApi.api().eventBus().register(this, this);
				GeyserApi.api().eventBus().subscribe(this, SessionJoinEvent.class, this::onGeyserPlayerJoin);
				getLogger().info("Animating Heads for Bedrock Players is enabled.");
			}
			else
				getLogger().info("Animating Heads for Bedrock Players is manually disabled.");
		}
	}
    
    public void onGeyserPlayerJoin(SessionJoinEvent event) {
    	int animateHeadBlockDistance = this.getConfig().getInt("animate-head-blocks-distance");
        if((this.getConfig().getInt("animate-head-blocks-distance")!=0) && playerChecker != null)
		{
        	AnimateHeadsForGeyser animateHeadsForGeyser = new AnimateHeadsForGeyser(this, animateHeadBlockDistance, Bukkit.getViewDistance());
			//there are no server events needing to be registered at this time in the AnimateHeadsForGeyser class.
			Bukkit.getPluginManager().registerEvents(animateHeadsForGeyser, this);
	        BukkitScheduler scheduler = getServer().getScheduler();
	        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
	            @Override
	            public void run() {
	                for (Connection playerConn : Geyser.api().onlineConnections()) {
						// Update every 4 ticks
	                	animateHeadsForGeyser.animateForGeyserPlayer(playerConn);
	                }
	            }
	        }, 0L, 4L);
    	}
    }
}
