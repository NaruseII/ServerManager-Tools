package fr.naruse.tools.main;

import fr.naruse.servermanager.core.ServerManager;
import fr.naruse.servermanager.core.connection.packet.Packets;
import fr.naruse.tools.commands.*;
import fr.naruse.tools.packets.*;
import org.bukkit.plugin.java.JavaPlugin;

public class ToolsPlugin extends JavaPlugin {

    private ToolsProcessPacketListener processPacketListener;

    @Override
    public void onEnable() {
        this.registerCommands();
        this.registerPackets();
    }

    private void registerCommands() {
        this.getCommand("tp").setExecutor(new TeleportCommand(this));
        this.getCommand("tphere").setExecutor(new TeleportHereCommand(this));
        this.getCommand("tpserverhere").setExecutor(new TeleportServerHereCommand(this));
        this.getCommand("kick").setExecutor(new KickCommand(this));
        this.getCommand("find").setExecutor(new FindCommand(this));
        this.getCommand("pmp").setExecutor(new PmpCommand(this));
        this.getCommand("pms").setExecutor(new PmsCommand(this));
        this.getCommand("staffChat").setExecutor(new StaffChatCommand(this));
    }

    private void registerPackets() {
        Packets.registerPacket("TOOLS_TELEPORT_TO_LOC", PacketTeleportToLocation.class);
        Packets.registerPacket("TOOLS_TELEPORT_TO_PLAYER", PacketTeleportToPlayer.class);
        Packets.registerPacket("TOOLS_KICK", PacketKick.class);
        Packets.registerPacket("TOOLS_STAFF_CHAT", PacketStaffChat.class);

        ServerManager.get().registerPacketProcessing(this.processPacketListener = new ToolsProcessPacketListener(this));
    }

    public ToolsProcessPacketListener getProcessPacketListener() {
        return processPacketListener;
    }
}
