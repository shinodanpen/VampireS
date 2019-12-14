package org.luca.VampireS.listeners.abilities;

import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.luca.VampireS.VampireSPlugin;
import org.valdi.SuperApiX.bukkit.users.SimpleUser;
import org.valdi.SuperApiX.bukkit.users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class VampireItemRestorationAbility implements Listener, Runnable {

    private final VampireSPlugin plugin;

    public VampireItemRestorationAbility(VampireSPlugin plugin) {
        this.plugin = plugin;
    }

    List<Material> noBlocks = ImmutableList.of(Material.ACACIA_BUTTON, Material.BIRCH_BUTTON, Material.DARK_OAK_BUTTON,
            Material.JUNGLE_BUTTON, Material.OAK_BUTTON, Material.SPRUCE_BUTTON, Material.STONE_BUTTON, Material.ACACIA_TRAPDOOR,
            Material.BIRCH_TRAPDOOR, Material.DARK_OAK_TRAPDOOR, Material.IRON_TRAPDOOR, Material.JUNGLE_TRAPDOOR, Material.OAK_TRAPDOOR,
            Material.OAK_TRAPDOOR, Material.SPRUCE_TRAPDOOR, Material.ACACIA_DOOR, Material.BIRCH_DOOR, Material.DARK_OAK_DOOR, Material.IRON_DOOR,
            Material.JUNGLE_DOOR, Material.OAK_DOOR, Material.SPRUCE_DOOR, Material.LEVER, Material.FURNACE, Material.CHEST, Material.CRAFTING_TABLE,
            Material.DROPPER, Material.DISPENSER, Material.DAYLIGHT_DETECTOR, Material.ANVIL, Material.CHIPPED_ANVIL, Material.DAMAGED_ANVIL,
            Material.BEACON, Material.COMPARATOR, Material.COMMAND_BLOCK, Material.HOPPER, Material.TRAPPED_CHEST, Material.ENCHANTING_TABLE,
            Material.ENDER_CHEST, Material.END_PORTAL_FRAME, Material.END_GATEWAY, Material.ITEM_FRAME,
            Material.ARMOR_STAND, Material.BLACK_BED, Material.BLUE_BED, Material.BROWN_BED, Material.CYAN_BED, Material.GRAY_BED, Material.GREEN_BED,
            Material.LIGHT_BLUE_BED, Material.LIGHT_GRAY_BED, Material.LIME_BED, Material.MAGENTA_BED, Material.ORANGE_BED, Material.PINK_BED,
            Material.PURPLE_BED, Material.RED_BED, Material.WHITE_BED, Material.YELLOW_BED, Material.JUKEBOX, Material.NOTE_BLOCK,
            Material.REPEATER, Material.CHAIN_COMMAND_BLOCK, Material.REPEATING_COMMAND_BLOCK,
            Material.SHULKER_BOX, Material.BLACK_SHULKER_BOX, Material.BLUE_SHULKER_BOX, Material.BROWN_SHULKER_BOX, Material.CYAN_SHULKER_BOX,
            Material.GRAY_SHULKER_BOX, Material.GREEN_SHULKER_BOX, Material.LIGHT_BLUE_SHULKER_BOX, Material.LIGHT_GRAY_SHULKER_BOX, Material.LIME_SHULKER_BOX,
            Material.MAGENTA_SHULKER_BOX, Material.ORANGE_SHULKER_BOX, Material.PINK_SHULKER_BOX, Material.PURPLE_SHULKER_BOX, Material.RED_SHULKER_BOX,
            Material.WHITE_SHULKER_BOX, Material.YELLOW_SHULKER_BOX, Material.BREWING_STAND);

    private List<UUID> fixitemCooldown = new ArrayList<>();

    @EventHandler
    public void FixItemAbility (PlayerInteractEvent e) {
        if(!plugin.getConfig().isFixitemEnable()) {
            return;
        }

        if(!(e.getPlayer() instanceof Player)) {
            return;
        }

        Player p = e.getPlayer();
        User user = SimpleUser.getInstance(p);
        UUID id = p.getUniqueId();
        int playerlevel = p.getLevel();

        if(!plugin.getVampires().contains(id) || !p.isSneaking()
                || !(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
                || !plugin.getItemManager().isRestorationRod(e.getItem())){
            return;
        }

        if(!plugin.getFixitemvampire().contains(p.getUniqueId())){
            user.sendMessage(plugin, "itemrestoration.donthaveability");
            return;
        }

        if(e.getClickedBlock() != null && noBlocks.contains(e.getClickedBlock().getType())) {
            return;
        }

        if(user.getPlayer().getLevel() < 10 && !plugin.noExpReq().contains(id)){
            user.sendMessage(plugin, "itemrestoration.experienceinsufficient");
            return;
        }

        if(fixitemCooldown.contains(id)) {
            // No cooldown for this player, he is immune
            if(plugin.noCooldown().contains(id)) {
                fixitemCooldown.remove(id);
                user.sendMessage(plugin, "itemrestoration.ready");
                return;
            }
            else
                user.sendMessage(plugin, "itemrestoration.cooldown");

            return;
        }

        // Add player to list
        fixitemCooldown.add(id);

        // Player sound & message to vampire



        ItemStack item = p.getInventory().getItemInOffHand();
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        if((((Damageable) meta).getDamage() == 0)){
            user.sendMessage(plugin, "itemrestoration.alreadyfixed");
            return;
        }

        ((Damageable) meta).setDamage(0);
        item.setItemMeta(meta);
        int newlevel = playerlevel-10;
        p.setLevel(newlevel);

        p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, SoundCategory.MASTER, 0.3F, 1.0F);
        user.sendMessage(plugin, "itemrestoration.itemfixed");

        // Remove player's ability timeout
        plugin.getScheduler().scheduleSyncDelayedTask(() -> {
            if(p != null && p.isOnline() && fixitemCooldown.contains(id)) {
//				if(!plugin.noStoptimeCooldown().contains(id)) {
                fixitemCooldown.remove(id);
                user.sendMessage(plugin, "itemrestoration.ready");
            }
        }, 10L, TimeUnit.MINUTES);
    }



    /**
     * All non vampire players holding a RestorationRod in their inventory item will be forced to drop it.
     */
    @Override
    public void run() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(!plugin.getVampires().contains(player.getUniqueId())) {
                ItemStack offHand = player.getInventory().getItemInOffHand();
                if(plugin.getItemManager().isRestorationRod(offHand)) {
                    player.getInventory().setItemInOffHand(null);
                    this.dropItem(player, offHand);
                }

                for(ItemStack item : player.getInventory().getContents()) {
                    if(plugin.getItemManager().isRestorationRod(item)) {
                        player.getInventory().remove(item);
                        this.dropItem(player, item);
                    }
                }
            }
        }
    }

    private void dropItem(Player player, ItemStack item) {
        plugin.getScheduler().runTask(() -> {
            Item drop = player.getWorld().dropItemNaturally(player.getLocation(), item);
            drop.setPickupDelay(40);
            User user = SimpleUser.getInstance(player);
            user.sendMessage(plugin, "itemrestoration.playerdroprestorationrod");
        });
    }

    @EventHandler
    public void onRestorationRodPlacement (BlockPlaceEvent e) {
        if(plugin.getItemManager().isRestorationRod(e.getItemInHand())){
            e.setCancelled(true);
        }
        return;
    }

}
