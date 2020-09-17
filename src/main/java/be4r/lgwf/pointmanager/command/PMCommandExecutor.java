package be4r.lgwf.pointmanager.command;

import be4r.lgwf.pointmanager.Main;
import be4r.lgwf.pointmanager.message.MessageType;
import be4r.lgwf.pointmanager.message.PMMessage;
import be4r.lgwf.pointmanager.sound.PMSound;
import be4r.lgwf.pointmanager.sound.SoundType;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

/**
 *
 * @author Be4rJP
 */
public class PMCommandExecutor implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        
        //------------------------Check sender type-----------------------------
        CommanderType type = CommanderType.CONSOLE;
        if(sender instanceof Player){
            if(((Player)sender).hasPermission("lpm.admin"))
                type = CommanderType.ADMIN;
            else
                type = CommanderType.MEMBER;
        }
        //----------------------------------------------------------------------
        
        
        //--------------------------/lpm command--------------------------------
        if(cmd.getName().equalsIgnoreCase("lpm")){
            
            if(args == null) return false;
            if(args.length == 0) return false;

            switch(args[0]){
                case "give":{
                    if(type != CommanderType.MEMBER){
                        if(args.length <= 2) return false;
                        if(!Main.isNumber(args[2])) return false;
                        if(args[1].length() == 32){
                            lpmCommand.givePlayerPoint(args[1], Integer.parseInt(args[2]), type, type == CommanderType.ADMIN ? (Player)sender : null);
                            return true;
                        }else{
                            lpmCommand.givePlayerPointByPlayerName(args[1], Integer.parseInt(args[2]), type, type == CommanderType.ADMIN ? (Player)sender : null);
                            return true;
                        }
                    }
                    break;
                }
                case "point":{
                    if(args.length == 2){
                        if(args[1].length() == 32){
                            lpmCommand.showPlayerPoint(args[1], type, type != CommanderType.CONSOLE ? (Player)sender : null);
                            return true;
                        }else{
                            lpmCommand.showPlayerPointByPlayerName(args[1], type, type != CommanderType.CONSOLE ? (Player)sender : null);
                            return true;
                        }
                    }
                     
                    if(args.length == 1){
                        if(sender instanceof Player){
                            lpmCommand.showPlayerPoint(((Player)sender).getUniqueId().toString().replaceAll("-", ""), type, type != CommanderType.CONSOLE ? (Player)sender : null);
                            return true;
                        }
                    }
                    break;
                }
                case "send":{
                    if(type != CommanderType.CONSOLE){
                        if(args.length <= 2) return false;
                        if(!Main.isNumber(args[2])) return false;
                        if(Integer.parseInt(args[2]) <= 0) return false;
                        if(((Player)sender).getName().endsWith(args[1])){
                            PMMessage.sendMessage("§cYour own name is not accepted.", MessageType.PLAYER, (Player)sender);
                            PMSound.playPMSound((Player)sender, SoundType.ERROR);
                            return true;
                        }
                        
                        lpmCommand.givePoint((Player)sender, Integer.parseInt(args[2]), args[1]);
                        return true;
                    }
                    break;
                }
                case "item":{
                    if(type != CommanderType.CONSOLE && Main.lgw){
                        lpmCommand.givePointItem((Player)sender);
                        return true;
                    }
                    break;
                }
                case "help":{
                    if(type != CommanderType.CONSOLE){
                        PMMessage.sendMessage("§e--------------<Command List>--------------", MessageType.PLAYER, (Player)sender);
                        PMMessage.sendMessage("", MessageType.PLAYER, (Player)sender);
                        if(type == CommanderType.ADMIN)
                            PMMessage.sendMessage("§b/lpm §6give §7<§aPlayerID§7> <§aNumber§7>", MessageType.PLAYER, (Player)sender);
                        PMMessage.sendMessage("§b/lpm §6point §7<§aPlayerID§7 or §aBlank§7>  §7<<--Show points", MessageType.PLAYER, (Player)sender);
                        if(Main.lgw)
                            PMMessage.sendMessage("§b/lpm §6item  §7<<--Exchanged your points for items", MessageType.PLAYER, (Player)sender);
                        PMMessage.sendMessage("§b/lpm §6send §7<§aPlayerID§7> <§aNumber§7> <<--Send points", MessageType.PLAYER, (Player)sender);
                        PMMessage.sendMessage("", MessageType.PLAYER, (Player)sender);
                        PMMessage.sendMessage("§e----------------------------------------", MessageType.PLAYER, (Player)sender);
                    }else{
                        PMMessage.sendMessage("--------------<Command List>--------------", MessageType.CONSOLE);
                        PMMessage.sendMessage("", MessageType.CONSOLE);
                        PMMessage.sendMessage("/lpm give <PlayerID> <Number>", MessageType.CONSOLE);
                        PMMessage.sendMessage("/lpm point <PlayerID or Blank>", MessageType.CONSOLE);
                        PMMessage.sendMessage("", MessageType.CONSOLE);
                        PMMessage.sendMessage("----------------------------------------", MessageType.CONSOLE);
                    }
                    return true;
                }
            }
            
        }
        //----------------------------------------------------------------------
                
        return false;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        
        //------------------------Check sender type-----------------------------
        CommanderType type = CommanderType.CONSOLE;
        if(sender instanceof Player){
            if(((Player)sender).hasPermission("lpm.admin"))
                type = CommanderType.ADMIN;
            else
                type = CommanderType.MEMBER;
        }
        //----------------------------------------------------------------------
        
        if (args.length == 1){
            List<String> list = new ArrayList<>();
            
            list.add("help");
            
            if(type != CommanderType.MEMBER)
                list.add("give");
            
            list.add("point");
            
            if(type != CommanderType.CONSOLE && Main.lgw)
                list.add("item");
            
            if(type != CommanderType.CONSOLE)
                list.add("send");
            
            return list;
        }else if(args.length == 2){
            if(args[0].equals("item")){
                List<String> list = new ArrayList<>();
                return list;
            }
        }else if(args.length == 3){
            List<String> list = new ArrayList<>();
            return list; 
        }
        return null;
    }
}
