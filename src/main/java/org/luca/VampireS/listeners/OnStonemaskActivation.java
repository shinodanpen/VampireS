package org.luca.VampireS.listeners;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.luca.VampireS.VampireSPlugin;
import org.luca.VampireS.events.VampireBitingEvent;

import com.google.common.collect.ImmutableList;

import net.md_5.bungee.api.ChatColor;
import org.luca.VampireS.VampireCreator;
import org.valdi.SuperApiX.bukkit.users.SimpleUser;
import org.valdi.SuperApiX.bukkit.users.User;

public class OnStonemaskActivation implements Listener{

	private final VampireSPlugin plugin;

	//Stone Mask activation
	public OnStonemaskActivation(VampireSPlugin plugin) {
		this.plugin = plugin;
	}



	@EventHandler
	public void StonemaskActivationEvent(EntityDamageByEntityEvent e) {
		if(!plugin.getConfig().isStonemaskActivation()) {
			return;
		}
		
		LivingEntity damageTaker = (LivingEntity) e.getEntity();
		if(e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			if(plugin.getVampires().contains(p.getUniqueId())) {
				return;
			}
			if(damageTaker.getType() == EntityType.VILLAGER) {
				if(e.getCause() == DamageCause.ENTITY_ATTACK) {
					if(p.getInventory().getItemInMainHand().getType() == Material.AIR) {
						if(plugin.getItemManager().isMask(p.getInventory().getHelmet())) {
							VampireBitingEvent event = new VampireBitingEvent(p, damageTaker); // I declare an event
							Bukkit.getPluginManager().callEvent(event); // I call the event for all plugins
							if(event.isCancelled()) {
								return; // If the event is cancelled the player doesn't transform
							}
							// let's start the transformation process
							damageTaker.setHealth(0);
							VampireCreator.setVampire(plugin, p);
						}
					}
				}
			}
		}
		if(!(e.getDamager() instanceof Player)){
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void IfPlayerMovesDuringTransformation (PlayerMoveEvent e) {
		if(VampireCreator.isTransforming(e.getPlayer())) {
			if(e.getTo().getPitch() != -90F) {
				e.setCancelled(true);
			}
		}
	}
}

