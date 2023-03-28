package com.tbyt;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.geysermc.floodgate.api.FloodgateApi;

import net.kyori.adventure.text.Component;

public final class SweepingEdgeFix implements Listener {
    private final Plugin plugin;
    private final Boolean anvilBookFix;

    public SweepingEdgeFix(Plugin plugin, Boolean anvilBookFix) {
        this.plugin = plugin;
        this.anvilBookFix = anvilBookFix;
    }

    /*
     * TBYT adds Sweeping Edge fix. This will update lore to sweeping edge and the
     * enchantment level. (Jens helped with Lore). Unbreaking 1 may be applied to
     * the Anvil Result in the event of a sweeping edge only book in the 2nd anvil
     * slot.
     */
    @EventHandler
    public void findEnchant(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (!FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId())) {
            return;
        }
        
        if (event.getClickedInventory() == null) {
            return;
        }

        ItemStack item = event.getCurrentItem();
        if (item == null) {
            return;
        }
        // all players must be checked for modifiedanvilbook tag, even if AnvilBookFix is disabled.
        
        if (item.getType().equals(Material.ENCHANTED_BOOK)) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
            if (meta.hasStoredEnchant(Enchantment.SWEEPING_EDGE) && meta.hasItemFlag(ItemFlag.HIDE_DYE) && meta.hasStoredEnchant(Enchantment.DURABILITY)) {
               
                meta.removeStoredEnchant(Enchantment.DURABILITY);
                meta.removeItemFlags(ItemFlag.HIDE_DYE);
                item.setItemMeta(meta);
                event.setCurrentItem(item);
            }
        }
        //else if(item.getType().toString().indexOf("SWORD")!=-1)
//        else if (item.hasItemMeta())
//        {
//        	  //player.getServer().broadcastMessage(String.valueOf(item.getItemMeta().hasItemFlag(ItemFlag.HIDE_DYE)));
//              ItemMeta meta = item.getItemMeta();
//              if (meta.hasEnchant(Enchantment.SWEEPING_EDGE) && meta.hasItemFlag(ItemFlag.HIDE_DYE) && meta.hasEnchant(Enchantment.DURABILITY))
//              {
//            	  meta.removeEnchant(Enchantment.DURABILITY);
//                  meta.removeItemFlags(ItemFlag.HIDE_DYE);
//                  item.setItemMeta(meta);
//                  event.setCurrentItem(item);
//              }
//        }
        
        //redact
//        if (event.getClickedInventory().getType() == InventoryType.PLAYER) {
//            item = event.getCurrentItem();
//
//            if (item == null) {
//                return;
//            }
//
//            if (item.getType().equals(Material.ENCHANTED_BOOK)) {
//                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
//                if (meta.hasStoredEnchant(Enchantment.SWEEPING_EDGE)) {
//                    int sweepingLevel = meta.getStoredEnchantLevel(Enchantment.SWEEPING_EDGE);
//                    // will overwrite any existing lore.
//                    List<Component> loreList = new ArrayList<>(); 
//                    loreList.add(Component.text("Sweeping Edge " + sweepingLevel));
//                    meta.lore(loreList);
//                } else {
//                    meta.lore(new ArrayList<>());
//                }
//
//                item.setItemMeta(meta);
//                event.setCurrentItem(item);
//            } else if (item.hasItemMeta()) {
//                ItemMeta meta = item.getItemMeta();
//                if (meta.hasEnchant(Enchantment.SWEEPING_EDGE)) {
//                    int sweepingLevel = meta.getEnchantLevel(Enchantment.SWEEPING_EDGE);
//                    // will overwrite any existing lore.
//                    List<Component> loreList = new ArrayList<>();
//                    loreList.add(Component.text("Sweeping Edge " + sweepingLevel));
//                    meta.lore(loreList);
//                } else {
//                    meta.lore(new ArrayList<>());
//                }
//
//                item.setItemMeta(meta);
//                event.setCurrentItem(item);
//            }
//        }
    }

    /*
     * https://bukkit.org/threads/how-to-put-unsafe-enchantments-to-result-item-in-
     * anvil.412472/#post-3350913
     */

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) 
    {
    	//If they enabled this fix in the config.
    	if(anvilBookFix)
    	{
	        Player player = (Player) event.getViewers().get(0);
	        if (!FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId())) {
	            return;
	        }
	        
	        ItemStack secondItem = event.getInventory().getSecondItem();
	        if (event.getInventory().getFirstItem() != null && secondItem != null) {
	            if (secondItem.getType() == Material.ENCHANTED_BOOK) {
	                EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta) secondItem.getItemMeta();
	                //If the enchanted book has another enchant than sweeping edge, you would not need this fix.
	                if (bookMeta.hasStoredEnchant(Enchantment.SWEEPING_EDGE) && bookMeta.getStoredEnchants().size() == 1) {
	                	//
	                	bookMeta.addItemFlags(ItemFlag.HIDE_DYE);
	                    bookMeta.addStoredEnchant(Enchantment.DURABILITY, 1, false);
	                    secondItem.setItemMeta(bookMeta);
	                    event.getInventory().setSecondItem(secondItem);
	                }
	            }
	        }
	    }
    }
}