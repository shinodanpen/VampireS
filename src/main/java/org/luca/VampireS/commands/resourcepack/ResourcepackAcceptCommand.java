package org.luca.VampireS.commands.resourcepack;

import org.luca.VampireS.VampireSPlugin;
import org.luca.VampireS.commands.ResourcepackCommand;
import org.luca.VampireS.resourcepack.CustomResourcePack;
import org.luca.VampireS.resourcepack.UtilChecksum;
import org.luca.VampireS.resourcepack.UtilToken;
import org.valdi.SuperApiX.bukkit.commands.CompositeCommand;
import org.valdi.SuperApiX.bukkit.users.User;

import java.util.List;

public class ResourcepackAcceptCommand extends CompositeCommand<VampireSPlugin> {

    public ResourcepackAcceptCommand(ResourcepackCommand parent) {
        super(parent, "accept");
    }

    @Override
    public void setup() {
        setPermission("accept");
    }

    @Override
    public boolean execute(User user, String label, List<String> args) {
        CustomResourcePack rp = plugin.getResourcePack();
        String url = "http://" + rp.getWebServer().ip + ":" + rp.getWebServer().port + "/" + UtilToken.getToken(user.getUniqueId());
        user.getPlayer().setResourcePack(url, UtilChecksum.getChecksum(UtilChecksum.fileToByteArray(rp.getWebServer().getFileLocation())));
        return true;
    }
}
