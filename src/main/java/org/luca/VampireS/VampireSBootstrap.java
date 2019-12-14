package org.luca.VampireS;

import org.valdi.SuperApiX.bukkit.plugin.AbstractBukkitBootstrap;
import org.valdi.SuperApiX.bukkit.versions.Compatibility;
import org.valdi.SuperApiX.bukkit.versions.ServerSoftware;
import org.valdi.SuperApiX.bukkit.versions.ServerVersion;
import org.valdi.SuperApiX.common.PluginDetails;
import org.valdi.SuperApiX.common.annotation.dependency.Dependency;
import org.valdi.SuperApiX.common.annotation.plugin.BukkitPlugin;

@BukkitPlugin(name = "VampireS", version = "1.0", api = BukkitPlugin.Target.v1_14)
@Dependency(PluginDetails.NAME)
public class VampireSBootstrap extends AbstractBukkitBootstrap<VampireSPlugin> {

    public VampireSBootstrap() {
        super();

        this.plugin = new VampireSPlugin(this);
    }

    @Override
    public void onLoad() {
        // Define which server softwares are compatible
        this.registerSoftwareCompatibility(Compatibility.INCOMPATIBLE, ServerSoftware.CRAFTBUKKIT, ServerSoftware.BUKKIT, ServerSoftware.GLOWSTONE);
        this.registerSoftwareCompatibility(Compatibility.NOT_SUPPORTED, ServerSoftware.PAPER, ServerSoftware.TACOSPIGOT, ServerSoftware.AKARIN);
        this.registerSoftwareCompatibility(Compatibility.COMPATIBLE, ServerSoftware.SPIGOT);

        // Define which server versions are compatible
        this.registerVersionCompatibility(Compatibility.INCOMPATIBLE, ServerVersion.values().toArray(new ServerVersion[0]));
        //this.registerVersionCompatibility(Compatibility.NOT_SUPPORTED, ServerVersion.V1_13, ServerVersion.V1_13_1, ServerVersion.V1_13_2);
        this.registerVersionCompatibility(Compatibility.COMPATIBLE, ServerVersion.v1_14, ServerVersion.v1_14_1, ServerVersion.v1_14_2, ServerVersion.v1_14_3, ServerVersion.v1_14_4);

        super.onLoad();

        if(!isCompatible()) {
            getServer().getLogger().severe("Aborting VampireS enabling.");
            getServer().getLogger().severe("VampireS cannot be loaded on this server.");
            getServer().getLogger().severe("You must use a compatible server software (Spigot) and run on a supported version (1.14.x).");
            return;
        }

        if(this.getServerCompatibility().getCompatibility() == Compatibility.NOT_SUPPORTED) {
            // Show a warning
            getServer().getLogger().warning("************ Disclaimer **************");
            getServer().getLogger().warning("VampireS may not be compatible with this server!");
            getServer().getLogger().warning("VampireS is tested only on the latest version of Spigot.");
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();

        if(!isCompatible()) {
            return;
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if(!isCompatible()) {
            return;
        }
    }
}
