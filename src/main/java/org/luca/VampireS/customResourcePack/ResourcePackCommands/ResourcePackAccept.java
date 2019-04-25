package org.luca.VampireS.customResourcePack.ResourcePackCommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.luca.VampireS.MainClass;
import org.luca.VampireS.customResourcePack.CustomResourcePack;

public class ResourcePackAccept implements CommandExecutor {

    private final CustomResourcePack addon;

    private final MainClass plugin;

    public ResourcePackAccept(CustomResourcePack addon, MainClass plugin) {
        this.addon = addon;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(args.length  > 0 && args[0].equalsIgnoreCase("resourcepack")){
                if(args.length > 1 && args[1].equalsIgnoreCase("accept")){

                        return true;
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
