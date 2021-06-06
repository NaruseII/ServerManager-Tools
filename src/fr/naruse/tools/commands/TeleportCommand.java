package fr.naruse.tools.commands;

import fr.naruse.servermanager.core.connection.packet.PacketSwitchServer;
import fr.naruse.servermanager.core.server.Server;
import fr.naruse.servermanager.core.server.ServerList;
import fr.naruse.tools.main.ToolsPlugin;
import fr.naruse.tools.packets.PacketTeleportToPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class TeleportCommand extends AbstractCommand {

    public TeleportCommand(ToolsPlugin pl) {
        super(pl, CompleteType.PLAYER);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!sender.hasPermission("servermanager.tools.commands.tp") && sender instanceof Player){
            return sendMessage(sender, "§4You do not have the permission.");
        }

        Player p = (Player) sender;

        if(args.length == 0){
            return sendMessage(sender, "§6/§7tp <Player>");
        }

        Player player = Bukkit.getPlayer(args[0]);
        if(player != null){
            p.teleport(player);
        }else if(ServerList.isPlayerOnline(args[0])){

            Optional<Server> optionalTarget = ServerList.findPlayerBukkitServer(args[0]);
            Optional<Server> optionalPlayerBungee = ServerList.findPlayerProxyServer(p.getUniqueId());

            if(!optionalPlayerBungee.isPresent()){
                return sendMessage(sender, "§cYou need to be on a Proxy to be teleported to another server!");
            }

            if(optionalTarget.isPresent()){
                optionalPlayerBungee.get().sendPacket(new PacketSwitchServer(optionalTarget.get(), p.getName()));
                optionalTarget.get().sendPacket(new PacketTeleportToPlayer(p.getName(), args[0]));
            }else{
                return sendMessage(sender, "§cPlayer in switch/connection. Retry in some seconds.");
            }

        }else{
            return sendMessage(sender, "§cPlayer not found.");
        }

        return sendMessage(sender, "§6Teleportation to §7"+args[0]+"§6...");
    }
}
