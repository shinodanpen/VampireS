package org.luca.VampireS;

import net.minecraft.server.v1_14_R1.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.valdi.SuperApiX.bukkit.users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemManager {

    public boolean isMask(ItemStack item) {
        if(item == null) {
            return false; // This can't be the item if the hand is empty
        }

        ItemMeta meta = item.getItemMeta();
        return item.getType()== Material.DIAMOND_HOE
                && meta.isUnbreakable()
                && meta instanceof Damageable
                && ((Damageable) meta).getDamage() == 1462
                && meta.getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', "&8&lStone Mask"));
        /* The item is a Stone Mask if it's:
         * a Diamond hoe
         * unbreakable
         * damageable
         * damage to the durability equals 1462
         * the displayname is &8&lStone Mask
         */
    }

    public ItemStack getMask() {
        ItemStack item = new ItemStack(Material.DIAMOND_HOE);
        // ItemMeta -> properties of the item
        ItemMeta meta = item.getItemMeta(); // the plugin gets the existing properties
        // I set a displayname
        assert meta != null;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&8&lStone Mask"));
        // I set a lore
        List<String> stonemasklore = new ArrayList<>();
        stonemasklore.add(ChatColor.translateAlternateColorCodes('&', "&7&oA mysterious mask that possesses a great power"));
        stonemasklore.add(ChatColor.translateAlternateColorCodes('&', "&7&oallowing you to transform into a Vampire."));
        meta.setLore(stonemasklore);
        meta.setUnbreakable(true);
        ((Damageable) meta).setDamage(1462);
        item.setItemMeta(meta); // I apply the properties to the item
        return item;
    }
    /**
     * Give out item to an online player
     * @param player the player
     */
    public void giveStonemaskToPlayer(User player) {
        player.getInventory().addItem(getMask());
        //Material = list of Minecraft items
    }

    public boolean isSilverSword(ItemStack item) {
        if(item == null) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        return item.getType()==Material.IRON_SWORD
                && meta.isUnbreakable()
                && meta instanceof Damageable
                && ((Damageable) meta).getDamage() == 151
                && meta.getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', "&e&lSilver Sword"));
        /* The item is a Silver Sword if it's:
         * an Iron Sword
         * Unbreakable
         * damageable
         * damage to durability equals 151
         * the displayname is &e&lSilver Sword
         */
    }

    public ItemStack getSilverSword() {
        ItemStack item = new ItemStack(Material.IRON_SWORD);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e&lSilver Sword"));
        List<String> silverswordlore = new ArrayList<>();
        silverswordlore.add(ChatColor.translateAlternateColorCodes('&', "&7&oA sword that can damage only Vampires,"));
        silverswordlore.add(ChatColor.translateAlternateColorCodes('&', "&7&oinflicting them more damage."));
        meta.setLore(silverswordlore);
        meta.setUnbreakable(true);
        ((Damageable) meta).setDamage(151);
        item.setItemMeta(meta);
        return item;
    }

    public void giveSilverswordToPlayer(User player) {
        player.getInventory().addItem(getSilverSword());
    }

    public boolean isStopTimeClock(ItemStack item) {
        if(item == null) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        return item.getType()==Material.CLOCK
                && meta.isUnbreakable()
                && meta instanceof Damageable
                && ((Damageable) meta).getDamage() == 151
                && meta.getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', "&6&lStop Time Clock"));
        /* The item is a Stop Time Clock if it's
         * a Clock
         * Unbreakable
         * Damageable
         * damage equals to 151
         * display name is &6&lStop Time Clock
         */
    }

    public ItemStack getStopTimeClock() {
        ItemStack item = new ItemStack(Material.CLOCK);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lStop Time Clock"));
        List<String> stoptimeclocklore = new ArrayList<>();
        stoptimeclocklore.add(ChatColor.translateAlternateColorCodes('&', "&7&oA clock that allows a Vampire"));
        stoptimeclocklore.add(ChatColor.translateAlternateColorCodes('&', "&7&oto stop time for a 10 seconds period."));
        meta.setLore(stoptimeclocklore);
        meta.setUnbreakable(true);
        ((Damageable) meta).setDamage(151);
        item.setItemMeta(meta);

        net.minecraft.server.v1_14_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nmsItem.getOrCreateTag();
        tag.setString("vampireS", UUID.randomUUID().toString());
        nmsItem.setTag(tag);

        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    public void giveStopTimeClock(User player) {
        player.getInventory().addItem(getStopTimeClock());
    }


    public boolean isRestorationRod(ItemStack item) {
        if(item == null) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        return item.getType()==Material.END_ROD
                && meta.isUnbreakable()
                && meta instanceof Damageable
                && ((Damageable) meta).getDamage() == 151
                && meta.getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', "&d&lRestoration Rod"));
        /* The item is a Restoration Rod if it's
         * a END_ROD
         * Unbreakable
         * Damageable
         * damage equals to 151
         * display name is &d&lRestoration Rod
         */
    }

    public ItemStack getRestorationRod() {
        ItemStack item = new ItemStack(Material.END_ROD);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&d&lRestoration Rod"));
        List<String> restorationrodlore = new ArrayList<>();
        restorationrodlore.add(ChatColor.translateAlternateColorCodes('&', "&7&oA magical rod that allows a Vampire"));
        restorationrodlore.add(ChatColor.translateAlternateColorCodes('&', "&7&oto to fix the item in their left hand."));
        meta.setLore(restorationrodlore);
        meta.setUnbreakable(true);
        ((Damageable) meta).setDamage(151);
        item.setItemMeta(meta);

        net.minecraft.server.v1_14_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = nmsItem.getOrCreateTag();
        tag.setString("vampireS", UUID.randomUUID().toString());
        nmsItem.setTag(tag);

        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    public void giveRestorationRod(User player) {
        player.getInventory().addItem(getRestorationRod());
    }
}
