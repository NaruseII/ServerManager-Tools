package fr.naruse.tools.commands;

import fr.naruse.servermanager.core.connection.packet.PacketSwitchServer;
import fr.naruse.servermanager.core.server.Server;
import fr.naruse.servermanager.core.server.ServerList;
import fr.naruse.tools.main.ToolsPlugin;
import fr.naruse.tools.packets.PacketTeleportToPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class TeleportHereCommand extends AbstractCommand {

    public TeleportHereCommand(ToolsPlugin pl) {
        super(pl, CompleteType.PLAYER);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!sender.hasPermission("servermanager.tools.commands.tphere") && sender instanceof Player){
            return sendMessage(sender, "§4You do not have the permission.");
        }

        Player p = (Player) sender;

        if(args.length == 0){
            return sendMessage(sender, "§6/§7tphere <Player>");
        }

        Player player = Bukkit.getPlayer(args[0]);
        if(player != null){
            player.teleport(p);
        }else if(ServerList.isPlayerOnline(args[0])){

            Optional<Server> optionalPlayerServer = ServerList.findPlayerBukkitServer(p.getUniqueId());
            Optional<Server> optionalTargetBungee = ServerList.findPlayerProxyServer(args[0]);

            if(!optionalTargetBungee.isPresent()){
                return sendMessage(sender, "§cThis player need to be on a Proxy to be teleported to another server!");
            }

            if(optionalPlayerServer.isPresent()){
                optionalTargetBungee.get().sendPacket(new PacketSwitchServer(optionalPlayerServer.get(),args[0]));
                pl.getProcessPacketListener().processAllPackets(new PacketTeleportToPlayer(args[0], p.getName()));
            }else{
                return sendMessage(sender, "§cYour server is not found!");
            }

        }else{
            return sendMessage(sender, "§cPlayer not found.");
        }

        return sendMessage(sender, "§6Teleportation from §7"+args[0]+"§6 to you.");
    }
}
