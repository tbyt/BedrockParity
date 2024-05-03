package com.tbyt;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.geysermc.geyser.api.GeyserApi;
import org.geysermc.geyser.api.event.EventRegistrar;
import org.geysermc.geyser.api.event.bedrock.SessionJoinEvent;

public class BedrockEvents implements EventRegistrar {

	public int animateHeadBlockDistance = 0;
	private final Plugin plugin;
	
	public BedrockEvents(int animateHeadBlockDistance, Plugin plugin) {
		this.animateHeadBlockDistance = animateHeadBlockDistance;
		this.plugin = plugin;
		GeyserApi.api().eventBus().subscribe(this, SessionJoinEvent.class, this::onGeyserPlayerJoin);
	}
	
	public void onGeyserPlayerJoin(SessionJoinEvent event) {
		AnimateHeadsForGeyser animateHeadsForGeyser = new AnimateHeadsForGeyser(plugin, event.connection(), animateHeadBlockDistance);
		Bukkit.getPluginManager().registerEvents(animateHeadsForGeyser, plugin);
		BukkitScheduler scheduler = plugin.getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				animateHeadsForGeyser.animateForBedrockPlayer();
			}
		}, 0L, 4L);
	}
}
