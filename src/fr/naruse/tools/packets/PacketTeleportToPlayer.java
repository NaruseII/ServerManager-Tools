package fr.naruse.tools.packets;

import com.google.common.collect.Lists;
import fr.naruse.servermanager.core.ServerManager;
import fr.naruse.servermanager.core.connection.packet.IPacket;
import org.bukkit.entity.Player;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class PacketTeleportToPlayer implements IPacket {

    public PacketTeleportToPlayer() {
    }

    private List<String> names;
    private String target;
    public PacketTeleportToPlayer(String name, String target) {
        this(Lists.newArrayList(name), target);
    }

    public PacketTeleportToPlayer(List<String> names, String target) {
        this.names = names;
        this.target = target;
    }

    @Override
    public void write(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeInt(this.names.size());
        for (int i = 0; i < this.names.size(); i++) {
            dataOutputStream.writeUTF(this.names.get(i));
        }
        dataOutputStream.writeUTF(this.target);
    }

    @Override
    public void read(DataInputStream dataInputStream) throws IOException {
        List<String> list = Lists.newArrayList();
        int size = dataInputStream.readInt();
        for(int i = 0; i < size; ++i) {
            list.add(dataInputStream.readUTF());
        }
        this.names = list;

        this.target = dataInputStream.readUTF();
    }

    @Override
    public void process(ServerManager serverManager) {

    }

    public List<String> getNames() {
        return names;
    }

    public String getTarget() {
        return target;
    }
}
