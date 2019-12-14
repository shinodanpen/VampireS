package org.luca.VampireS.listeners;

import com.google.common.collect.ImmutableList;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.luca.VampireS.VampireCreator;
import org.luca.VampireS.VampireSPlugin;
import org.luca.VampireS.events.VampireBitingEvent;
import org.valdi.SuperApiX.bukkit.users.SimpleUser;
import org.valdi.SuperApiX.bukkit.users.User;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class VampireProperties implements Listener {
    private final VampireSPlugin plugin;


    public VampireProperties(VampireSPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void VampireDamagedByExternalCause(EntityDamageEvent e) {
        if(e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if(plugin.getVampires().contains(p.getUniqueId()) && e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                e.setCancelled(true);
            }
            if(plugin.getVampires().contains(p.getUniqueId())) {
                switch(e.getCause()) {
                    case FIRE_TICK:
                    case FIRE:
                    case LAVA:
                        e.setDamage(e.getDamage() * 2);
                        break;
                    default:
                        // Ignore other damage types.
                        break;
                }
            }
        }
    }

    @EventHandler
    public void RemoveVampireStatusOnDeath (PlayerDeathEvent e) {
        e.getEntity();
        Player p = (Player) e.getEntity();
        if(plugin.getVampires().contains(p.getUniqueId())) {
            plugin.getVampires().remove(e.getEntity().getUniqueId());
            p.getWorld().playSound(p.getLocation(), Sound.ENTITY_WITHER_DEATH, SoundCategory.MASTER, 0.3F, 1.0F);
            e.setNewExp(0);
            e.setDroppedExp(e.getDroppedExp()+532);
            if(p.getLastDamage()== 40D && p.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.CUSTOM) {
                String displayName = p.getDisplayName();
                User user = SimpleUser.getInstance(p) ;
                e.setDeathMessage(user.getTranslationOrNothing(plugin, "vampiresundamage.deathmessage", "[displayname]", displayName));
                p.setFireTicks(0);
            }
        }
        if(plugin.getStoptimevampire().contains(p.getUniqueId())){
            plugin.getStoptimevampire().remove(p.getUniqueId());
        }
        if(plugin.getFixitemvampire().contains(p.getUniqueId())){
            plugin.getFixitemvampire().remove(p.getUniqueId());
        }
    }

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent e) {
        Player p = e.getPlayer();
        if(plugin.getVampires().contains(p.getUniqueId()) || VampireCreator.isTransforming(p)) {
            if(e.getItem().getType().equals(Material.MILK_BUCKET)) {
                User user = SimpleUser.getInstance(p) ;
                user.sendMessage(plugin, "vampirebitingmessage.drinkingmilk");
                e.setCancelled(true);
            }
        }
    }



    List<Material> noFoods = ImmutableList.of(Material.APPLE, Material.MUSHROOM_STEW,
            Material.BREAD, Material.PORKCHOP, Material.COOKED_PORKCHOP, Material.COD, Material.SALMON,
            Material.TROPICAL_FISH, Material.COOKED_COD, Material.COOKED_SALMON, Material.COOKIE, Material.MELON,
            Material.DRIED_KELP, Material.BEEF, Material.COOKED_BEEF, Material.CHICKEN, Material.COOKED_CHICKEN,
            Material.CARROT, Material.POTATO, Material.BAKED_POTATO, Material.PUMPKIN_PIE, Material.RABBIT,
            Material.COOKED_RABBIT, Material.RABBIT_STEW, Material.MUTTON, Material.COOKED_MUTTON, Material.BEETROOT,
            Material.BEETROOT_SOUP, Material.CHORUS_FRUIT, Material.GOLDEN_APPLE, Material.ENCHANTED_GOLDEN_APPLE);

    @EventHandler
    public void onVampireNormalFoodEating(PlayerItemConsumeEvent e) {
        Player p = e.getPlayer();
        if(plugin.getVampires().contains(p.getUniqueId()) && noFoods.contains(e.getItem().getType())) {
            p.setFoodLevel(p.getFoodLevel());
            p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 600, 1, false, false, false));User user = SimpleUser.getInstance(p) ;
            user.sendMessage(plugin, "vampirebitingmessage.eatingfooditem");

            if(e.getItem().getType() == Material.GOLDEN_APPLE || e.getItem().getType() == Material.ENCHANTED_GOLDEN_APPLE) {
                plugin.getScheduler().scheduleSyncDelayedTask(() -> {
                    p.removePotionEffect(PotionEffectType.ABSORPTION);
                    p.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
                }, 1L, TimeUnit.SECONDS);
            }
        }
    }

    @EventHandler
    public void onVampireCakeBlockFoodEating (PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(plugin.getVampires().contains(p.getUniqueId()) && e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.CAKE ) {
                e.setCancelled(true);
                p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 600, 1, false, false, false));
                User user = SimpleUser.getInstance(p) ;
                user.sendMessage(plugin, "vampirebitingmessage.eatingfooditem");
                p.setFoodLevel(p.getFoodLevel());
            }
        }
    }

    public static List<EntityType> vampireFood = ImmutableList.of(EntityType.BAT, EntityType.CHICKEN,
            EntityType.COD, EntityType.COW, EntityType.DONKEY, EntityType.HORSE,
            EntityType.MUSHROOM_COW, EntityType.MULE, EntityType.OCELOT, EntityType.PARROT,
            EntityType.PIG, EntityType.RABBIT, EntityType.SHEEP,
            EntityType.SALMON, EntityType.SQUID, EntityType.TURTLE, EntityType.TROPICAL_FISH,
            EntityType.VILLAGER, EntityType.DOLPHIN,
            EntityType.WOLF, EntityType.GUARDIAN, EntityType.ELDER_GUARDIAN, EntityType.EVOKER,
            EntityType.VINDICATOR, EntityType.WITCH, EntityType.SPIDER, EntityType.LLAMA,
            EntityType.POLAR_BEAR, EntityType.PUFFERFISH, EntityType.CREEPER, EntityType.CAVE_SPIDER, EntityType.SILVERFISH);

    public static List<EntityType> vampireEnemy = ImmutableList.of(EntityType.ZOMBIE, EntityType.ZOMBIE_HORSE, EntityType.ZOMBIE_VILLAGER,
            EntityType.BLAZE, EntityType.PIG_ZOMBIE, EntityType.ENDERMAN, EntityType.ENDERMITE, EntityType.DROWNED,
            EntityType.GHAST, EntityType.HUSK, EntityType.MAGMA_CUBE, EntityType.PHANTOM,
            EntityType.SHULKER, EntityType.SKELETON, EntityType.SKELETON_HORSE, EntityType.STRAY, EntityType.VEX, EntityType.WITHER_SKELETON,
            EntityType.SNOWMAN, EntityType.IRON_GOLEM, EntityType.WITHER);

    @EventHandler
    public void onVampirePeacefulEntityHurt(EntityDamageByEntityEvent e) {
        if(!(e.getEntity() instanceof LivingEntity) || !(e.getDamager() instanceof Player)) {
            return;
        }

        LivingEntity damageTaker = (LivingEntity) e.getEntity();
        Player p = (Player) e.getDamager();
        if(plugin.getVampires().contains(p.getUniqueId())
                && e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK
                && p.getInventory().getItemInMainHand().getType() == Material.AIR) {
            if(vampireFood.contains(damageTaker.getType())) {
                VampireBitingEvent event = new VampireBitingEvent(p, damageTaker);
                Bukkit.getPluginManager().callEvent(event);
                if(event.isCancelled()) {
                    return;
                }
                int vampireFoodLevel = p.getFoodLevel();
                if(vampireFoodLevel < 20 ) {
                    p.setFoodLevel(vampireFoodLevel + 6);
                    double takerHealth = damageTaker.getHealth();
                    damageTaker.setHealth(takerHealth / 2);
                    p.getWorld().playSound(p.getLocation(), "vampires.vampirefoodbite", SoundCategory.MASTER, 1.0F, 1.0F);
                    User user = SimpleUser.getInstance(p) ;
                    user.sendMessage(plugin, "vampirebitingmessage.bitingfoodentity");
                }
            }
        }
    }

    @EventHandler
    public void onVampireUndeadEntityHurt(EntityDamageByEntityEvent e) {
        if(!(e.getEntity() instanceof LivingEntity) || !(e.getDamager() instanceof Player)) {
            return;
        }

        LivingEntity damageTaker = (LivingEntity) e.getEntity();
        Player p = (Player) e.getDamager();
        if(plugin.getVampires().contains(p.getUniqueId())
                && e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK
                && p.getInventory().getItemInMainHand().getType() == Material.AIR) {
            int vampireFoodLevel = p.getFoodLevel();
            if(vampireEnemy.contains(damageTaker.getType()) && vampireFoodLevel < 20) {
                VampireBitingEvent event = new VampireBitingEvent(p, damageTaker);
                Bukkit.getPluginManager().callEvent(event);
                if(event.isCancelled()) {
                    return;
                }
                if(damageTaker.getType() == EntityType.BLAZE
                        || damageTaker.getType() == EntityType.MAGMA_CUBE) {
                    p.setFireTicks(100);
                }
                if(damageTaker.getType() == EntityType.ENDERMAN
                        || damageTaker.getType() == EntityType.ENDERMITE
                        || damageTaker.getType() == EntityType.SHULKER) {
                    Random r = new Random();
                    double xAxis = r.nextInt(20)-10;
                    double zAxis = r.nextInt(20)-10;

                    p.teleport(p.getLocation().add(xAxis, 0, zAxis), PlayerTeleportEvent.TeleportCause.PLUGIN);
                }
                if(damageTaker.getType() == EntityType.PHANTOM) {
                    PotionEffect effect2 = new PotionEffect(PotionEffectType.BLINDNESS, 200, 0, false, false, false);
                    p.addPotionEffect(effect2);
                }
                if(damageTaker.getType() == EntityType.STRAY
                        || damageTaker.getType() == EntityType.SNOWMAN) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 140, 1, false, false, false));
                }
                if(damageTaker.getType() == EntityType.VEX) {
                    PotionEffect effect5 = new PotionEffect(PotionEffectType.CONFUSION, 280, 4, false, false, false);
                    p.addPotionEffect(effect5);
                }
                if(damageTaker.getType() == EntityType.WITHER_SKELETON
                        || damageTaker.getType() == EntityType.WITHER) {
                    PotionEffect effect4 = new PotionEffect(PotionEffectType.WITHER, 100, 5, false, false, false);
                    p.addPotionEffect(effect4);
                }
                PotionEffect effect1 = new PotionEffect(PotionEffectType.HUNGER, 1200, 2, false, false, false);
                p.addPotionEffect(effect1);
                p.getWorld().playSound(p.getLocation(), Sound.ENCHANT_THORNS_HIT, SoundCategory.MASTER, 1.0F, 1.0F);
                User user = SimpleUser.getInstance(p) ;
                user.sendMessage(plugin, "vampirebitingmessage.bitingenemyentity");
            }
        }
    }

    @EventHandler
    public void onPlayerFly(PlayerToggleFlightEvent e) {
        Player p = e.getPlayer();
        if(plugin.getVampires().contains(p.getUniqueId())) {
            if (p.getGameMode() != GameMode.CREATIVE) {
                if(plugin.getConfig().isDoublejumpEnable()) {
                    e.setCancelled(true);
                    p.setAllowFlight(false);
                    p.setFlying(false);
                    p.setVelocity(p.getLocation().getDirection().multiply(2.0D).setY(1.5D));
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 0.4F, 1.0F);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (plugin.getVampires().contains(p.getUniqueId()) && e.getPlayer().getGameMode() != GameMode.CREATIVE
                && p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR) {
            p.setAllowFlight(true);
        }
    }


    @EventHandler
    public void VampireAttackingNeutralMonster (EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Player) {
            Player p = (Player) e.getDamager();
            if(plugin.getVampires().contains(p.getUniqueId()) && e.getEntity() instanceof Monster) {
                Entity entity = e.getEntity();
                if(entity instanceof Monster) {
                    ((Monster) entity).setTarget(p);
                }
            }
        }
    }

    @EventHandler
    public void MonsterNeutralToVampire (EntityTargetEvent e) {
        if(e.getTarget() instanceof Player
                && e.getEntity() instanceof Monster
                && plugin.getVampires().contains(e.getTarget().getUniqueId())) {
            e.setCancelled(true);
            e.setTarget(null);
        }
    }
}
