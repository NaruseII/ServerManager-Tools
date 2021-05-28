package fr.naruse.tools.commands;

import fr.naruse.servermanager.core.server.Server;
import fr.naruse.servermanager.core.server.ServerList;
import fr.naruse.tools.main.ToolsPlugin;
import fr.naruse.tools.packets.PacketKick;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class KickCommand extends AbstractCommand {

    public KickCommand(ToolsPlugin pl) {
        super(pl, CompleteType.PLAYER);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!sender.hasPermission("servermanager.tools.commands.kick")){
            return sendMessage(sender, "§4You do not have the permission.");
        }

        if(args.length < 2){
            return sendMessage(sender, "§6/§7kick <Player> <Reason>");
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            stringBuilder.append(" ").append(args[i]);
        }
        String reason = stringBuilder.toString().trim();

        Player player = Bukkit.getPlayer(args[0]);
        if(player != null){
            player.kickPlayer(reason);
        }else if(ServerList.isPlayerOnline(args[0])){

            Optional<Server> optionalTarget = ServerList.findPlayerBukkitServer(args[0]);

            if(optionalTarget.isPresent()){
                optionalTarget.get().sendPacket(new PacketKick(args[0], reason));
            }else{
                return sendMessage(sender, "§cPlayer in switch/connection. Retry in some seconds.");
            }

        }else{
            return sendMessage(sender, "§cPlayer not found.");
        }

        return sendMessage(sender, "§6Player kicked.");
    }
}
