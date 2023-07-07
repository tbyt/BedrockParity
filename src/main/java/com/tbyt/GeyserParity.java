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
		String MinecraftVersion = Bukkit.getMinecraftVersion();
		
		if (playerChecker != null) {
			if (config.sweepingEdgeBookAnvilFix()) 
			{
				Bukkit.getPluginManager().registerEvents(new SweepingEdgeFix(this), this);
				getLogger().info("Sweeping Edge Book Fix in Anvil is enabled.");
			}
			else
				getLogger().info("Sweeping Edge Book Fix in Anvil is disabled.");
			
			// to understand this If Block as a non-programmer, reference: https://www.javatpoint.com/java-string-charat
			// "Successful if server is between 1.10 and up to 1.19.4. As long as it is before 1.20."
			if(MinecraftVersion.charAt(0)=='1'&&MinecraftVersion.charAt(2)=='1'&&MinecraftVersion.charAt(4)=='.')
			{
				if (config.viaVersionLegacySmithing()) 
				{
					Bukkit.getPluginManager().registerEvents(new ViaVersionLegacySmithing(this), this);
					getLogger().info("ViaVersion Legacy Smithing is enabled.");
				}
				else
					getLogger().info("ViaVersion Legacy Smithing is disabled.");
			}
			else
			{
				getLogger().info("ViaVersion Legacy Smithing Disabled. Reason: Minecraft Server Version is not between 1.10 and 1.19.4 to apply this.");
			}
		}
	}
}
