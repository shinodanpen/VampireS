package org.luca.VampireS.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import org.luca.VampireS.VampireSPlugin;
import org.valdi.SuperApiX.bukkit.users.SimpleUser;
import org.valdi.SuperApiX.bukkit.users.User;

public class VampireSilverSword implements Listener, Runnable {

    private final VampireSPlugin plugin;

    public VampireSilverSword(VampireSPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onVampireHitBySilverSword(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)) {
            return;
        }

        Player damager = (Player) e.getDamager();
        Entity damaged = e.getEntity();
        if (!(plugin.getVampires().contains(damager.getUniqueId())) && plugin.getItemManager().isSilverSword(damager.getInventory().getItemInMainHand())) {
            if (damaged instanceof Player && plugin.getVampires().contains(damaged.getUniqueId())) {
                e.setDamage(38);
                User user = SimpleUser.getInstance((Player) damaged);
                user.sendMessage(plugin, "silversword.vampirehit");
                return;
            }

            if (e.getCause() == DamageCause.ENTITY_SWEEP_ATTACK || e.getCause() == DamageCause.ENTITY_ATTACK) {
                e.setCancelled(true);
            }
        }
    }


    /**
     * All vampire players holding a SilverSword in their inventory item will be forced to drop it.
     */
    @Override
    public void run() {
        for (UUID uuid : plugin.getVampires()) {
            Player vampire = Bukkit.getPlayer(uuid);
            if (vampire == null) {
                continue;
            }

            ItemStack offHand = vampire.getInventory().getItemInOffHand();
            if (plugin.getItemManager().isSilverSword(offHand)) {
                vampire.getInventory().setItemInOffHand(null);
                this.dropItem(vampire, offHand);
            }

            for (ItemStack item : vampire.getInventory().getContents()) {
                if (plugin.getItemManager().isSilverSword(item)) {
                    vampire.getInventory().remove(item);
                    this.dropItem(vampire, item);
                }
            }
        }
    }

    private void dropItem(Player vampire, ItemStack item) {
        plugin.getScheduler().runTask(() -> {
            Item drop = vampire.getWorld().dropItemNaturally(vampire.getLocation(), item);
            drop.setPickupDelay(40);
            User user = SimpleUser.getInstance(vampire);
            user.sendMessage(plugin, "silversword.vampiredropsilversword");
        });
    }

}
