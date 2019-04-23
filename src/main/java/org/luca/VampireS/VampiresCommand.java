package org.luca.VampireS;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.minecraft.server.v1_13_R2.NBTTagCompound;

public class VampiresCommand implements CommandExecutor {
	private final MainClass plugin;
	
	public VampiresCommand(MainClass plugin) {
		this.plugin = plugin;
	}
	


	/*
	 * (non-Javadoc)
	 * @see org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(args.length  > 0 && args[0].equalsIgnoreCase("god")) {
			if(sender instanceof Player) {
				String perm = "vampires.godmode";
				Player p = (Player) sender;
				String displayName = p.getDisplayName();
				if(!plugin.getVampires().contains(p.getUniqueId())) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig()
							.getString("commandmessage.mustbevampire", "[prefix]&eYou must be a Vampire to execute that command.")).replace("[prefix]", plugin.plPrefix));
					return false;
				}
				if(!p.hasPermission(perm)) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig()
							.getString("commandmessage.insufficientperms", "&cInsufficient permissions ([perm]).")).replace("[perm]", perm));
					return false;
					}
				
				if(plugin.getNoDamage().contains(p.getUniqueId())) {
					plugin.getNoDamage().remove(p.getUniqueId());
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig()
							.getString("commandmessage.vampiregodmode.disablesuccess", "[prefix]&eVampire god mode &cdisabled &efor [displayname].").replace("[prefix]", plugin.plPrefix))
							.replace("[displayname]", displayName));
					}
				else {
					plugin.getNoDamage().add(p.getUniqueId());
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig()
							.getString("commandmessage.vampiregodmode.enablesuccess", "[prefix]&eVampire god mode &aenabled &efor [displayname].").replace("[prefix]", plugin.plPrefix))
							.replace("[displayname]", displayName));
					}
				}
					return true;
			}	
		
		if(args.length  > 0 && args[0].equalsIgnoreCase("cooldown")) {
			if(sender instanceof Player) {
					String perm = "vampires.nocooldown";
					Player p = (Player) sender;
					String displayName = p.getDisplayName();
					if(!plugin.getVampires().contains(p.getUniqueId())) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig()
								.getString("commandmessage.mustbevampire", "[prefix]&cYou must be a Vampire to execute that command!").replace("[prefix]", plugin.plPrefix)));
						return false;
					}
					if(!p.hasPermission(perm)) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig()
								.getString("commandmessage.insufficientperms", "&cInsufficient permissions ([perm]).")).replace("[perm]", perm));
						return false;
						}
					
					if(plugin.noStoptimeCooldown().contains(p.getUniqueId())) {
						plugin.noStoptimeCooldown().remove(p.getUniqueId());
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig()
								.getString("commandmessage.vampirenocooldown.disablesuccess", "[prefix]&eVampire Stop Time cooldown &cdisabled &efor [displayname].").replace("[prefix]", plugin.plPrefix))
								.replace("[displayname]", displayName));
						}
					else {
						plugin.noStoptimeCooldown().add(p.getUniqueId());
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig()
								.getString("commandmessage.vampirenocooldown.enablesuccess", "[prefix]&eVampire Stop Time cooldown &aenabled &efor [displayname].").replace("[prefix]", plugin.plPrefix))
								.replace("[displayname]", displayName));
						}
					}
						return true;
			}
		
		if(args.length > 0 && args[0].equalsIgnoreCase("summon")) {
			if(sender instanceof Player) {
				Player p = (Player) sender;
				Location loc = p.getLocation();
				Random rand = new Random();
				String perm = "vampires.summon";
				if(!p.hasPermission(perm)) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig()
							.getString("commandmessage.insufficientperms", "&cInsufficient permissions ([perm]).")).replace("[perm]", perm));
				}
				if(args.length > 1 && args[1].equalsIgnoreCase("food")) {
					for(EntityType type : OnStonemaskActivation.vampireFood) {
						Location spawn = loc.clone().add(rand.nextInt(10) - 5, 0, rand.nextInt(10) - 5);
						LivingEntity e = (LivingEntity) spawn.getWorld().spawnEntity(spawn, type);
						e.setInvulnerable(true);
						e.setAI(false);
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
							e.setInvulnerable(false);
						}, 100L);
					}
					String displayName = p.getDisplayName();
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("commandmessage.summon.food", "[prefix]&eVampire food entities successfully summoned at [displayname]&e's location.").replace("[prefix]", plugin.plPrefix)).replace("[displayname]", displayName));
				}
				if(args.length > 1 && args[1].equalsIgnoreCase("enemy")) {
					for(EntityType type : OnStonemaskActivation.vampireEnemy) {
						Location spawn = loc.clone().add(rand.nextInt(10) - 5, 0, rand.nextInt(10) - 5);
						LivingEntity e = (LivingEntity) spawn.getWorld().spawnEntity(spawn, type);
						e.setInvulnerable(true);
						e.setAI(false);
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
							e.setInvulnerable(false);
						}, 100L);
					}
					String displayName = p.getDisplayName();
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("commandmessage.summon.enemy", "[prefix]&eVampire enemy entities successfully summoned at [displayname]&e's location.").replace("[prefix]", plugin.plPrefix)).replace("[displayname]", displayName));
				}
			}
			
		}
		if(args.length > 1 && args[1].equalsIgnoreCase("vampirestatus")) {
			Player p = (Player) sender;
			String perm = "vampires.changestatus";
			if(!p.hasPermission(perm)) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig()
						.getString("commandmessage.insufficientperms", "&cInsufficient permissions ([perm]).")).replace("[perm]", perm));
				return false; // Exit from the plugin and tell bukkit the command didn't go well
			}
			if(args.length > 0 && args[0].equalsIgnoreCase("add")) {
				plugin.getVampires().add(p.getUniqueId());
				p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 255, false, false, false));
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WITHER_BREAK_BLOCK, SoundCategory.MASTER, 0.3F, 0.4F);
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WITHER_AMBIENT, SoundCategory.MASTER, 0.3F, 0.1F);
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("commandmessage.vampirestatus.addsuccess", "[prefix]&eVampire status successfully applied.").replace("[prefix]", plugin.plPrefix)));
				return true;
			}
			else if(args.length > 0 && args[0].equalsIgnoreCase("remove")) {
				plugin.getVampires().remove(p.getUniqueId());
				p.removePotionEffect(PotionEffectType.NIGHT_VISION);
				p.removePotionEffect(PotionEffectType.WATER_BREATHING);
				p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
				p.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
				p.removePotionEffect(PotionEffectType.FAST_DIGGING);
				p.removePotionEffect(PotionEffectType.REGENERATION);
				p.removePotionEffect(PotionEffectType.HEALTH_BOOST);
				p.removePotionEffect(PotionEffectType.SPEED);
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("commandmessage.vampirestatus.removesuccess", "[prefix]&eVampire status successfully removed.").replace("[prefix]", plugin.plPrefix)));
				return true;
			}
		}
		if(args.length > 0 && args[0].equalsIgnoreCase("help")) {
			String version = Bukkit.getBukkitVersion();
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e------------------------------------"));
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4&lVampireS &7- &bVersion &a" + plugin.getDescription().getVersion()+ "&b for &e" + version));
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bMade by &c&lJinsei_No_Danpen &b& &c&lValdi_1111"));
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e------------------------------------"));
			return true; // all right
		}
		if(args.length > 0 && args[0].equalsIgnoreCase("give")) {
			if(sender instanceof Player) {
				// execute this code only if the sender is a PLAYER
				Player p = (Player) sender;
				String perm = "vampires.give";
				// Check player's permissions
				if(!p.hasPermission(perm)) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("commandmessage.insufficientperms", "&cInsufficient permissions ([perm]).")).replace("[perm]", perm));
					return false; // Exit from the plugin and tell bukkit the command didn't go well
				}
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("commandmessage.giveitem", "[prefix]&aItem successfully given.").replace("[prefix]", plugin.plPrefix)));
				
				if(args.length > 1 && args[1].equalsIgnoreCase("stonemask")) {
					//The player has the permission, give him the item
					this.giveStonemaskToPlayer(p);
				}
				if(args.length > 1 && args[1].equalsIgnoreCase("silversword")) {
					//The player has the permission, give him the item
					this.giveSilverswordToPlayer(p);
				}
				if(args.length > 1 && args[1].equalsIgnoreCase("stoptimeclock")) {
					//The player has the permission, give him the item
					this.giveStopTimeClock(p);
				}
				return true; // all right
			}			
			
			// It's not a player, the plugin assumes it's the console, since entities can't execute commands
			if(args.length <= 2) {
				// The command doesn't have arguments!
				// Show the command usage.
				sender.sendMessage(ChatColor.RED+"Usage: /" + label + " give <player> <item>");
				return false; // Exit from the plugin and tell bukkit the command didn't go well
			}
			//the plugin gets the player from the entered argument
			Player player = Bukkit.getPlayer(args[1]);
			if(player == null) {
				sender.sendMessage(ChatColor.RED+"Player not found");
				return false;
			}
			
			// Exit from the function if there's a player
			if(args[2].equalsIgnoreCase("stonemask")) {
				//The player has been found, let's give him the item
				this.giveStonemaskToPlayer(player);
			}
			if(args[2].equalsIgnoreCase("silversword")) {
				//The player has been found, let's give him the item
				this.giveSilverswordToPlayer(player);
			}
			return true; // all right
		}
		return true; // tutto ok
	}
	
	public static ItemStack getMask() {
		ItemStack item = new ItemStack(Material.DIAMOND_HOE);
		// ItemMeta -> properties of the item
		ItemMeta meta = item.getItemMeta(); // the plugin gets the existing properties
		// I set a displayname
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8&lStone Mask"));
		// I set a lore
		List<String> stonemasklore = new ArrayList<>();
		stonemasklore.add(ChatColor.translateAlternateColorCodes('&', "&7&oA mysterious mask that possesses a great power"));
		stonemasklore.add(ChatColor.translateAlternateColorCodes('&', "&7&oallowing you to transform into a Vampire."));
		meta.setLore(stonemasklore);
		meta.setUnbreakable(true);
		((Damageable) meta).setDamage(1462);
		item.setItemMeta(meta); // I apply the properties to the item
		return item;
	}
	/**
	 * Give out item to an online player
	 * @param player the player
	 */
	public void giveStonemaskToPlayer(Player player) {
		player.getInventory().addItem(getMask());
		//Material = list of Minecraft items
	}
	
