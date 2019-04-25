package org.luca.VampireS.customResourcePack;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.luca.VampireS.MainClass;
import org.luca.VampireS.customResourcePack.ResourcePackCommands.ResourcePackAccept;
import org.luca.VampireS.customResourcePack.ResourcePackCommands.ResourcePackReject;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CustomResourcePack {
    private final MainClass plugin;

    private final Set<UUID> playersWithRP = new HashSet<>();
    private LocalServer webServer;

    public CustomResourcePack(MainClass plugin) {
        this.plugin = plugin;
    }

    public void onEnable() {
        webServer = new LocalServer(this);
        webServer.start();
        plugin.getCommand("vampires").setExecutor(new ResourcePackAccept(this, addon));
        plugin.getCommand("vampires").setExecutor(new ResourcePackReject(this, addon));
    }


    public void onPostEnable() {
        this.registerListeners();
    }

    public void onDisable() {
        webServer.stop();
    }

    public static CustomResourcePack getInstance() {
        return addon;
    }

    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), addon);
        Bukkit.getPluginManager().registerEvents(new ResourcePackListener(this), addon);
    }

    public LocalServer getWebServer() {
        return this.webServer;
    }

    public void addPlayerToList(Player player) {
        playersWithRP.add(player.getUniqueId());
    }

    public void removePlayerFromList(Player player) {
        playersWithRP.remove(player.getUniqueId());
    }

    public boolean isPlayerInList(Player player) {
        return playersWithRP.contains(player.getUniqueId());
    }

    public void getLogger() {
        return;
    }

}
