package org.luca.VampireS.commands.vampires.ability;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.luca.VampireS.VampireSPlugin;
import org.luca.VampireS.commands.vampires.VampireSAbilityCommand;
import org.valdi.SuperApiX.bukkit.commands.CompositeCommand;
import org.valdi.SuperApiX.bukkit.users.SimpleUser;
import org.valdi.SuperApiX.bukkit.users.User;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.luca.VampireS.VampireSPlugin.PREFIX;
import static org.luca.VampireS.VampireSPlugin.PREFIX_VALUE;

public class VampireSAbilityRemoveCommand extends CompositeCommand<VampireSPlugin> {

    public VampireSAbilityRemoveCommand(VampireSAbilityCommand parent) {
        super(parent, "remove");
    }

    @Override
    public void setup() {
        setPermission("ability.remove"); // perm: vampires.commands.ability.remove
    }

    @Override
    public boolean execute(User user, String label, List<String> args) {
        if (args.isEmpty()) {
            return showHelp(this, user);
        }

        User target = user;
        if (args.size() == 2) {
            Player player = Bukkit.getPlayer(args.get(1));
            if (player == null) {
                user.sendMessage(null, "general.unknown-player");
                return false;
            }

            target = SimpleUser.getInstance(player);
        }

        if(args.size() == 1 && user.isConsole()) {
            return showHelp(this, user);
        }

        switch (args.get(0)) {
            case "all": {
                if (plugin.getStoptimevampire().contains(target.getPlayer().getUniqueId())) {
                    plugin.getStoptimevampire().remove(target.getPlayer().getUniqueId());
                }
                if (plugin.getFixitemvampire().contains(target.getPlayer().getUniqueId())) {
                    plugin.getFixitemvampire().remove(target.getPlayer().getUniqueId());
                }


                // TODO aggiungere altre abilità
                user.sendMessage(plugin, "commands.vampires.ability.remove", PREFIX, PREFIX_VALUE,
                        "[name]", target.getPlayer().getDisplayName());
                return true;
            }

            case "stoptime": {
                if (plugin.getStoptimevampire().contains(target.getPlayer().getUniqueId())) {
                    plugin.getStoptimevampire().remove(target.getPlayer().getUniqueId());
                    user.sendMessage(plugin, "commands.vampires.ability.remove", PREFIX, PREFIX_VALUE,
                            "[name]", target.getPlayer().getDisplayName());
                    return true;
                }
                user.sendMessage(plugin, "commands.vampires.ability.donthave", PREFIX, PREFIX_VALUE,
                        "[name]", target.getPlayer().getDisplayName());
                return false;
            }

            case "fixitem": {
                if (plugin.getFixitemvampire().contains(target.getPlayer().getUniqueId())) {
                    plugin.getFixitemvampire().remove(target.getPlayer().getUniqueId());
                    user.sendMessage(plugin, "commands.vampires.ability.remove", PREFIX, PREFIX_VALUE,
                            "[name]", target.getPlayer().getDisplayName());
                    return true;
                }

                user.sendMessage(plugin, "commands.vampires.ability.donthave", PREFIX, PREFIX_VALUE,
                        "[name]", target.getPlayer().getDisplayName());
                return false;
            }
        }

        return showHelp(this, user);
    }

    @Override
    public Optional<List<String>> tabComplete(User user, String alias, List<String> args) {
        if(args.size() == 3) {
            return Optional.of(Arrays.asList("all", "stoptime", "fixitem"));
        }

        return Optional.of(Bukkit.getOnlinePlayers().stream()
                .filter(p -> user.getPlayer().canSee(p))
                .map(Player::getName)
                .collect(Collectors.toList()));
    }

}
