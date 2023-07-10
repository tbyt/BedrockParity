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
     * 
     *  Minecraft Version 1.16.1 - 1.19.4
     */
    @EventHandler
    public void onPrepareSmithing(PrepareSmithingEvent event)
    {
    	Player player = (Player) event.getViewers().get(0);
    	if (!FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId())) {
    		return;
    	}

    	//slot 0 is the InputEquipment, slot 1 is the InputMineral. Friendly for older server versions.
    	ItemStack slot1 = event.getInventory().getItem(0);
    	ItemStack slot2 = event.getInventory().getItem(1);
    	if (slot1 != null && slot2==null)
    	{
    		if (slot1.getType().equals(Material.NETHERITE_INGOT))
            {
    			// Swap Slots.
    			event.getInventory().setItem(0,null);
    			event.getInventory().setItem(1,slot1);
    			return;
            }
    	}
    }
}