	public static ItemStack getSilverSword() {
		ItemStack item = new ItemStack(Material.IRON_SWORD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&lSilver Sword"));
		List<String> silverswordlore = new ArrayList<>();
		silverswordlore.add(ChatColor.translateAlternateColorCodes('&', "&7&oA sword that can damage only Vampires,"));
		silverswordlore.add(ChatColor.translateAlternateColorCodes('&', "&7&oinflicting them more damage."));
		meta.setLore(silverswordlore);
		meta.setUnbreakable(true);
		((Damageable) meta).setDamage(151);
		item.setItemMeta(meta);
		return item;
	}
	
	public void giveSilverswordToPlayer(Player player) {
		player.getInventory().addItem(getSilverSword());
	}
	
	public static ItemStack getStopTimeClock() {
		ItemStack item = new ItemStack(Material.CLOCK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lStop Time Clock"));
		List<String> stoptimeclocklore = new ArrayList<>();
		stoptimeclocklore.add(ChatColor.translateAlternateColorCodes('&', "&7&oA clock that allows a Vampire"));
		stoptimeclocklore.add(ChatColor.translateAlternateColorCodes('&', "&7&oto stop time for a 10 seconds period."));
		meta.setLore(stoptimeclocklore);
		meta.setUnbreakable(true);
		((Damageable) meta).setDamage(151);
		item.setItemMeta(meta);
		
		net.minecraft.server.v1_13_R2.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		NBTTagCompound tag = nmsItem.getOrCreateTag();
		tag.setString("vampireS", UUID.randomUUID().toString());
		nmsItem.setTag(tag);
		
		return CraftItemStack.asBukkitCopy(nmsItem);
	}
	
	public void giveStopTimeClock(Player player) {
		player.getInventory().addItem(getStopTimeClock());
	}


}
