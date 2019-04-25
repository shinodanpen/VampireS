package org.luca.VampireS;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.google.common.collect.ImmutableList;

public class VampireStopTimeAbility implements Listener, Runnable {
	
	private final MainClass plugin;

	public VampireStopTimeAbility(MainClass plugin) {
		this.plugin = plugin;
	}
	
	private List<UUID> affectedPlayers = new ArrayList<>();
	
	private List<UUID> unaffectedPlayers = new ArrayList<>();
	
	List<Material> noBlocks = ImmutableList.of(Material.ACACIA_BUTTON, Material.BIRCH_BUTTON, Material.DARK_OAK_BUTTON,
			Material.JUNGLE_BUTTON, Material.OAK_BUTTON, Material.SPRUCE_BUTTON, Material.STONE_BUTTON, Material.ACACIA_TRAPDOOR,
			Material.BIRCH_TRAPDOOR, Material.DARK_OAK_TRAPDOOR, Material.IRON_TRAPDOOR, Material.JUNGLE_TRAPDOOR, Material.OAK_TRAPDOOR,
			Material.OAK_TRAPDOOR, Material.SPRUCE_TRAPDOOR, Material.ACACIA_DOOR, Material.BIRCH_DOOR, Material.DARK_OAK_DOOR, Material.IRON_DOOR,
			Material.JUNGLE_DOOR, Material.OAK_DOOR, Material.SPRUCE_DOOR, Material.LEVER, Material.FURNACE, Material.CHEST, Material.CRAFTING_TABLE,
			Material.DROPPER, Material.DISPENSER, Material.DAYLIGHT_DETECTOR, Material.ANVIL, Material.CHIPPED_ANVIL, Material.DAMAGED_ANVIL,
			Material.BEACON, Material.COMPARATOR, Material.COMMAND_BLOCK, Material.HOPPER, Material.TRAPPED_CHEST, Material.ENCHANTING_TABLE,
			Material.ENDER_CHEST, Material.END_PORTAL_FRAME, Material.END_GATEWAY, Material.ITEM_FRAME, Material.FLOWER_POT, Material.SIGN,
			Material.ARMOR_STAND, Material.BLACK_BED, Material.BLUE_BED, Material.BROWN_BED, Material.CYAN_BED, Material.GRAY_BED, Material.GREEN_BED,
			Material.LIGHT_BLUE_BED, Material.LIGHT_GRAY_BED, Material.LIME_BED, Material.MAGENTA_BED, Material.ORANGE_BED, Material.PINK_BED,
			Material.PURPLE_BED, Material.RED_BED, Material.WHITE_BED, Material.YELLOW_BED, Material.JUKEBOX, Material.NOTE_BLOCK,
			Material.REPEATER, Material.CHAIN_COMMAND_BLOCK, Material.REPEATING_COMMAND_BLOCK, Material.TURTLE_EGG, Material.CONDUIT,
			Material.SHULKER_BOX, Material.BLACK_SHULKER_BOX, Material.BLUE_SHULKER_BOX, Material.BROWN_SHULKER_BOX, Material.CYAN_SHULKER_BOX,
			Material.GRAY_SHULKER_BOX, Material.GREEN_SHULKER_BOX, Material.LIGHT_BLUE_SHULKER_BOX, Material.LIGHT_GRAY_SHULKER_BOX, Material.LIME_SHULKER_BOX,
			Material.MAGENTA_SHULKER_BOX, Material.ORANGE_SHULKER_BOX, Material.PINK_SHULKER_BOX, Material.PURPLE_SHULKER_BOX, Material.RED_SHULKER_BOX,
			Material.WHITE_SHULKER_BOX, Material.YELLOW_SHULKER_BOX, Material.CAKE, Material.CAULDRON, Material.BREWING_STAND, Material.TRIPWIRE_HOOK,
			Material.TRIPWIRE);
	
	private List<UUID> stoptimeCooldown = new ArrayList<>();

	@EventHandler
	public void StopTimeAbility (PlayerInteractEvent e) {
		if(!plugin.getConfig().getBoolean("stoptime.enable")) {
			return;
		}
		
		if(!(e.getPlayer() instanceof Player)) {
			return;
		}

		Player p = e.getPlayer();
		UUID id = p.getUniqueId();
		if(!plugin.getVampires().contains(id) || !p.isSneaking()
				|| !(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
				|| !plugin.isStopTimeClock(p.getInventory().getItemInMainHand())) {
			return;
		}
		
		if(e.getClickedBlock() != null && noBlocks.contains(e.getClickedBlock().getType())) {
			return;
		}
		
		if(stoptimeCooldown.contains(id)) {
			// No cooldown for this player, he is immune
			if(plugin.noStoptimeCooldown().contains(id)) {
				stoptimeCooldown.remove(id);
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("stoptime.vampireready",
						"&eYou can now stop time.")));
				return;
			}
			
			return;
		}
		
		// Add player to list
		stoptimeCooldown.add(id);
		unaffectedPlayers.add(id);
		
		// Get nearby entities
		Collection<Entity> entities = p.getWorld().getNearbyEntities(p.getLocation(), 20D, 20D, 20D);
		
