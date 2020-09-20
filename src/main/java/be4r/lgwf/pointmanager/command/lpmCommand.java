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
                        PMMessage.sendMessage("§aプレイヤーに §e" + String.valueOf(givePoint) + "§a ポイントを与えることに成功しました。", MessageType.PLAYER, player);
                    if(type == CommanderType.CONSOLE)
                        PMMessage.sendMessage("You have succeeded in giving the player " + String.valueOf(givePoint) + " points.", MessageType.CONSOLE);

                    //playsound
                    if(type == CommanderType.ADMIN && player != null)
                        PMSound.playPMSound(player, SoundType.SUCCESS);
                    
                    BukkitRunnable task1 = new BukkitRunnable() {
                        @Override
                        public void run() {
                            for(Player p : Main.getPlugin().getServer().getOnlinePlayers()){
                                if(p.getUniqueId().toString().replaceAll("-", "").equals(uuid)){
                                    if(!player.getUniqueId().toString().replaceAll("-", "").equals(uuid)){
                                        if(givePoint >= 0){
                                            PMMessage.sendMessage("§e " + String.valueOf(givePoint) + "§a ポイントを受け取りました。", MessageType.PLAYER, p);
                                            PMSound.playPMSound(p, SoundType.CONGRATULATIONS);
                                        }else
                                            PMMessage.sendMessage("§e " + String.valueOf(givePoint * -1) + "§a ポイントを取り上げられました。", MessageType.PLAYER, p);
                                    }
                                }
                            }      
                        }
                    };
                    task1.runTask(Main.getPlugin());

                } catch (SQLException e) {//------------Error message-----------
                        //send message
                        if(type == CommanderType.ADMIN && player != null){
                            PMMessage.sendMessage("§c！！！プレイヤーにポイントを与えることに失敗しました！！！", MessageType.PLAYER, player);
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
                        PMMessage.sendMessage("§c！！！プレイヤーのUUID情報の取得に失敗しました！！！", MessageType.PLAYER, player);
                        PMMessage.sendMessage("§c間違ったIDが指定された可能性があります。", MessageType.PLAYER, player);
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
                            PMMessage.sendMessage("§aあなたは §e" + String.valueOf(point) + "§a ポイントを所持しています。", MessageType.PLAYER, player);
                        else
                            PMMessage.sendMessage("§aそのプレイヤーは §e" + String.valueOf(point) + "§a ポイントを所持しています。", MessageType.PLAYER, player);
                    }
                    if(type == CommanderType.CONSOLE)
                        PMMessage.sendMessage("They have " + String.valueOf(point) + " points.", MessageType.CONSOLE);

                    //playsound
                    if(player != null)
                        PMSound.playPMSound(player, SoundType.SUCCESS);

                } catch (SQLException e) {//------------Error message-----------
                        //send message
                        if(player != null){
                            PMMessage.sendMessage("§c！！！ポイントの取得に失敗しました！！！", MessageType.PLAYER, player);
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
                        PMMessage.sendMessage("§c！！！プレイヤーのUUID情報の取得に失敗しました！！！", MessageType.PLAYER, player);
                        PMMessage.sendMessage("§c間違ったIDが指定された可能性があります。", MessageType.PLAYER, player);
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
                        PMMessage.sendMessage("§bあなたはポイントを所持していません。", MessageType.PLAYER, player);
                        PMSound.playPMSound(player, SoundType.ERROR);
                        return;
                    }
                    if(emptySlots == 0){
                        PMMessage.sendMessage("§bインベントリにアイテムを受け取るための十分なスペースがありません。", MessageType.PLAYER, player);
                        PMSound.playPMSound(player, SoundType.ERROR);
                        return;
                    }
                    
                    SQLDriverManager.addPlayerPoint(uuid, emptySlots * -1);
                    
                    BukkitRunnable itemTask = new BukkitRunnable() {
                        @Override
                        public void run() {
                            ItemStack itemStack = Main.item;
                            int IPoint = emptySlots >= point ? point : emptySlots;
                            itemStack.setAmount(IPoint);
                            player.getInventory().addItem(itemStack);
                            PMMessage.sendMessage("§e " + IPoint + "§b ポイントのアイテム化に成功しました。", MessageType.PLAYER, player);
                            PMSound.playPMSound(player, SoundType.CONGRATULATIONS);
                        }
                    };
                    itemTask.runTask(Main.getPlugin());
                    
                } catch (SQLException e) {//------------Error message-----------
                        //send message
                        PMMessage.sendMessage("§c！！！プレイヤーのポイントを変更することに失敗しました！！！", MessageType.PLAYER, player);
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
                    PMMessage.sendMessage("§c！！！プレイヤーのUUID情報の取得に失敗しました！！！", MessageType.PLAYER, sender);
                    PMMessage.sendMessage("§c間違ったIDが指定された可能性があります。", MessageType.PLAYER, sender);
                    //playsound
                    PMSound.playPMSound(sender, SoundType.ERROR);
                    
                    return;
                }
                
                try{
                    int S_point = SQLDriverManager.getPlayerPoint(S_uuid);
                    
                    if(S_point == 0){
                        PMMessage.sendMessage("§bあなたはポイントを所持していません。", MessageType.PLAYER, sender);
                        PMSound.playPMSound(sender, SoundType.ERROR);
                        return;
                    }
                    
                    SQLDriverManager.addPlayerPoint(S_uuid, givePoint * -1);
                    
                    int A_S_Point = SQLDriverManager.getPlayerPoint(S_uuid);
                    
                    int point = S_point - A_S_Point;
                    
                    SQLDriverManager.addPlayerPoint(R_uuid, point);
                    
                    //send message
                    PMMessage.sendMessage("§a" + receiver + " に §e" + String.valueOf(point) + "§a ポイントを送信しました。", MessageType.PLAYER, sender);
                    
                    //playsound
                    PMSound.playPMSound(sender, SoundType.SUCCESS);
                    
                    BukkitRunnable task = new BukkitRunnable() {
                        @Override
                        public void run() {
                            for(Player player : Main.getPlugin().getServer().getOnlinePlayers()){
                                if(player.getName().equals(receiver)){
                                    PMMessage.sendMessage("§b" + sender.getName() + " から §e" + String.valueOf(point) + "§a ポイントを受け取りました。.", MessageType.PLAYER, player);
                                    PMSound.playPMSound(player, SoundType.CONGRATULATIONS);
                                }
                            }      
                        }
                    };
                    task.runTask(Main.getPlugin());

                } catch (SQLException e) {//------------Error message-----------
                        //send message
                        PMMessage.sendMessage("§c！！！ポイントの送信に失敗しました！！！", MessageType.PLAYER, sender);
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
