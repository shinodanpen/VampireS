package org.luca.VampireS;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

import net.md_5.bungee.api.ChatColor;

public class CustomResourcePack implements Listener {
	private static final String resURL = "http://2.224.170.54/texture.zip";

	private final MainClass plugin;

	public CustomResourcePack (MainClass plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void SetResourcepackOnLogin (PlayerJoinEvent e) {
		if(!plugin.getConfig().getBoolean("customresourcepack.enabled")) {
			return;
		}
		
		Player p = e.getPlayer();
		p.setResourcePack(resURL);
	}
	
	
	@EventHandler
	public void resourcePackCases (PlayerResourcePackStatusEvent e) {
		Player p = e.getPlayer();
		switch(e.getStatus()) {
		case DECLINED:
			p.kickPlayer(ChatColor.translateAlternateColorCodes('&',plugin.getConfig()
					.getString("customresourcepack.playerkick", "[prefix]&4&lERROR: &eYou &o&emust &euse the plugin's custom resource pack. Log in again and &o&eaccpet &e the resource pack.").replace("[prefix]", plugin.plPrefix)));
			break;
		case FAILED_DOWNLOAD:
			p.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig()
					.getString("customresourcepack.downloadfailed", "[prefix]&6&lWARN: &eResource pack download failed. Retrying  in 5 seconds.").replace("[prefix]", plugin.plPrefix)));
			Bukkit.getScheduler().runTaskLater(plugin, () -> p.setResourcePack(resURL), 100L);
			break;
		case ACCEPTED:
			p.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig()
					.getString("customresourcepack.accepted", "[prefix]&a&lINFO: &eResource pack accepted. Downloading...").replace("[prefix]", plugin.plPrefix)));
			break;
		case SUCCESSFULLY_LOADED:
			p.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig()
					.getString("customresourcepack.successenable", "[prefix]&a&lINFO: &eResource pack successfully enabled.").replace("[prefix]", plugin.plPrefix)));
			break;
		}
	}
}
