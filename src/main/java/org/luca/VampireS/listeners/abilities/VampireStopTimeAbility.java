package org.luca.VampireS.listeners.abilities;

import java.util.*;
import java.util.concurrent.TimeUnit;

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
import org.luca.VampireS.VampireSPlugin;
import org.valdi.SuperApiX.bukkit.users.SimpleUser;
import org.valdi.SuperApiX.bukkit.users.User;

public class VampireStopTimeAbility implements Listener, Runnable {
	
	private final VampireSPlugin plugin;

	public VampireStopTimeAbility(VampireSPlugin plugin) {
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
			Material.ENDER_CHEST, Material.END_PORTAL_FRAME, Material.END_GATEWAY, Material.ITEM_FRAME,
			Material.ARMOR_STAND, Material.BLACK_BED, Material.BLUE_BED, Material.BROWN_BED, Material.CYAN_BED, Material.GRAY_BED, Material.GREEN_BED,
			Material.LIGHT_BLUE_BED, Material.LIGHT_GRAY_BED, Material.LIME_BED, Material.MAGENTA_BED, Material.ORANGE_BED, Material.PINK_BED,
			Material.PURPLE_BED, Material.RED_BED, Material.WHITE_BED, Material.YELLOW_BED, Material.JUKEBOX, Material.NOTE_BLOCK,
			Material.REPEATER, Material.CHAIN_COMMAND_BLOCK, Material.REPEATING_COMMAND_BLOCK,
			Material.SHULKER_BOX, Material.BLACK_SHULKER_BOX, Material.BLUE_SHULKER_BOX, Material.BROWN_SHULKER_BOX, Material.CYAN_SHULKER_BOX,
			Material.GRAY_SHULKER_BOX, Material.GREEN_SHULKER_BOX, Material.LIGHT_BLUE_SHULKER_BOX, Material.LIGHT_GRAY_SHULKER_BOX, Material.LIME_SHULKER_BOX,
			Material.MAGENTA_SHULKER_BOX, Material.ORANGE_SHULKER_BOX, Material.PINK_SHULKER_BOX, Material.PURPLE_SHULKER_BOX, Material.RED_SHULKER_BOX,
			Material.WHITE_SHULKER_BOX, Material.YELLOW_SHULKER_BOX, Material.BREWING_STAND);
	
	private List<UUID> stoptimeCooldown = new ArrayList<>();

