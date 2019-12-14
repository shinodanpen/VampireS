package org.luca.VampireS.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.luca.VampireS.VampireSPlugin;
import org.valdi.SuperApiX.bukkit.api.Title;
import org.valdi.SuperApiX.bukkit.users.SimpleUser;
import org.valdi.SuperApiX.bukkit.users.User;

public class VampireSunDamage implements Runnable {
	private final VampireSPlugin plugin;
	
	public VampireSunDamage(VampireSPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		for(Player p : Bukkit.getOnlinePlayers()) {

			if(plugin.getVampires().contains(p.getUniqueId())) {

				if (p.getGameMode() == GameMode.CREATIVE || plugin.getNoDamage().contains(p.getUniqueId())) {
					continue;
				}

				World world = p.getWorld();
				if (world.getEnvironment() == Environment.THE_END || world.getEnvironment() == Environment.NETHER) {
					continue;
				}

				long time = world.getTime();

				if (time > 12300 && time < 23850 || p.getWorld().hasStorm()) {
					continue;
				}

				int maxY = world.getHighestBlockYAt(p.getLocation());
				if (maxY > p.getLocation().getY()) {
					continue;
				}

				p.damage(40D);
				p.setFireTicks(41);

				User user = SimpleUser.getInstance(p);
				Title.builder()
						.title(user.getTranslationOrNothing(plugin, "vampiresundamage.titlemessage"))
						.subtitle(user.getTranslationOrNothing(plugin, "vampiresundamage.subtitlemessage"))
						.fadeIn(1)
						.stay(20)
						.fadeOut(10)
						.send(p);
			} 
		}
	}

}
