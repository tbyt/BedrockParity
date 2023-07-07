package com.tbyt;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.geysermc.floodgate.api.FloodgateApi;

public final class ViaVersionLegacySmithing implements Listener {
    private final Plugin plugin;

    public ViaVersionLegacySmithing(Plugin plugin) {
        this.plugin = plugin;
    }

    /*
     * Made by TBYT
     */
    @EventHandler
    public void onPrepareSmithing(PrepareSmithingEvent event)
    {
    	Player player = (Player) event.getViewers().get(0);
    	if (!FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId())) {
    		return;
    	}

    	if (event.getInventory().getInputEquipment() != null && event.getInventory().getInputMineral() == null) 
    	{
    		ItemStack netherite_ingot = new ItemStack(Material.NETHERITE_INGOT,1);
    		if(player.getInventory().contains(Material.NETHERITE_INGOT))
    		{
    			player.getInventory().removeItem(netherite_ingot);
    			event.getInventory().setInputMineral(netherite_ingot);
    		}
    	}
    }
}