package org.luca.VampireS.resourcepack;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.luca.VampireS.VampireSPlugin;
import org.valdi.SuperApiX.bukkit.users.SimpleUser;
import org.valdi.SuperApiX.bukkit.users.User;

public class ResourcePackListener implements Listener {

    private final CustomResourcePack addon;
    private VampireSPlugin plugin;

    public ResourcePackListener(CustomResourcePack addon, VampireSPlugin plugin) {
        this.addon = addon;
        this.plugin = plugin;
    }

    @EventHandler
    public void onResourcePackStatus(PlayerResourcePackStatusEvent event) {
        Player player = event.getPlayer();
        User user = SimpleUser.getInstance(player);
        switch (event.getStatus()) {
            case SUCCESSFULLY_LOADED:
                addon.addPlayerToList(player);
                user.sendMessage(plugin, "resourcepack.success-enable", VampireSPlugin.PREFIX, VampireSPlugin.PREFIX_VALUE);
                break;
            case DECLINED:
                addon.removePlayerFromList(player);
                player.kickPlayer(user.getTranslationOrNothing(plugin, "resourcepack.declined", VampireSPlugin.PREFIX, VampireSPlugin.PREFIX_VALUE));
                break;
            case FAILED_DOWNLOAD:
                addon.removePlayerFromList(player);
                if(plugin.getConfig().isResourcepackKickOnDlFailed()) {
                    player.kickPlayer(user.getTranslationOrNothing(plugin, "resourcepack.download-failed", VampireSPlugin.PREFIX, VampireSPlugin.PREFIX_VALUE));
                }
                else{
                    user.sendMessage(plugin, "resourcepack.download-failed", VampireSPlugin.PREFIX, VampireSPlugin.PREFIX_VALUE);
                }
                break;
            case ACCEPTED:
                addon.addPlayerToList(player);
                user.sendMessage(plugin, "resourcepack.accepted", VampireSPlugin.PREFIX, VampireSPlugin.PREFIX_VALUE);
                break;
        }
    }

}
