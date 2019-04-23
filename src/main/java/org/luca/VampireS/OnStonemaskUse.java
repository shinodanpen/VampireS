package org.luca.VampireS;

import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;

import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class OnStonemaskUse implements Listener {	
	
	private final MainClass plugin;
	
	public OnStonemaskUse(MainClass plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onStonemaskSwap(InventoryClickEvent e) {
		if(!plugin.getConfig().getBoolean("stonemask.use")) {
			return; // If it's false the plugin doesn't execute the function
		}
		
		Player player = (Player)e.getWhoClicked();
		ItemStack item = e.getCurrentItem();
		ItemStack cItem = e.getCursor();
		
		if(e.getAction() == InventoryAction.PLACE_ALL
				&& e.getSlotType() == SlotType.ARMOR 
				&& e.getSlot() == 39 
				&& plugin.isMask(cItem)) {
			e.setResult(Result.DENY);
			player.getInventory().setHelmet(cItem);
			player.setItemOnCursor(null);
		}

		if(e.getAction() == InventoryAction.NOTHING
				&& e.getSlotType() == SlotType.ARMOR 
				&& e.getSlot() == 39 
				&& plugin.isMask(cItem)) {
			e.setResult(Result.DENY);
			ItemStack helmet = player.getInventory().getHelmet();
			player.getInventory().setHelmet(cItem);
			player.setItemOnCursor(helmet);
		}
		
		if(e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY 
				&& e.getSlotType() != SlotType.ARMOR
				&& player.getInventory().getHelmet() == null
				&& plugin.isMask(item)) {
			e.setResult(Result.DENY);
			player.getInventory().setHelmet(item);
			e.getClickedInventory().setItem(e.getSlot(), null);
		}
	}
	
	@EventHandler
	public void onStonemaskEquip(PlayerInteractEvent e) {	
		if(!plugin.getConfig().getBoolean("stonemask.use")) {
			return; // If it's false the plugin doesn't execute the function
		}
		Player player = e.getPlayer();	
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			ItemStack item = player.getInventory().getItemInMainHand();
			if(plugin.isMask(item)) {
				ItemStack helmet = player.getInventory().getHelmet();
				if(helmet == null) { // Set as helmet only if the helmet slot is empty
					player.getInventory().setHelmet(item);
					player.getInventory().setItemInMainHand(null);
				}
			}
		}
	}

}
