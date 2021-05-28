package fr.naruse.tools.commands;

import com.google.common.collect.Lists;
import fr.naruse.servermanager.core.server.Server;
import fr.naruse.servermanager.core.server.ServerList;
import fr.naruse.tools.main.ToolsPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class AbstractCommand implements CommandExecutor, TabExecutor {

    protected ToolsPlugin pl;
    private CompleteType completeType;

    public AbstractCommand(ToolsPlugin pl) {
        this.pl = pl;
    }

    public AbstractCommand(ToolsPlugin pl, CompleteType completeType) {
        this(pl);
        this.completeType = completeType;
    }

    protected boolean sendMessage(CommandSender sender, String msg){
        sender.sendMessage(msg);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if(completeType != null){
            if(completeType == CompleteType.SERVER){
                return ServerList.getAll().stream().filter(server -> server.getName().startsWith(args[args.length-1])).map(server -> server.getName()).collect(Collectors.toList());
            }else if(completeType == CompleteType.PLAYER) {
                List<String> list = Lists.newArrayList();
                for (Server server : ServerList.getAll()) {
                    for (String s1 : server.getData().getUUIDByNameMap().keySet()) {
                        if(s1.startsWith(args[args.length-1])){
                            list.add(s1);
                        }
                    }
                }
                return list;
            }
        }
        return Lists.newArrayList();
    }

    public enum CompleteType {
        PLAYER,
        SERVER
    }
}
