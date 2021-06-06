package fr.naruse.tools.commands;

import fr.naruse.servermanager.core.CoreServerType;
import fr.naruse.servermanager.core.ServerManager;
import fr.naruse.servermanager.core.connection.packet.PacketBroadcast;
import fr.naruse.servermanager.core.server.Server;
import fr.naruse.servermanager.core.server.ServerList;
import fr.naruse.tools.main.ToolsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class StaffChatCommand extends AbstractCommand {

    public StaffChatCommand(ToolsPlugin pl) {
        super(pl);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!sender.hasPermission("servermanager.tools.commands.staffchat")){
            return sendMessage(sender, "§4You do not have the permission.");
        }

        if(args.length == 0){
            return sendMessage(sender, "§6/§7sc <Message>");
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            stringBuilder.append(" ").append(args[i]);
        }
        String msg = "§e§lStaffChat §r§c"+sender.getName()+" §7§l» §r§d"+stringBuilder.toString().trim().replace("&", "§");

        PacketStaffChat packetStaffChat = new PacketStaffChat(msg);

        for (Server server : ServerList.findServer(CoreServerType.BUKKIT_MANAGER, CoreServerType.SPONGE_MANAGER)) {
            if(server.equals(ServerManager.get().getCurrentServer())){
                pl.getProcessPacketListener().processAllPackets(packetStaffChat);
            }else{
                server.sendPacket(packetStaffChat);
            }
        }

        return true;
    }
}
