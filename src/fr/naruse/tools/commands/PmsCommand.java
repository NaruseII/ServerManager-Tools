package fr.naruse.tools.commands;

import fr.naruse.servermanager.core.ServerManager;
import fr.naruse.servermanager.core.connection.packet.PacketBroadcast;
import fr.naruse.servermanager.core.server.Server;
import fr.naruse.servermanager.core.server.ServerList;
import fr.naruse.tools.main.ToolsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class PmsCommand extends AbstractCommand {

    public PmsCommand(ToolsPlugin pl) {
        super(pl, CompleteType.SERVER);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!sender.hasPermission("servermanager.tools.commands.pms")){
            return sendMessage(sender, "§4You do not have the permission.");
        }

        if(args.length < 2){
            return sendMessage(sender, "§6/§7pms <Server> <Message>");
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            stringBuilder.append(" ").append(args[i]);
        }
        String msg = "§4"+sender.getName()+" §7§l» §r§d"+stringBuilder.toString().trim().replace("&", "§");

        Server server = ServerList.getByName(args[0]);
        if(server != null){
            if(server.equals(ServerManager.get().getCurrentServer())){
                Bukkit.broadcastMessage(msg);
                return true;
            }
            server.sendPacket(new PacketBroadcast(msg));
        }else{
            return sendMessage(sender, "§cServer not found.");
        }

        return sendMessage(sender, msg);
    }
}
