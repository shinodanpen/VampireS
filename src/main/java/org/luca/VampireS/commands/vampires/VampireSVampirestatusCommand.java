package org.luca.VampireS.commands.vampires;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.luca.VampireS.VampireSPlugin;
import org.valdi.SuperApiX.bukkit.commands.CompositeCommand;
import org.valdi.SuperApiX.bukkit.users.SimpleUser;
import org.valdi.SuperApiX.bukkit.users.User;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.luca.VampireS.VampireSPlugin.PREFIX_VALUE;

public class VampireSVampirestatusCommand extends CompositeCommand {

    public VampireSVampirestatusCommand(CompositeCommand parent) {
        super(parent, "vampirestatus");
    }

    @Override
    public void setup() {
        setOnlyPlayer(false);
        setPermission("vampires.command.status");
        setParametersHelp("commands.vampires.status.parameters");
        setDescription("commands.vampires.status.description");
    }

    @Override
    public boolean execute(User user, String label, List<String> args) {
        // execute this code only if the sender is a PLAYER
        if(args.size() == 0) {
            this.showHelp(this, user);
            return false;
        }

        if(!user.isPlayer() && args.size() < 2) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "[prefix] &cConsole must specify a player.")
                    .replace("[prefix]", VampireSPlugin.PREFIX));
            return false;
        }

        User target = user;
        if(args.size() > 1) {
            Player player = Bukkit.getPlayer(args.get(1));
            if(player == null) {
                // TODO message
                return false;
            }

            target = SimpleUser.getInstance(player);
        }

        switch(args.get(0)) {
            case "add":
                if (!((VampireSPlugin) plugin).getVampires().contains(target.getPlayer().getUniqueId())){

                    target.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 60, 255, false, false, false));
                    target.getWorld().playSound(user.getLocation(), Sound.ENTITY_WITHER_BREAK_BLOCK, SoundCategory.MASTER, 0.3F, 0.4F);
                    target.getWorld().playSound(user.getLocation(), Sound.ENTITY_WITHER_AMBIENT, SoundCategory.MASTER, 0.3F, 0.1F);
                    ((VampireSPlugin) plugin).getVampires().add(target.getPlayer().getUniqueId());
                    user.sendMessage(plugin, "commands.vampires.status.add", VampireSPlugin.PREFIX, PREFIX_VALUE,
                            "[name]", Objects.requireNonNull(target.getPlayer()).getDisplayName());
                    break;
                }
                else if(((VampireSPlugin) plugin).getVampires().contains(target.getPlayer().getUniqueId())){
                    user.sendMessage(plugin, "commands.vampires.status.alreadyvampire", VampireSPlugin.PREFIX, PREFIX_VALUE,
                            "[name]", Objects.requireNonNull(target.getPlayer()).getDisplayName());
                    break;
                }

            case "remove":
                if (((VampireSPlugin) plugin).getVampires().contains(target.getPlayer().getUniqueId())) {
                    target.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
                    target.getPlayer().removePotionEffect(PotionEffectType.WATER_BREATHING);
                    target.getPlayer().removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                    target.getPlayer().removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                    target.getPlayer().removePotionEffect(PotionEffectType.FAST_DIGGING);
                    target.getPlayer().removePotionEffect(PotionEffectType.REGENERATION);
                    target.getPlayer().removePotionEffect(PotionEffectType.HEALTH_BOOST);
                    target.getPlayer().removePotionEffect(PotionEffectType.SPEED);
                    target.getPlayer().removePotionEffect(PotionEffectType.DOLPHINS_GRACE);
                    ((VampireSPlugin) plugin).getVampires().remove(target.getPlayer().getUniqueId());
                    user.sendMessage(plugin, "commands.vampires.status.remove", VampireSPlugin.PREFIX, PREFIX_VALUE,
                            "[name]", Objects.requireNonNull(target.getPlayer()).getDisplayName());
                    break;
                }
                else if(!((VampireSPlugin) plugin).getVampires().contains(target.getPlayer().getUniqueId())){
                    user.sendMessage(plugin, "commands.vampires.status.notvampire", VampireSPlugin.PREFIX, PREFIX_VALUE,
                            "[name]", Objects.requireNonNull(target.getPlayer()).getDisplayName());
                    break;
                }
        }
        // magari dai cose sensate come percorsi
        // tipo sempre commands.vampires.give.item-given

        return true; // all right
    }

    @Override
    public Optional<List<String>> tabComplete(User user, String alias, List<String> args) {
        if(args.size() == 2) {
            return Optional.of(Arrays.asList("add", "remove"));
        }

        return Optional.of(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
    }
}
