package be4r.lgwf.pointmanager.message;

import be4r.lgwf.pointmanager.Main;
import org.bukkit.entity.Player;

/**
 *
 * @author Be4rJP
 */
public class PMMessage {
    public static void sendMessage(String message, MessageType type){
        String pm = "[§6PointManager§r] ";
        String ncpm = "[PointManager] ";
        
        StringBuilder buff = new StringBuilder();
        
        if(type == MessageType.CONSOLE)
            buff.append(ncpm);
        else
            buff.append(pm);
        buff.append(message);
        
        switch(type){
            case ALL_PLAYER:{
                Main.getPlugin().getServer().getOnlinePlayers().forEach((player) -> {
                    player.sendMessage(buff.toString());
                });
                break;
            }
            case CONSOLE:{
                Main.getPlugin().getServer().getLogger().info(buff.toString());
                break;
            }
            case BROADCAST:{
                Main.getPlugin().getServer().broadcastMessage(buff.toString());
                break;
            }
        }
    }
    
    public static void sendMessage(String message, MessageType type, Player player){
        String pm = "[§6PointManager§r] ";
        StringBuilder buff = new StringBuilder();
        buff.append(pm);
        buff.append(message);
        if(type == MessageType.PLAYER)
            player.sendMessage(buff.toString());
    }
}
