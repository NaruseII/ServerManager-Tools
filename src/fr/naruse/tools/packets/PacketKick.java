package fr.naruse.tools.packets;

import fr.naruse.servermanager.core.ServerManager;
import fr.naruse.servermanager.core.connection.packet.IPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketKick implements IPacket {

    public PacketKick() {
    }

    private String name;
    private String reason;
    public PacketKick(String name, String reason) {
        this.name = name;
        this.reason = reason;
    }

    @Override
    public void write(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeUTF(this.name);
        dataOutputStream.writeUTF(this.reason);
    }

    @Override
    public void read(DataInputStream dataInputStream) throws IOException {
        this.name = dataInputStream.readUTF();
        this.reason = dataInputStream.readUTF();
    }

    @Override
    public void process(ServerManager serverManager) {

    }

    public String getName() {
        return name;
    }

    public String getReason() {
        return reason;
    }
}
