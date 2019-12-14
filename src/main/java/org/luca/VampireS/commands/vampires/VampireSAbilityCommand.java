package org.luca.VampireS.commands.vampires;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.luca.VampireS.VampireSPlugin;
import org.luca.VampireS.commands.vampires.ability.VampireSAbilityInfoCommand;
import org.luca.VampireS.commands.vampires.ability.VampireSAbilityRemoveCommand;
import org.luca.VampireS.commands.vampires.ability.VampireSAbilitySetCommand;
import org.valdi.SuperApiX.bukkit.commands.CompositeCommand;
import org.valdi.SuperApiX.bukkit.users.SimpleUser;
import org.valdi.SuperApiX.bukkit.users.User;
import org.valdi.SuperApiX.bukkit.users.locale.TextVariables;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.luca.VampireS.VampireSPlugin.PREFIX;
import static org.luca.VampireS.VampireSPlugin.PREFIX_VALUE;

public class VampireSAbilityCommand extends CompositeCommand<VampireSPlugin> {

    public VampireSAbilityCommand(CompositeCommand parent) {
        super(parent, "ability");
    }

    public static final String ABILITY = "[ability]";
    public static final String ABILITY_STOPTIME = "&6&lStop Time";
    public static final String ABILITY_FIXITEM = "&d&lItem Restoration";

    @Override
    public void setup() {
        setOnlyPlayer(false);

        setPermission("ability");

        // Commands
        new VampireSAbilitySetCommand(this); // questo lo fa
        new VampireSAbilityRemoveCommand(this);
        new VampireSAbilityInfoCommand(this);
    }

    @Override
    public boolean execute(User user, String label, List<String> args) {
        if (!args.isEmpty()) {
            user.sendMessage(plugin, "commands.unknown-command", PREFIX, PREFIX_VALUE);
            return false;
        }

        return showHelp(this, user);
    }
}
