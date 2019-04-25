package org.luca.VampireS.customResourcePack;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.luca.VampireS.MainClass;

public class ResourcePackListener implements Listener {

    private final CustomResourcePack addon;

    private final MainClass plugin;

    public ResourcePackListener(CustomResourcePack addon, MainClass plugin) {
        this.addon = addon;
        this.plugin = plugin;
    }

    @EventHandler
    public void onResourcePackStatus(PlayerResourcePackStatusEvent event) {
        Player player = event.getPlayer();
        switch (event.getStatus()) {
            case SUCCESSFULLY_LOADED:
                addon.addPlayerToList(player);
                player.sendMessage("resourcepack.loaded");
                break;
            case DECLINED:
                addon.removePlayerFromList(player);
                player.sendMessage("resourcepack.rejected");
                break;
            case FAILED_DOWNLOAD:
                addon.removePlayerFromList(player);
                player.sendMessage("resourcepack.failed_download");
                break;
            case ACCEPTED:
                addon.addPlayerToList(player);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("customresourcepack.accepted",
                        "[prefix]&a&lINFO: &eResource pack accepted. Downloading...")
                        .replace("[prefix]", plugin.plPrefix)));
                break;
        }
    }

}
