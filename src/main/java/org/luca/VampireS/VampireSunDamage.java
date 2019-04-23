package org.luca.VampireS;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class VampireSunDamage implements Runnable {
	private final MainClass plugin;
	
	public VampireSunDamage(MainClass plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			// the plugin executes this function for every player currently online on the server
			if(!plugin.getVampires().contains(p.getUniqueId())) {
				continue; // continue: don't execute the function for this player and move on to the next
			}
			
			PotionEffect effect1 = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 3, false, false, false);
			PotionEffect effect2 = new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1, false, false, false);
			PotionEffect effect3 = new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 0, false, false, false);
			PotionEffect effect4 = new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false, false);
			PotionEffect effect5 = new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false, false);
			PotionEffect effect6 = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 2, false, false, false);
			PotionEffect effect7 = new PotionEffect(PotionEffectType.HEALTH_BOOST, Integer.MAX_VALUE, 4, false, false, false);
			PotionEffect effect8 = new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 1, false, false, false);
			
			p.addPotionEffect(effect1);
			p.addPotionEffect(effect2);
			p.addPotionEffect(effect3);
			p.addPotionEffect(effect4);
			p.addPotionEffect(effect5);
			p.addPotionEffect(effect6);
			p.addPotionEffect(effect7);
			p.addPotionEffect(effect8);
			
			if(p.getGameMode() == GameMode.CREATIVE || plugin.getNoDamage().contains(p.getUniqueId())) {
				continue;
			}
			
			World world = p.getWorld();			
			if(world.getEnvironment() == Environment.THE_END || world.getEnvironment() == Environment.NETHER) {
				continue;
			}
			
			long time = world.getTime();
			if(time > 12300 && time < 23850) {
				continue;
			}
			
			int maxY = world.getHighestBlockYAt(p.getLocation());
			if(maxY > p.getLocation().getY()) {
				continue;
			}
			
			p.damage(40D);
			p.setFireTicks(41);
			p.sendTitle(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("vampiresundamage.titlemessage", "&e&lYOU ARE EXPOSED TO SUNLIGHT!!!")),
					ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("vampiresundamage.subtitlemessage", "&6HURRY UP AND FIND COVER!")), 1, 20, 10);
		}
	}

}
