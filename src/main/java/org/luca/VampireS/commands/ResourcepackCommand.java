package org.luca.VampireS.commands;

import org.luca.VampireS.VampireSPlugin;
import org.luca.VampireS.commands.resourcepack.ResourcepackAcceptCommand;
import org.luca.VampireS.commands.resourcepack.ResourcepackRejectCommand;
import org.valdi.SuperApiX.bukkit.commands.CompositeCommand;
import org.valdi.SuperApiX.bukkit.users.User;
import org.valdi.SuperApiX.bukkit.users.locale.TextVariables;

import java.util.List;

public class ResourcepackCommand extends CompositeCommand<VampireSPlugin> {

    public ResourcepackCommand(VampireSPlugin plugin) {
        super(plugin, "vampiresrp");
    }

    @Override
    public void setup() {
        setPermissionPrefix("vampires.resourcepack");
        setPermission("info");

        setOnlyPlayer(true);

        // Commands
        new ResourcepackAcceptCommand(this);
        new ResourcepackRejectCommand(this);
    }

    @Override
    public boolean execute(User user, String label, List<String> args) {
        if(!args.isEmpty()) {
            user.sendMessage(plugin, "commands.unknown-command", TextVariables.LABEL, getTopLabel());
            return false;
        }

        return showHelp(this, user);
    }
}
