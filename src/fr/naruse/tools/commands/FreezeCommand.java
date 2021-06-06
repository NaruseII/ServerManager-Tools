package fr.naruse.tools.commands;

import com.google.common.collect.Lists;
import fr.naruse.servermanager.core.callback.CallbackSingle;
import fr.naruse.servermanager.core.database.Database;
import fr.naruse.tools.main.ToolsPlugin;
import fr.naruse.tools.utils.FreezeManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class FreezeCommand extends AbstractCommand {

    public FreezeCommand(ToolsPlugin pl) {
        super(pl, CompleteType.PLAYER);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!sender.hasPermission("servermanager.tools.commands.freeze")){
            return sendMessage(sender, "§4You do not have the permission.");
        }

        if(args.length == 0){
            return sendMessage(sender, "§6/§7freeze <Player>");
        }

        String name = args[0];
        Database.sendGet(FreezeManager.KEY, new CallbackSingle() {
            @Override
            public void runSingle(Database.DataObject dataObject) {
                if(dataObject == null){
                    List<String> list = Lists.newArrayList(name);
                    Database.sendPut(FreezeManager.KEY, new Database.DataObject(Database.DataType.LIST, list), true);
                    sendMessage(sender, "§6Player frozen.");
                }else{
                    List<String> list = Database.DataType.LIST.cast(dataObject.getValue());
                    if(list.contains(name)){
                        list.remove(name);
                        Database.sendPut(FreezeManager.KEY, new Database.DataObject(Database.DataType.LIST, list), true);
                        sendMessage(sender, "§6Player unfrozen.");
                    }else{
                        list.add(name);
                        Database.sendPut(FreezeManager.KEY, new Database.DataObject(Database.DataType.LIST, list), true);
                        sendMessage(sender, "§6Player frozen.");
                    }
                }
            }
        });

        return true;
    }
}
