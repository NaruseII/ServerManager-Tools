package fr.naruse.tools.packets;

import com.google.common.collect.Lists;
import fr.naruse.servermanager.core.connection.packet.IPacket;
import fr.naruse.servermanager.core.connection.packet.PacketDatabaseRequest;
import fr.naruse.servermanager.core.connection.packet.PacketDatabaseRequestUpdate;
import fr.naruse.servermanager.core.connection.packet.ProcessPacketListener;
import fr.naruse.servermanager.core.database.Database;
import fr.naruse.servermanager.core.logging.ServerManagerLogger;
import fr.naruse.tools.commands.PacketStaffChat;
import fr.naruse.tools.main.ToolsPlugin;
import fr.naruse.tools.utils.FreezeManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

public class ToolsProcessPacketListener extends ProcessPacketListener {
    private ToolsPlugin pl;

    public ToolsProcessPacketListener(ToolsPlugin pl) {
        this.pl = pl;
    }

    @Override
    public void processDatabaseRequestUpdate(PacketDatabaseRequestUpdate p) {
        PacketDatabaseRequest packet = p.getPacket();
        if(packet.getAction() == PacketDatabaseRequest.Action.PUT && packet.getDataObject() != null && packet.getKey() != null && packet.getKey().equals(FreezeManager.KEY)){
            Bukkit.getScheduler().runTask(pl, () -> FreezeManager.FROZEN_LIST = Database.DataType.LIST.cast(packet.getDataObject().getValue()));
        }
    }

    @Override
    public void processAllPackets(IPacket iPacket) {
        if (iPacket instanceof PacketTeleportToLocation) {
            PacketTeleportToLocation packet = (PacketTeleportToLocation) iPacket;

            this.processTeleportToLocation(packet);
        } else if (iPacket instanceof PacketTeleportToPlayer) {
            PacketTeleportToPlayer packet = (PacketTeleportToPlayer) iPacket;

            this.processTeleportToPlayer(packet);
        } else{
            Bukkit.getScheduler().runTask(pl, () -> {
                if (iPacket instanceof PacketKick) {
                    PacketKick packet = (PacketKick) iPacket;

                    this.processKick(packet);
                }else if (iPacket instanceof PacketStaffChat) {
                    PacketStaffChat packet = (PacketStaffChat) iPacket;

                    this.processStaffChat(packet);
                }
            });
        }
    }

    private void processTeleportToLocation(PacketTeleportToLocation packet) {
        if (packet.getLocation() == null) {
            pl.getLogger().log(Level.SEVERE, "World not found for packet PacketTeleportToLocation.");
            return;
        }

        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                if (packet.getNames().isEmpty() || count > 40) {
                    cancel();
                    return;
                }
                for (String name : Lists.newArrayList(packet.getNames())) {
                    Player player = Bukkit.getPlayer(name);
                    if (player != null) {
                        player.teleport(packet.getLocation());
                        packet.getNames().remove(name);
                    }
                }
                count++;
            }
        }.runTaskTimer(pl, 5, 5);
    }

    private void processTeleportToPlayer(PacketTeleportToPlayer packet) {
        Player target = Bukkit.getPlayer(packet.getTarget());
        if (target == null) {
            pl.getLogger().log(Level.SEVERE, "Target not found for packet PacketTeleportToPlayer.");
            return;
        }

        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                if (packet.getNames().isEmpty() || count > 40) {
                    cancel();
                    return;
                }
                for (String name : Lists.newArrayList(packet.getNames())) {
                    Player player = Bukkit.getPlayer(name);
                    if (player != null && target.isOnline()) {
                        player.teleport(target);
                        packet.getNames().remove(name);
                    }
                }
                count++;
            }
        }.runTaskTimer(pl, 5, 5);
    }

    private void processKick(PacketKick packet) {
        Player player = Bukkit.getPlayer(packet.getName());

        if(player == null){
            pl.getLogger().log(Level.SEVERE, "Player '"+packet.getName()+"' not found for packet PacketKick.");
            return;
        }

        player.kickPlayer(packet.getReason());
    }

    private void processStaffChat(PacketStaffChat packet) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("servermanager.tools.commands.staffchat")) {
                p.sendMessage(packet.getMessage());
            }
        }
    }
}
