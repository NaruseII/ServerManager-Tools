package fr.naruse.tools.commands;

import fr.naruse.servermanager.core.ServerManager;
import fr.naruse.servermanager.core.connection.packet.IPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketStaffChat implements IPacket {

    private String msg;

    public PacketStaffChat() {
    }

    public PacketStaffChat(String msg) {
        this.msg = msg;
    }

    @Override
    public void write(DataOutputStream stream) throws IOException {
        stream.writeUTF(this.msg);
    }

    @Override
    public void read(DataInputStream stream) throws IOException {
        this.msg = stream.readUTF();
    }

    @Override
    public void process(ServerManager serverManager) {

    }

    public String getMessage() {
        return this.msg;
    }
}