	@EventHandler
	public void StopTimeAbility (PlayerInteractEvent e) {
		if(!plugin.getConfig().isStoptimeEnable()) {
			return;
		}
		
		if(!(e.getPlayer() instanceof Player)) {
			return;
		}

		Player p = e.getPlayer();
		User user = SimpleUser.getInstance(p);
		UUID id = p.getUniqueId();
		int playerlevel = p.getLevel();

		if(!plugin.getVampires().contains(id) || !p.isSneaking()
				|| !(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
				|| !plugin.getItemManager().isStopTimeClock(e.getItem())){
			return;
		}

		if(!plugin.getStoptimevampire().contains(p.getUniqueId())){
			user.sendMessage(plugin, "stoptime.donthaveability");
			return;
		}

		if(e.getClickedBlock() != null && noBlocks.contains(e.getClickedBlock().getType())) {
			return;
		}

		if(user.getPlayer().getLevel() < 30 && !plugin.noExpReq().contains(id)){
			user.sendMessage(plugin, "stoptime.experienceinsufficient");
			return;
		}
		
		if(stoptimeCooldown.contains(id)) {
			// No cooldown for this player, he is immune
			if(plugin.noCooldown().contains(id)) {
				stoptimeCooldown.remove(id);
				user.sendMessage(plugin, "stoptime.vampireready");
				return;
			}
			else
				user.sendMessage(plugin, "stoptime.cooldown");
			
			return;
		}
		
		// Add player to list
		stoptimeCooldown.add(id);
		unaffectedPlayers.add(id);
		
		// Get nearby entities
		Collection<Entity> entities = p.getWorld().getNearbyEntities(p.getLocation(), 50D, 50D, 50D);
		
		// Player sound & message to vampire
		int newlevel = playerlevel-30;
		p.setLevel(newlevel);
		p.playSound(p.getLocation(), "vampires.stoptime", SoundCategory.MASTER, 0.3F, 1.0F);
		p.sendMessage(" ");
		user.sendMessage(plugin, "stoptime.vampiremessage");
		p.sendMessage(" ");
		// Freeze players & entities (send message & play sound)
		for(Entity en : entities) {
			if (en instanceof Player && !unaffectedPlayers.contains(en.getUniqueId())) {
				if(id.equals(en.getUniqueId())) { // The scanned player is the same who is activating time stop ability... do nothing (skip)
					continue;
				}
				
				Player other = (Player) en;
				User target = SimpleUser.getInstance(other);
				other.playSound(other.getLocation(), "vampires.stoptime", SoundCategory.MASTER, 0.3F, 1.0F);
				if(plugin.getVampires().contains(other.getUniqueId())) {
					unaffectedPlayers.add(other.getUniqueId());
					other.sendMessage(" ");
					target.sendMessage(plugin, "stoptime.vampirenotaffected.initialmessage");
					other.sendMessage(" ");
					
					plugin.getScheduler().scheduleSyncDelayedTask(() -> {
						unaffectedPlayers.remove(other.getUniqueId());
						other.sendMessage(" ");
						target.sendMessage(plugin, "stoptime.vampirenotaffected.finalmessage");
						other.sendMessage(" ");

						p.sendMessage(" ");
						target.sendMessage(plugin, "stoptime.vampireendmessage");
						p.sendMessage(" ");
					}, 11L, TimeUnit.SECONDS);
					return;
				}
				if(!unaffectedPlayers.contains(other.getUniqueId())) {
					affectedPlayers.add(other.getUniqueId());
					
					// Freeze & message
					plugin.getScheduler().scheduleSyncDelayedTask(() -> {
						other.setInvulnerable(true);
						other.setFlySpeed(0F);
						other.setWalkSpeed(0F);
						other.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 200, 128, false, false, false));
						other.sendMessage(" ");
						target.sendMessage(plugin, "stoptime.playerinitialmessage");
						other.sendMessage(" ");
					}, 1L, TimeUnit.SECONDS);
					
				}
				
				// Unfreeze & message
				plugin.getScheduler().scheduleSyncDelayedTask(() -> {
					affectedPlayers.remove(other.getUniqueId());
					other.setInvulnerable(false);
					other.setFlySpeed(0.1F);
					other.setWalkSpeed(0.2F);
					other.sendMessage(" ");
					target.sendMessage(plugin, "stoptime.playerfinalmessage");
					other.sendMessage(" ");
				}, 11L, TimeUnit.SECONDS);
				
			} else if(en instanceof LivingEntity) {
				LivingEntity le = (LivingEntity) en;
				
				// Freeze
				plugin.getScheduler().scheduleSyncDelayedTask(() -> {
					le.setInvulnerable(true);
					le.setAI(false);
				}, 1L, TimeUnit.SECONDS);
				
				// Unfreeze
				plugin.getScheduler().scheduleSyncDelayedTask(() -> {
					le.setInvulnerable(false);
					le.setAI(true);
				}, 11L, TimeUnit.SECONDS);
			}
		}
		
		// Unfreeze everybody message
		plugin.getScheduler().scheduleSyncDelayedTask(() -> {
			p.sendMessage(" ");
			user.sendMessage(plugin, "stoptime.vampireendmessage");
			p.sendMessage(" ");
		}, 11L, TimeUnit.SECONDS);
		
		// Remove player's ability timeout
		plugin.getScheduler().scheduleSyncDelayedTask(() -> {
			if(p != null && p.isOnline() && stoptimeCooldown.contains(id)) {
//				if(!plugin.noStoptimeCooldown().contains(id)) {
				stoptimeCooldown.remove(id);
				user.sendMessage(plugin, "stoptime.ready");
			}
		}, 30L, TimeUnit.MINUTES);
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
				if(plugin.getItemManager().isStopTimeClock(offHand)) {
					player.getInventory().setItemInOffHand(null);
					this.dropItem(player, offHand);
				}
				
				for(ItemStack item : player.getInventory().getContents()) {
					if(plugin.getItemManager().isStopTimeClock(item)) {
						player.getInventory().remove(item);
						this.dropItem(player, item);
					}
				}
			}
		}
	}
	
	private void dropItem(Player player, ItemStack item) {
		plugin.getScheduler().runTask(() -> {
			Item drop = player.getWorld().dropItemNaturally(player.getLocation(), item);
			drop.setPickupDelay(40);
			User user = SimpleUser.getInstance(player);
			user.sendMessage(plugin, "stoptime.playerdropstoptimeclock");
		});
	}
	
}
