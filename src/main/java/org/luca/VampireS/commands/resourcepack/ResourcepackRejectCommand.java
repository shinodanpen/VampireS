package org.luca.VampireS.commands.resourcepack;

import org.luca.VampireS.VampireSPlugin;
import org.luca.VampireS.commands.ResourcepackCommand;
import org.valdi.SuperApiX.bukkit.commands.CompositeCommand;
import org.valdi.SuperApiX.bukkit.users.User;
import org.valdi.SuperApiX.bukkit.users.locale.TextVariables;

import java.util.List;

public class ResourcepackRejectCommand extends CompositeCommand<VampireSPlugin> {

    public ResourcepackRejectCommand(ResourcepackCommand parent) {
        super(parent, "reject");
    }

    @Override
    public void setup() {
        setPermission("reject");
    }

    @Override
    public boolean execute(User user, String label, List<String> args) {
        String perm = this.getPermission() + ".ignore";
        if(user.hasPermission(perm)){
            user.sendMessage(plugin, "resourcepack.join.rejected-successfully", VampireSPlugin.PREFIX, VampireSPlugin.PREFIX_VALUE, TextVariables.PERMISSION, perm);
            return true;
        }

        user.getPlayer().kickPlayer(user.getTranslationOrNothing(plugin, "resourcepack.player-kick", VampireSPlugin.PREFIX, VampireSPlugin.PREFIX_VALUE));
        return true;
    }
}
