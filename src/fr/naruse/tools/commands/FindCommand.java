package fr.naruse.tools.commands;

import fr.naruse.servermanager.core.server.Server;
import fr.naruse.servermanager.core.server.ServerList;
import fr.naruse.tools.main.ToolsPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Optional;

public class FindCommand extends AbstractCommand {

    public FindCommand(ToolsPlugin pl) {
        super(pl, CompleteType.PLAYER);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!sender.hasPermission("servermanager.tools.commands.find")){
            return sendMessage(sender, "§4You do not have the permission.");
        }

        if(args.length == 0){
            return sendMessage(sender, "§6/§7find <Player>");
        }

        Optional<Server> optionalBukkit = ServerList.findPlayerBukkitServer(args[0]);
        if(!optionalBukkit.isPresent()){
            return sendMessage(sender, "§4Player §coffline§6.");
        }

        Optional<Server> optionalBungee = ServerList.findPlayerBungeeServer(args[0]);
        if(!optionalBungee.isPresent()){
            return sendMessage(sender, "§4Player §7"+args[0]+" §6 is connected on §a"+optionalBukkit.get().getName()+" §6and on Proxy §a"+optionalBungee.get().getName()+"§6.");
        }

        return sendMessage(sender, "§4Player §7"+args[0]+" §6 is connected on §a"+optionalBukkit.get().getName()+"§6.");
    }
}
