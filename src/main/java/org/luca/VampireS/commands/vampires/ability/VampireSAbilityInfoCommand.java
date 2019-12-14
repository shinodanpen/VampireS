package org.luca.VampireS.commands.vampires.ability;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.luca.VampireS.VampireSPlugin;
import org.luca.VampireS.commands.vampires.VampireSAbilityCommand;
import org.valdi.SuperApiX.bukkit.commands.CompositeCommand;
import org.valdi.SuperApiX.bukkit.users.SimpleUser;
import org.valdi.SuperApiX.bukkit.users.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.luca.VampireS.VampireSPlugin.PREFIX;
import static org.luca.VampireS.VampireSPlugin.PREFIX_VALUE;
import static org.luca.VampireS.commands.vampires.VampireSAbilityCommand.*;

public class VampireSAbilityInfoCommand extends CompositeCommand<VampireSPlugin> {

    public VampireSAbilityInfoCommand(VampireSAbilityCommand parent) {
        super(parent, "info");
    }

    @Override
    public void setup() {
        setPermission("ability.info"); // perm: vampires.commands.ability.set
    }

    @Override
    public boolean execute(User user, String label, List<String> args) {
        if (args.isEmpty()) {
            return showHelp(this, user);
        }

        User target = user;
        if (args.size() == 1) {
            Player player = Bukkit.getPlayer(args.get(0));
            if (player == null) {
                user.sendMessage(null, "general.unknown-player");
                return false;
            }

            target = SimpleUser.getInstance(player);
        }

        if(args.size() == 1 && user.isConsole()) {
            return showHelp(this, user);
        }

        if(plugin.getStoptimevampire().contains(target.getPlayer().getUniqueId())){

            user.sendMessage(plugin, "commands.vampires.ability.info", PREFIX, PREFIX_VALUE,
                    ABILITY, ABILITY_STOPTIME,
                    "[name]", target.getPlayer().getDisplayName());
            return true;
        }


        if(plugin.getFixitemvampire().contains(target.getPlayer().getUniqueId())){

            user.sendMessage(plugin, "commands.vampires.ability.info", PREFIX, PREFIX_VALUE,
                    ABILITY, ABILITY_FIXITEM,
                    "[name]", target.getPlayer().getDisplayName());
            return true;
        }

        if(!plugin.getStoptimevampire().contains(target.getPlayer().getUniqueId())
                && !plugin.getFixitemvampire().contains(target.getPlayer().getUniqueId())) {

            user.sendMessage(plugin, "commands.vampires.ability.vampirehasnoability", PREFIX, PREFIX_VALUE,
                    "[name]", target.getPlayer().getDisplayName());
            return false;
        }


        return showHelp(this, user);
    }

    @Override
    public Optional<List<String>> tabComplete(User user, String alias, List<String> args) {
        return Optional.of(Bukkit.getOnlinePlayers().stream()
                .filter(p -> user.getPlayer().canSee(p))
                .map(Player::getName)
                .collect(Collectors.toList()));
    }

}

