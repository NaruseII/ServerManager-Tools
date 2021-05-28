package fr.naruse.tools.packets;

import com.google.common.collect.Lists;
import fr.naruse.servermanager.core.ServerManager;
import fr.naruse.servermanager.core.connection.packet.IPacket;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class PacketTeleportToLocation implements IPacket {

    public PacketTeleportToLocation() {
    }

    private List<String> names;
    private Location location;
    private String world;
    public PacketTeleportToLocation(List<String> names, Location location, String world) {
        this.names = names;
        this.location = location;
        this.world = world;
    }

    @Override
    public void write(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeInt(this.names.size());
        for (int i = 0; i < this.names.size(); i++) {
            dataOutputStream.writeUTF(this.names.get(i));
        }
        dataOutputStream.writeUTF(this.world);
        dataOutputStream.writeDouble(this.location.getX());
        dataOutputStream.writeDouble(this.location.getY());
        dataOutputStream.writeDouble(this.location.getZ());
        dataOutputStream.writeFloat(this.location.getYaw());
        dataOutputStream.writeFloat(this.location.getPitch());
    }

    @Override
    public void read(DataInputStream dataInputStream) throws IOException {
        List<String> list = Lists.newArrayList();
        int size = dataInputStream.readInt();
        for(int i = 0; i < size; ++i) {
            list.add(dataInputStream.readUTF());
        }
        this.names = list;

        World world = Bukkit.getWorld(dataInputStream.readUTF());
        if(world == null){
            return;
        }
        this.location = new Location(world, dataInputStream.readDouble(), dataInputStream.readDouble(), dataInputStream.readDouble(), dataInputStream.readFloat(), dataInputStream.readFloat());
    }

    @Override
    public void process(ServerManager serverManager) {

    }

    public List<String> getNames() {
        return names;
    }

    public Location getLocation() {
        return location;
    }
}
