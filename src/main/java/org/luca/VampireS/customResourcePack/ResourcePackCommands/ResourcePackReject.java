package org.luca.VampireS.customResourcePack.ResourcePackCommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.luca.VampireS.MainClass;
import org.luca.VampireS.customResourcePack.CustomResourcePack;

public class ResourcePackReject implements CommandExecutor {

    private final CustomResourcePack addon;

    private final MainClass plugin;

    public ResourcePackReject(CustomResourcePack addon, MainClass plugin) {
        this.addon = addon;
        this.plugin = plugin;
    }

    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            String perm = "vampires.resourcepack.reject";
            if(args.length  > 0 && args[0].equalsIgnoreCase("resourcepack")){
                if(args.length > 1 && args[1].equalsIgnoreCase("reject")){
                    if(p.hasPermission(perm)){
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                plugin.getConfig().getString("customresourcepack.join.rejected-successfully",
                                        "[prefix]&aCustom Resource Pack rejected successfully.")
                                        .replace("[prefix]", plugin.plPrefix).replace("[perm]", perm)));
                        return true;
                    }
                    else{
                        p.kickPlayer(ChatColor.translateAlternateColorCodes
                                ('&', plugin.getConfig().getString("customresourcepack.playerkick",
                                        "[prefix]&4&lERROR: &eYou &o&emust &euse the plugin's custom resource pack. Log in again and &o&eaccpet &e the resource pack.")
                                        .replace("[prefix]", plugin.plPrefix)));
                        return true;
                    }
                }
            }

        }

        else if(sender instanceof ConsoleCommandSender){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfig().getString("commandmessage.console-cannot-perform",
                            "[prefix]&cThis command cannot be executed by the Console.").replace("[prefix]", plugin.plPrefix)));
            return false;
        }
        return true;
    }
}
