package org.luca.VampireS;

import org.valdi.SuperApiX.common.config.advanced.ConfigComment;
import org.valdi.SuperApiX.common.config.advanced.ConfigEntry;
import org.valdi.SuperApiX.common.config.advanced.StoreTo;
import org.valdi.SuperApiX.common.config.advanced.StoredAt;

@StoredAt(filename = "config.yml")
@StoreTo(filename = "config.yml")
// TODO qua dovresti mettere quelle robe come @ConfigComment
public class Settings {

    @ConfigEntry(path = "resourcepack.enable")
    private boolean resourcepackEnabled = true;

    @ConfigEntry(path = "resourcepack.kick-player-on-download-failed")
    private boolean resourcepackKickOnDlFailed = true;

    @ConfigComment("Here goes the NUMBERED ip of the server the plugin is installed in.")
    @ConfigEntry(path = "resourcepack.server-ip")
    private String resourcepackServerIp = "localhost";

    @ConfigComment("The port 41000 is normally unused, but if it's used, insert another port.")
    @ConfigComment("The default port used by the plugin is 41000;")
    @ConfigEntry(path = "resourcepack.server-port")
    private int resourcepackServerPort = 41000;

    @ConfigEntry(path = "stonemask.enable-crafting")
    private boolean stonemaskEnableCrafting = true;

    @ConfigEntry(path = "stonemask.use")
    private boolean stonemaskUse = true;

    @ConfigEntry(path = "stonemask.activation")
    private boolean stonemaskActivation = true;

    @ConfigComment("The option to enable or disable the crafting of the Silver Sword, who can only damage Vampires, inflicting them triple damage.")
    @ConfigEntry(path = "silversword.enable-crafting")
    private boolean silverswordEnableCrafting = true;

    @ConfigComment("The option to enable or disable the Stop Time ability.")
    @ConfigEntry(path = "stoptime.enable")
    private boolean stoptimeEnable = true;

    @ConfigComment("The option to enable or disable the crafting of the Stop Time Clock, required for the activation of the Stop Time ability.")
    @ConfigEntry(path = "stoptime.clock.enable-crafting")
    private boolean stoptimeClockEnableCrafting = true;

    @ConfigComment("The option to enable or disable the Item Restoration ability.")
    @ConfigEntry(path = "fixitem.enable")
    private boolean fixitemEnable = true;

    @ConfigComment("The option to enable or disable the vampire's ability to double jump.")
    @ConfigEntry(path = "doublejump.enable")
    private boolean doublejumpEnable = true;

    public boolean isResourcepackEnabled() {
        return resourcepackEnabled;
    }

    public void setResourcepackEnabled(boolean resourcepackEnabled) {
        this.resourcepackEnabled = resourcepackEnabled;
    }

    public boolean isResourcepackKickOnDlFailed() {
        return resourcepackKickOnDlFailed;
    }

    public void setResourcepackKickOnDlFailed(boolean resourcepackKickOnDlFailed) {
        this.resourcepackKickOnDlFailed = resourcepackKickOnDlFailed;
    }

    public String getResourcepackServerIp() {
        return resourcepackServerIp;
    }

    public void setResourcepackServerIp(String resourcepackServerIp) {
        this.resourcepackServerIp = resourcepackServerIp;
    }

    public int getResourcepackServerPort() {
        return resourcepackServerPort;
    }

    public void setResourcepackServerPort(int resourcepackServerPort) {
        this.resourcepackServerPort = resourcepackServerPort;
    }

    public boolean isStonemaskEnableCrafting() {
        return stonemaskEnableCrafting;
    }

    public void setStonemaskEnableCrafting(boolean stonemaskEnableCrafting) {
        this.stonemaskEnableCrafting = stonemaskEnableCrafting;
    }

    public boolean isStonemaskUse() {
        return stonemaskUse;
    }

    public void setStonemaskUse(boolean stonemaskUse) {
        this.stonemaskUse = stonemaskUse;
    }

    public boolean isStonemaskActivation() {
        return stonemaskActivation;
    }

    public void setStonemaskActivation(boolean stonemaskActivation) {
        this.stonemaskActivation = stonemaskActivation;
    }

    public boolean isSilverswordEnableCrafting() {
        return silverswordEnableCrafting;
    }

    public void setSilverswordEnableCrafting(boolean silverswordEnableCrafting) {
        this.silverswordEnableCrafting = silverswordEnableCrafting;
    }

    public boolean isStoptimeEnable() {
        return stoptimeEnable;
    }

    public void setStoptimeEnable(boolean stoptimeEnable) {
        this.stoptimeEnable = stoptimeEnable;
    }

    public boolean isStoptimeClockEnableCrafting() {
        return stoptimeClockEnableCrafting;
    }

    public void setStoptimeClockEnableCrafting(boolean stoptimeClockEnableCrafting) {
        this.stoptimeClockEnableCrafting = stoptimeClockEnableCrafting;
    }

    public boolean isFixitemEnable() {
        return fixitemEnable;
    }

    public void setFixitemEnable(boolean fixitemEnable) {
        this.fixitemEnable = fixitemEnable;
    }


    //TODO quando aggiungi nuove entry, fai prima la boolean e poi fai alt + inst, getter & setter


    public boolean isDoublejumpEnable() {
        return doublejumpEnable;
    }

    public void setDoublejumpEnable(boolean doublejumpEnable) {
        this.doublejumpEnable = doublejumpEnable;
    }
}
