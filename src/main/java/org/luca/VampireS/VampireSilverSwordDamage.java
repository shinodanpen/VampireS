package org.luca.VampireS;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

public class VampireSilverSwordDamage implements Listener, Runnable {
	
	private final MainClass plugin;
	
	public VampireSilverSwordDamage(MainClass plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onVampireHitBySilverSword (EntityDamageByEntityEvent e) {
		if(!(e.getDamager() instanceof Player)) {
			return;
		}
		
		Player damager = (Player) e.getDamager();
		Entity damaged = e.getEntity();
		if(!(plugin.getVampires().contains(damager.getUniqueId())) && plugin.isSilverSword(damager.getInventory().getItemInMainHand())) {			
			if(damaged instanceof Player && plugin.getVampires().contains(damaged.getUniqueId())) {
				e.setDamage(e.getDamage() * 3);
				damaged.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("silversword.vampirehit", "&cAghn-! It's burning my skin! &4&o(Enemy Player is using a Silver Sword, inflicting triple damage! )")));
				return;
			}
			
			if(e.getCause() == DamageCause.ENTITY_SWEEP_ATTACK || e.getCause() == DamageCause.ENTITY_ATTACK) {
				e.setCancelled(true);
			}
		}
	}


	/**
	 * All vampire players holding a SilverSword in their inventory item will be forced to drop it.
	 */
	@Override
	public void run() {		
		for(UUID uuid : plugin.getVampires()) {
			Player vampire = Bukkit.getPlayer(uuid);
			if(vampire == null) {
				continue;
			}

			ItemStack offHand = vampire.getInventory().getItemInOffHand();
			if(plugin.isSilverSword(offHand)) {
				vampire.getInventory().setItemInOffHand(null);
				this.dropItem(vampire, offHand);
			}
			
			for(ItemStack item : vampire.getInventory().getContents()) {
				if(plugin.isSilverSword(item)) {
					vampire.getInventory().remove(item);
					this.dropItem(vampire, item);
				}
			}
		}
	}
	
	private void dropItem(Player vampire, ItemStack item) {
		Bukkit.getScheduler().runTask(plugin, () -> {
			Item drop = vampire.getWorld().dropItemNaturally(vampire.getLocation(), item);
			drop.setPickupDelay(40);
			vampire.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("silversword.vampiredropsilversword", "&eAh-! That hurts! &e&o(Silver burns a Vampire's skin. Item Dropped.)")));
		});
	}

}
