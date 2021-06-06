package fr.naruse.tools.commands;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
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

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

public class TeleportServerHereCommand extends AbstractCommand {

    public TeleportServerHereCommand(ToolsPlugin pl) {
        super(pl, CompleteType.SERVER);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!sender.hasPermission("servermanager.tools.commands.tpserverhere") && sender instanceof Player){
            return sendMessage(sender, "§4You do not have the permission.");
        }

        Player p = (Player) sender;

        if(args.length == 0){
            return sendMessage(sender, "§6/§7tpServerHere <Server name>");
        }

        Server server = ServerList.getByName(args[0]);
        if(server == null){
            return sendMessage(sender, "§cServer not found.");
        }

        int count = 0;
        Map<Server, Set<String>> bungeePlayers = Maps.newHashMap();

        for (String name : server.getData().getUUIDByNameMap().keySet()) {

            Optional<Server> optional = ServerList.findPlayerProxyServer(name);

            if(optional.isPresent()){
                count++;
                if(bungeePlayers.containsKey(optional.get())){
                    bungeePlayers.get(optional.get()).add(name);
                }else{
                    bungeePlayers.put(optional.get(), Sets.newHashSet(name));
                }
            }
        }

        bungeePlayers.forEach((Server server1, Set<String> strings) -> {
            String[] names = strings.toArray(new String[0]);
            server1.sendPacket(new PacketSwitchServer(server, names));
            pl.getProcessPacketListener().processAllPackets(new PacketTeleportToPlayer(Lists.newArrayList(names), p.getName()));
        });

        return sendMessage(sender, "§6Teleportation from §7"+args[0]+"§6 to you. §7("+count+" players)");
    }
}
