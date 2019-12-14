package org.luca.VampireS.resourcepack;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.luca.VampireS.VampireSPlugin;
import org.valdi.SuperApiX.bukkit.users.SimpleUser;
import org.valdi.SuperApiX.bukkit.users.User;
import org.valdi.SuperApiX.bukkit.users.locale.TextVariables;

import java.util.concurrent.TimeUnit;

import static org.luca.VampireS.VampireSPlugin.PREFIX_VALUE;

public class PlayerListener implements Listener {

    private final CustomResourcePack addon;
    private VampireSPlugin plugin;

    public PlayerListener(CustomResourcePack addon, VampireSPlugin plugin) {
        this.addon = addon;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        UtilToken.removeToken(e.getPlayer().getUniqueId());
        addon.removePlayerFromList(e.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        plugin.getScheduler().scheduleSyncDelayedTask(() -> {
            Player player = (Player) e.getPlayer();
            User user = SimpleUser.getInstance(player);
            user.sendRawMessage(" ");
            user.sendMessage(plugin, "commands.header", VampireSPlugin.PREFIX, PREFIX_VALUE);
            user.sendRawMessage(" ");
            user.sendMessage(plugin, "resourcepack.join.can-send-question");
            user.sendMessage(plugin, "resourcepack.join.what-does-it-do");
            user.sendRawMessage(" ");
            BaseComponent[] confirm = new ComponentBuilder("                 ").reset()
                    .append("ACCEPT").bold(true).color(ChatColor.GREEN)
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to accept").color(ChatColor.GREEN).create()))
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/vampiresrp accept")).append("   ").reset()
                    .append("REJECT").color(ChatColor.RED).bold(true)
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to reject.").color(ChatColor.RED).create()))
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/vampiresrp reject")).create();
            user.sendMessage(confirm);
            user.sendRawMessage(" ");
            user.sendMessage(plugin, "commands.end");
            user.sendRawMessage(" ");
            //BaseComponent[] confirm = new TextComponent( "                 " )
        }, 1L, TimeUnit.SECONDS);
    }

}
