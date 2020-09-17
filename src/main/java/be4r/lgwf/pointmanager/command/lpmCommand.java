package be4r.lgwf.pointmanager.command;

import be4r.lgwf.pointmanager.Main;
import be4r.lgwf.pointmanager.UUID;
import be4r.lgwf.pointmanager.message.MessageType;
import be4r.lgwf.pointmanager.message.PMMessage;
import be4r.lgwf.pointmanager.sound.PMSound;
import be4r.lgwf.pointmanager.sound.SoundType;
import be4r.lgwf.pointmanager.sql.SQLDriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Be4rJP
 */
public class lpmCommand {
    
    public static void givePlayerPoint(String uuid, int givePoint, CommanderType type, Player player){
        
        if(type == CommanderType.ADMIN && player != null)
            PMMessage.sendMessage("§bNow trying...", MessageType.PLAYER, player);
        if(type == CommanderType.CONSOLE)
            PMMessage.sendMessage("Now trying...", MessageType.CONSOLE);
        
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                try{
                    SQLDriverManager.addPlayerPoint(uuid, givePoint);
                    
                    //send message
                    if(type == CommanderType.ADMIN && player != null)
                        PMMessage.sendMessage("§aYou have succeeded in giving the player " + String.valueOf(givePoint) + " points.", MessageType.PLAYER, player);
                    if(type == CommanderType.CONSOLE)
                        PMMessage.sendMessage("You have succeeded in giving the player " + String.valueOf(givePoint) + " points.", MessageType.CONSOLE);

                    //playsound
                    if(type == CommanderType.ADMIN && player != null)
                        PMSound.playPMSound(player, SoundType.SUCCESS);

                } catch (SQLException e) {//------------Error message-----------
                        //send message
                        if(type == CommanderType.ADMIN && player != null){
                            PMMessage.sendMessage("§cError!!! You failed to give the player any points.", MessageType.PLAYER, player);
                            PMMessage.sendMessage("§cSQL State: " + e.getSQLState(), MessageType.PLAYER, player);
                            PMMessage.sendMessage("§c" + e.getMessage(), MessageType.PLAYER, player);
                        }
                        if(type == CommanderType.CONSOLE){
                            PMMessage.sendMessage("Error!!! You failed to give the player any points.", MessageType.CONSOLE);
                            PMMessage.sendMessage("SQL State: " + e.getSQLState(), MessageType.CONSOLE, player);
                            PMMessage.sendMessage(e.getMessage(), MessageType.CONSOLE, player);
                        }

                        //playsound
                        if(type == CommanderType.ADMIN && player != null)
                            PMSound.playPMSound(player, SoundType.ERROR);
                        System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
                        
                } catch (Exception e) {
                        e.printStackTrace();
                }
            }
        };
        task.runTaskAsynchronously(Main.getPlugin());
    }
    
    public static void givePlayerPointByPlayerName(String name, int givePoint, CommanderType type, Player player){
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                String uuid = UUID.getUuid(name);
                if(uuid.length() != 32){
                    if(type == CommanderType.ADMIN && player != null){
                        PMMessage.sendMessage("§cError!!! You failed to give the player any points.", MessageType.PLAYER, player);
                        PMMessage.sendMessage("§cFailed to get the player's information.", MessageType.PLAYER, player);
                    }
                    if(type == CommanderType.CONSOLE){
                        PMMessage.sendMessage("Error!!! You failed to give the player any points.", MessageType.CONSOLE);
                        PMMessage.sendMessage("Failed to get the player's information.", MessageType.CONSOLE);
                    }

                    //playsound
                    if(type == CommanderType.ADMIN && player != null)
                        PMSound.playPMSound(player, SoundType.ERROR);
                    
                    return;
                }
                givePlayerPoint(uuid, givePoint, type, player);
            }
        };
        task.runTaskAsynchronously(Main.getPlugin());
    }
    
    
    public static void showPlayerPoint(String uuid, CommanderType type, Player player){
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                try{
                    int point = SQLDriverManager.getPlayerPoint(uuid);
                    
                    //send message
                    if(player != null){
                        if(uuid.equals(player.getUniqueId().toString().replaceAll("-", "")))
                            PMMessage.sendMessage("§aYou have " + String.valueOf(point) + " points.", MessageType.PLAYER, player);
                        else
                            PMMessage.sendMessage("§aThey have " + String.valueOf(point) + " points.", MessageType.PLAYER, player);
                    }
                    if(type == CommanderType.CONSOLE)
                        PMMessage.sendMessage("They have " + String.valueOf(point) + " points.", MessageType.CONSOLE);

                    //playsound
                    if(player != null)
                        PMSound.playPMSound(player, SoundType.SUCCESS);

                } catch (SQLException e) {//------------Error message-----------
                        //send message
                        if(player != null){
                            PMMessage.sendMessage("§cError!!! You failed to get the points.", MessageType.PLAYER, player);
                            PMMessage.sendMessage("§cSQL State: " + e.getSQLState(), MessageType.PLAYER, player);
                            PMMessage.sendMessage("§c" + e.getMessage(), MessageType.PLAYER, player);
                        }
                        if(type == CommanderType.CONSOLE){
                            PMMessage.sendMessage("Error!!! You failed to get the points.", MessageType.CONSOLE);
                            PMMessage.sendMessage("SQL State: " + e.getSQLState(), MessageType.CONSOLE);
                            PMMessage.sendMessage(e.getMessage(), MessageType.CONSOLE);
                        }

                        //playsound
                        if(player != null)
                            PMSound.playPMSound(player, SoundType.ERROR);
                        System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
                        
                } catch (Exception e) {
                        e.printStackTrace();
                }
            }
        };
        task.runTaskAsynchronously(Main.getPlugin());
    }
    
    public static void showPlayerPointByPlayerName(String name, CommanderType type, Player player){
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                String uuid = UUID.getUuid(name);
                if(uuid.length() != 32){
                    if(player != null){
                        PMMessage.sendMessage("§cError!!! You failed to get the points.", MessageType.PLAYER, player);
                        PMMessage.sendMessage("§cFailed to get the player's information.", MessageType.PLAYER, player);
                    }
                    if(type == CommanderType.CONSOLE){
                        PMMessage.sendMessage("Error!!! You failed to get the points.", MessageType.CONSOLE);
                        PMMessage.sendMessage("Failed to get the player's information.", MessageType.CONSOLE);
                    }

                    //playsound
                    if(player != null)
                        PMSound.playPMSound(player, SoundType.ERROR);
                    
                    return;
                }
                showPlayerPoint(uuid, type, player);
            }
        };
        task.runTaskAsynchronously(Main.getPlugin());
    }
    
    public static void givePointItem(Player player){
        
        int empty = 0;
        for(int i = 0; i <= 35; i++){
            if(player.getInventory().getItem(i) == null)
                empty+=64;
        }
        int emptySlots = empty;
        
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                try{
                    String uuid = UUID.getUuid(player.getName());
                    if(uuid.length() != 32) return;
                    
                    int point = SQLDriverManager.getPlayerPoint(uuid);
                    
                    if(point == 0){
                        PMMessage.sendMessage("§bYou have no points.", MessageType.PLAYER, player);
                        PMSound.playPMSound(player, SoundType.ERROR);
                        return;
                    }
                    if(emptySlots == 0){
                        PMMessage.sendMessage("§bThere is not enough space to receive the items.", MessageType.PLAYER, player);
                        PMSound.playPMSound(player, SoundType.ERROR);
                        return;
                    }
                    
                    SQLDriverManager.addPlayerPoint(uuid, emptySlots * -1);
                    
                    BukkitRunnable itemTask = new BukkitRunnable() {
                        @Override
                        public void run() {
                            ItemStack itemStack = Main.item;
                            itemStack.setAmount(emptySlots >= point ? point : emptySlots);
                            player.getInventory().addItem(itemStack);
                            PMMessage.sendMessage("§bYou have successfully exchanged your points for items.", MessageType.PLAYER, player);
                            PMSound.playPMSound(player, SoundType.CONGRATULATIONS);
                        }
                    };
                    itemTask.runTask(Main.getPlugin());
                    
                } catch (SQLException e) {//------------Error message-----------
                        //send message
                        PMMessage.sendMessage("§cError!!! Failed to change the player any points.", MessageType.PLAYER, player);
                        PMMessage.sendMessage("§cSQL State: " + e.getSQLState(), MessageType.PLAYER, player);
                        PMMessage.sendMessage("§c" + e.getMessage(), MessageType.PLAYER, player);

                        //playsound
                        if(player != null)
                            PMSound.playPMSound(player, SoundType.ERROR);
                        System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
                        
                } catch (Exception e) {
                        e.printStackTrace();
                }
            }
        };
        task.runTaskAsynchronously(Main.getPlugin());
    }
    
    public static void givePoint(Player sender, int givePoint, String receiver){
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                String S_uuid = UUID.getUuid(sender.getName());
                String R_uuid = UUID.getUuid(receiver);
                if(S_uuid.length() != 32 || R_uuid.length() != 32){
                    //send message
                    PMMessage.sendMessage("§cFailed to get the player's information.", MessageType.PLAYER, sender);
                    //playsound
                    PMSound.playPMSound(sender, SoundType.ERROR);
                    
                    return;
                }
                
                try{
                    int S_point = SQLDriverManager.getPlayerPoint(S_uuid);
                    
                    if(S_point == 0){
                        PMMessage.sendMessage("§bYou have no points.", MessageType.PLAYER, sender);
                        PMSound.playPMSound(sender, SoundType.ERROR);
                        return;
                    }
                    
                    SQLDriverManager.addPlayerPoint(S_uuid, givePoint * -1);
                    
                    int A_S_Point = SQLDriverManager.getPlayerPoint(S_uuid);
                    
                    int point = S_point - A_S_Point;
                    
                    SQLDriverManager.addPlayerPoint(R_uuid, point);
                    
                    //send message
                    PMMessage.sendMessage("§aYou have successfully transferred " + String.valueOf(point) + " points to a player.", MessageType.PLAYER, sender);
                    
                    //playsound
                    PMSound.playPMSound(sender, SoundType.SUCCESS);
                    
                    BukkitRunnable task = new BukkitRunnable() {
                        @Override
                        public void run() {
                            for(Player player : Main.getPlugin().getServer().getOnlinePlayers()){
                                if(player.getName().equals(receiver)){
                                    PMMessage.sendMessage("§bYou have received " + String.valueOf(point) + " points from " + sender.getName() + ".", MessageType.PLAYER, player);
                                    PMSound.playPMSound(player, SoundType.CONGRATULATIONS);
                                }
                            }      
                        }
                    };
                    task.runTask(Main.getPlugin());

                } catch (SQLException e) {//------------Error message-----------
                        //send message
                        PMMessage.sendMessage("§cError!!! You have failed to transfer points to a player.", MessageType.PLAYER, sender);
                        PMMessage.sendMessage("§cSQL State: " + e.getSQLState(), MessageType.PLAYER, sender);
                        PMMessage.sendMessage("§c" + e.getMessage(), MessageType.PLAYER, sender);
                        //playsound
                        PMSound.playPMSound(sender, SoundType.ERROR);
                        System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
                        
                } catch (Exception e) {
                        e.printStackTrace();
                }
            }
        };
        task.runTaskAsynchronously(Main.getPlugin());
    }
}
