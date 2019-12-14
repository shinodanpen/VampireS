package org.luca.VampireS.resourcepack;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.luca.VampireS.VampireSPlugin;
import org.luca.VampireS.commands.ResourcepackCommand;
import org.valdi.SuperApiX.common.dependencies.Dependency;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CustomResourcePack {
    private final VampireSPlugin plugin;

    private final Set<UUID> playersWithRP = new HashSet<>();
    private LocalServer webServer;

    public static final Dependency VERTX = Dependency
            .builder("VERTX")
            .setGroupId("io{}vertx")
            .setArtifactId("vertx-core")
            .setVersion("3.7.0")
            .setChecksum("cBmPyZjvASILvdx4sdOQwuHYG7M/xYhJY89SKzgCyB8=")
            .setAutoLoad(true)
            .build();

    public CustomResourcePack(VampireSPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadDependency() {
        // questo carica il jar
        plugin.getDependencyManager().loadDependencies(plugin.getLogger(), plugin.getBootstrap().getPluginClassLoader(),
                plugin.getBootstrap().getDataFolder(), true, VERTX);
    }

    public void onEnable() {
        webServer = new LocalServer(this, plugin);
        webServer.start();

        plugin.getCommandsManager().registerCommand(new ResourcepackCommand(plugin));
        this.registerListeners();
    }

    public void onDisable() {
        webServer.stop();
    }

    public void registerListeners() {
        PluginManager pm = plugin.getServer().getPluginManager();

        pm.registerEvents(new PlayerListener(this, plugin), plugin.getBootstrap());
        pm.registerEvents(new ResourcePackListener(this, plugin), plugin.getBootstrap());
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

}
