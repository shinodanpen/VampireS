package org.luca.VampireS.customResourcePack;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.luca.VampireS.MainClass;

public class PlayerListener implements Listener {

    private final CustomResourcePack addon;

    private final MainClass plugin;

    public PlayerListener(CustomResourcePack addon, MainClass plugin) {
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
        addon.getScheduler().syncLater(() -> {
            Traveler user = Traveler.getInstance(e.getPlayer());
            user.sendMessage("commands.help.header", TextVariables.LABEL, "Textures");
            e.getPlayer().sendMessage(" ");
            user.sendMessage("join.can-send-question");
            user.sendMessage("join.what-does-it-do");
            user.sendMessage(" ");
            BaseComponent[] confirm = new ComponentBuilder("                 ").reset()
                    .append(user.getTranslation("join.accept")).color(ChatColor.GREEN).bold(true)
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(user.getTranslation("join.click-to-accept")).color(ChatColor.GREEN).create()))
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/resourcepack accept")).append("   ").reset()
                    .append(user.getTranslation("join.reject")).color(ChatColor.RED).bold(true)
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(user.getTranslation("join.click-to-reject")).color(ChatColor.RED).create()))
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/resourcepack reject")).create();
            user.sendMessage(confirm);
            e.getPlayer().sendMessage(" ");
            user.sendMessage("commands.help.end");
        }, 2L, TimeUnit.SECONDS);
    }

}
