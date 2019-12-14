package org.luca.VampireS;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import net.minecraft.server.v1_14_R1.PacketPlayOutWorldBorder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.valdi.SuperApiX.bukkit.users.User;

public class VampireCreator implements Listener {
	private static final List<UUID> transformation = new ArrayList<>();
	
	/**
	 * Set the player as vampire
	 * @param plugin an active plugin instance
	 * @param p the player
	 */
	public static void setVampire(VampireSPlugin plugin, Player p) {
		transformation.add(p.getUniqueId());
		p.setInvulnerable(true);
		Location loc = p.getLocation();
		Location newLoc = loc.clone();
		float pitch = loc.getPitch();
		newLoc.setPitch(-90F);
		p.teleport(newLoc, TeleportCause.PLUGIN);

		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WITHER_SPAWN, SoundCategory.MASTER, 0.3F, 0.1F);

		sendBorder(p, 0D, loc);


		p.setWalkSpeed(0);
		p.setFlySpeed(0);
		PotionEffect noJump = new PotionEffect(PotionEffectType.JUMP, 180, 128, false, false, false);
		
		PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 60, 255, false, false, false);
		PotionEffect blind = new PotionEffect(PotionEffectType.BLINDNESS, 180, 255, false, false, false);
		PotionEffect regen255 = new PotionEffect(PotionEffectType.REGENERATION, 200, 255, false, false, false);

		p.addPotionEffect(blind);
		p.addPotionEffect(speed);
		p.addPotionEffect(regen255);
		
		// After 60 ticks the plugin applies the other effects
		plugin.getScheduler().scheduleSyncDelayedTask(() -> {
			PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, 60, 255, false, false, false);
			// The plugin applies the effect only if the receiver is online
			if(p != null && p.isOnline()) {
				p.addPotionEffect(slow);
			}
		}, 3L, TimeUnit.SECONDS);
		
		plugin.getScheduler().scheduleSyncDelayedTask(() -> {
			PotionEffect nausea = new PotionEffect(PotionEffectType.CONFUSION, 60, 1, false, false, false);
			// The plugin applies the effect only if the receiver is online
			if(p != null && p.isOnline()) {
				p.addPotionEffect(nausea);
			}
		}, 6L, TimeUnit.SECONDS);
		
		plugin.getScheduler().scheduleSyncDelayedTask(() -> {
			PotionEffect effect1 = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 3, false, false, false);
			PotionEffect effect2 = new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1, false, false, false);
			PotionEffect effect3 = new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 0, false, false, false);
			PotionEffect effect4 = new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false, false);
			PotionEffect effect5 = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false, false);
			PotionEffect effect6 = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 2, false, false, false);
			PotionEffect effect7 = new PotionEffect(PotionEffectType.HEALTH_BOOST, Integer.MAX_VALUE, 4, false, false, false);
			PotionEffect effect8 = new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 1, false, false, false);
			PotionEffect effect9 = new PotionEffect(PotionEffectType.DOLPHINS_GRACE, Integer.MAX_VALUE, 1, false, false, false);
			
			// The plugin applies the effect only if the receiver is online
			if(p != null && p.isOnline()) {
				p.addPotionEffect(effect1);
				p.addPotionEffect(effect2);
				p.addPotionEffect(effect3);
				p.addPotionEffect(effect4);
				p.addPotionEffect(effect5);
				p.addPotionEffect(effect6);
				p.addPotionEffect(effect7);
				p.addPotionEffect(effect8);
				p.addPotionEffect(effect9);

				p.setWalkSpeed(0.2F);
				p.setFlySpeed(0.1F);

				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WITHER_BREAK_BLOCK, SoundCategory.MASTER, 0.3F, 0.4F);
				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WITHER_AMBIENT, SoundCategory.MASTER, 0.3F, 0.1F);

				p.getLocation().setPitch(pitch);
				p.setInvulnerable(false);
			}

			// The plugin removes the player from the transformation list and applies the vampire status anyway
			transformation.remove(p.getUniqueId());
			plugin.getVampires().add(p.getUniqueId());

			restoreBorder(p, 60000000D, loc);



			Random random = new Random();
			int result = random.nextInt((2 - 1) + 1) + 1;
			String cmd = "vampire ability info " + p.getName();
			switch (result){
				case 1: {
					plugin.getStoptimevampire().add(p.getUniqueId());
					p.performCommand(cmd);
					break;
				}
				case 2: {
					plugin.getFixitemvampire().add(p.getUniqueId());
					p.performCommand(cmd);
					break;
				}

			}

		}, 9L, TimeUnit.SECONDS);
	}

	public static boolean isTransforming(Player player) {
		return transformation.contains(player.getUniqueId());
	}
	
	public static void setTransforming(Player player, boolean value) {
		if(value) {
			transformation.add(player.getUniqueId());
		} else {
			transformation.remove(player.getUniqueId());
		}
	}

	@EventHandler
	public void onPlayerMovementDuringTransformation (PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if((isTransforming(p))){

			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerToggleFlightDuringTransformation (PlayerToggleFlightEvent e) {
		Player p = e.getPlayer();
		if((isTransforming(p))){
			e.setCancelled(true);
		}
	}

	public static void sendBorder(Player player, double radius, Location loc) {
		net.minecraft.server.v1_14_R1.WorldBorder worldBorder = new net.minecraft.server.v1_14_R1.WorldBorder();

		worldBorder.setCenter(loc.getX(), loc.getZ());
		worldBorder.setSize(0);
		worldBorder.world = ((CraftWorld) player.getWorld()).getHandle();

		PacketPlayOutWorldBorder sizePacket = new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_SIZE);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(sizePacket);

		PacketPlayOutWorldBorder centerPacket = new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_CENTER);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(centerPacket);
	}

	public static void restoreBorder(Player player, double radius, Location loc) {
		net.minecraft.server.v1_14_R1.WorldBorder worldBorder = new net.minecraft.server.v1_14_R1.WorldBorder();

		worldBorder.setCenter(loc.getX(), loc.getZ());
		worldBorder.setSize(60000000);
		worldBorder.world = ((CraftWorld) player.getWorld()).getHandle();

		PacketPlayOutWorldBorder sizePacket = new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_SIZE);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(sizePacket);

		PacketPlayOutWorldBorder centerPacket = new PacketPlayOutWorldBorder(worldBorder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_CENTER);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(centerPacket);
	}

	@EventHandler
	public void onPlayerQuit (PlayerQuitEvent e) {
		Player p  = e.getPlayer();
		if(VampireCreator.isTransforming(p)) {
			p.setWalkSpeed(0.2F);
			p.setFlySpeed(0.1F);
			p.setInvulnerable(false);
		}
	}

}
