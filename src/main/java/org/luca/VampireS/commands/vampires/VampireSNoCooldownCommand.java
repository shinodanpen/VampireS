package org.luca.VampireS.commands.vampires;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.luca.VampireS.VampireSPlugin;
import org.valdi.SuperApiX.bukkit.commands.CompositeCommand;
import org.valdi.SuperApiX.bukkit.users.SimpleUser;
import org.valdi.SuperApiX.bukkit.users.User;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class VampireSNoCooldownCommand extends CompositeCommand {

    public VampireSNoCooldownCommand(CompositeCommand parent) {
        super(parent, "cooldown");
    }

    @Override
    public void setup() {
        setOnlyPlayer(false);
        setPermission("vampires.command.nocooldown");
        setParametersHelp("commands.vampires.cooldown.parameters");
        setDescription("commands.vampires.cooldown.description");
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
                    .replace("[prefix]", VampireSPlugin.PREFIX_VALUE));
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
            case "enable":
                ((VampireSPlugin) plugin).noCooldown().remove(target.getUniqueId());
                user.sendMessage(plugin, "commands.vampires.nocooldown.enable", VampireSPlugin.PREFIX, VampireSPlugin.PREFIX_VALUE,
                        "[name]", Objects.requireNonNull(target.getPlayer()).getDisplayName());
                break;
            case "disable":
                ((VampireSPlugin) plugin).noCooldown().add(target.getUniqueId());
                user.sendMessage(plugin, "commands.vampires.nocooldown.disable", VampireSPlugin.PREFIX, VampireSPlugin.PREFIX_VALUE,
                        "[name]", Objects.requireNonNull(target.getPlayer()).getDisplayName());
                break;
        }
        // magari dai cose sensate come percorsi
        // tipo sempre commands.vampires.give.item-given

        return true; // all right
    }

    @Override
    public Optional<List<String>> tabComplete(User user, String alias, List<String> args) {
        if(args.size() == 2) {
            return Optional.of(Arrays.asList("enable", "disable"));
        }

        return Optional.of(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
    }
}
