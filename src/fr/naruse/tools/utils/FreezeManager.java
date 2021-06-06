package fr.naruse.tools.utils;

import com.google.common.collect.Lists;
import fr.naruse.servermanager.core.callback.CallbackSingle;
import fr.naruse.servermanager.core.database.Database;
import fr.naruse.tools.main.ToolsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.List;

public class FreezeManager implements Listener {

    public static List<String> FROZEN_LIST = Lists.newArrayList();
    public static final String KEY = "frozenPlayers";

    public FreezeManager(ToolsPlugin pl) {
        Bukkit.getScheduler().runTaskLater(pl, () -> Database.sendGet(KEY, new CallbackSingle() {
            @Override
            public void runSingle(Database.DataObject dataObject) {
                if(dataObject != null){
                    Bukkit.getScheduler().runTask(pl, () -> FROZEN_LIST = Database.DataType.LIST.cast(dataObject.getValue()));
                }
            }
        }), 20*5);
    }

    @EventHandler
    public void move(PlayerMoveEvent e){
        if(FROZEN_LIST.contains(e.getPlayer().getName())){
            e.setCancelled(true);
        }
    }
}
