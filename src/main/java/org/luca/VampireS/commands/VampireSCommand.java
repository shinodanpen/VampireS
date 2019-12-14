package org.luca.VampireS.commands;

import org.luca.VampireS.VampireSPlugin;
import org.luca.VampireS.commands.vampires.*;
import org.valdi.SuperApiX.bukkit.commands.CompositeCommand;
import org.valdi.SuperApiX.bukkit.users.User;

import java.util.List;

import static org.luca.VampireS.VampireSPlugin.PREFIX_VALUE;

public class VampireSCommand extends CompositeCommand {

    public VampireSCommand(VampireSPlugin plugin) {
        super(plugin, "vampires", "vampire", "v");
    }

    @Override
    public void setup() {
        setPermissionPrefix("vampires.commands");
        setPermission("info");

        setOnlyPlayer(false);

        // Commands
        new VampireSGiveCommand(this);
        new VampireSGodCommand(this);
        new VampireSVampirestatusCommand(this);
        new VampireSNoCooldownCommand(this);
        new VampiresNoExpRequirementCommand(this);
        new VampireSAbilityCommand(this);
    }

    @Override
    public boolean execute(User user, String label, List<String> args) {
        if(!args.isEmpty()) {
            user.sendMessage(plugin, "commands.unknown-command", VampireSPlugin.PREFIX, PREFIX_VALUE);
            return false;
        }

        return showHelp(this, user);
    }

}