		// Player sound & message to vampire
		p.playSound(p.getLocation(), "vampires.stoptime", SoundCategory.MASTER, 0.3F, 1.0F);
		p.sendMessage(" ");
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("stoptime.vampiremessage",
				"&eYou have stopped time! &7&o(All entities in a radius of 20 blocks except you can not move for 10 seconds from now.)")));
		p.sendMessage(" ");
		// Freeze players & entities (send message & play sound)
		for(Entity en : entities) {
			if (en instanceof Player && !unaffectedPlayers.contains(en.getUniqueId())) {
				if(id.equals(en.getUniqueId())) { // The scanned player is the same who is activating time stop ability... do nothing (skip)
					continue;
				}
				
				Player other = (Player) en;
				other.playSound(other.getLocation(), "vampires.stoptime", SoundCategory.MASTER, 0.3F, 1.0F);
				if(plugin.getVampires().contains(other.getUniqueId())) {
					unaffectedPlayers.add(other.getUniqueId());
					other.sendMessage(" ");
					other.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("stoptime.vampirenotaffected.initialmessage",
							"&eTime has been stopped, but you are not affected! &7&o(Vampires are not affected by &6&lStop Time&7&o)")));
					other.sendMessage(" ");
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
						unaffectedPlayers.remove(other.getUniqueId());
						other.sendMessage(" ");
						other.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("stoptime.vampirenotaffected.finalmessage",
								"&6&lStop Time &e effect has ended.")));
						other.sendMessage(" ");

						p.sendMessage(" ");
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("stoptime.vampireendmessage",
								"&eTime has started again. &7&o(All entities in a radius of 20 blocks except you can now act freely.)")));
						p.sendMessage(" ");
					}, 220L);
					return;
				}
				if(!unaffectedPlayers.contains(other.getUniqueId())) {
					affectedPlayers.add(other.getUniqueId());
					
					// Freeze & message
					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
						other.setInvulnerable(true);
						other.setFlySpeed(0F);
						other.setWalkSpeed(0F);
						other.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 200, 128, false, false, false));
						other.sendMessage(" ");
						other.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("stoptime.playerinitialmessage",
								"&eTime has been stopped! &7&o(You cannot move for 10 seconds from now...)")));
						other.sendMessage(" ");
					}, 20L);
					
				}
				
				// Unfreeze & message
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
					affectedPlayers.remove(other.getUniqueId());
					other.setInvulnerable(false);
					other.setFlySpeed(0.1F);
					other.setWalkSpeed(0.2F);
					other.sendMessage(" ");
					other.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("stoptime.playerfinalmessage",
							"&6&lStop Time &eeffect has ended. &7&o(You can now move again.)")));
					other.sendMessage(" ");
				}, 220L);
				
			} else if(en instanceof LivingEntity) {
				LivingEntity le = (LivingEntity) en;
				
				// Freeze
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
					le.setInvulnerable(true);
					le.setAI(false);
				}, 20L);
				
				// Unfreeze
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
					le.setInvulnerable(false);
					le.setAI(true);
				}, 220L);
			}
		}
		
		// Unfreeze everybody message
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			p.sendMessage(" ");
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("stoptime.vampireendmessage",
					"&eTime has started again. &7&o(All entities in a radius of 20 blocks except you can now act freely.)")));
			p.sendMessage(" ");
		}, 220L);
		
		// Remove player's ability timeout
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			if(p != null && p.isOnline() && stoptimeCooldown.contains(id)) {
//				if(!plugin.noStoptimeCooldown().contains(id)) {
				stoptimeCooldown.remove(id);
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("stoptime.vampireready",
						"&eYou can now stop time.")));
			}
		}, 12000L);
	}
	
	@EventHandler
	public void onPlayerMovementDuringStopTime (PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if(!(unaffectedPlayers.contains(p.getUniqueId()))){
			if(affectedPlayers.contains(p.getUniqueId())) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerToggleFlightDuringStopTime (PlayerToggleFlightEvent e) {
		Player p = e.getPlayer();
		if(!(unaffectedPlayers.contains(p.getUniqueId()))){
			if(affectedPlayers.contains(p.getUniqueId())) {
				e.setCancelled(true);
			}	
		}
	}

	@EventHandler
	public void onPlayerQuitDuringStopTime (PlayerQuitEvent e){
		Player p = e.getPlayer();
		if(affectedPlayers.contains(p.getUniqueId())){
			affectedPlayers.remove(p.getUniqueId());
			p.setWalkSpeed(0.2F);
			p.setFlySpeed(0.1F);
		}
		if(plugin.getVampires().contains(p.getUniqueId()) && unaffectedPlayers.contains(p.getUniqueId())) {
			unaffectedPlayers.remove(p.getUniqueId());
		}
	}
	
	/**
	 * All non vampire players holding a StopTimeClock in their inventory item will be forced to drop it.
	 */
	@Override
	public void run() {		
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(!plugin.getVampires().contains(player.getUniqueId())) {
				ItemStack offHand = player.getInventory().getItemInOffHand();
				if(plugin.isStopTimeClock(offHand)) {
					player.getInventory().setItemInOffHand(null);
					this.dropItem(player, offHand);
				}
				
				for(ItemStack item : player.getInventory().getContents()) {
					if(plugin.isStopTimeClock(item)) {
						player.getInventory().remove(item);
						this.dropItem(player, item);
					}
				}
			}
		}
	}
	
	private void dropItem(Player player, ItemStack item) {
		Bukkit.getScheduler().runTask(plugin, () -> {
			Item drop = player.getWorld().dropItemNaturally(player.getLocation(), item);
			drop.setPickupDelay(40);
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("stoptime.playerdropstoptimeclock", "&eYou don't know how to use this... &7&o(This item is accessible to Vampires only. Item Dropped.)")));
		});
	}
	
}
