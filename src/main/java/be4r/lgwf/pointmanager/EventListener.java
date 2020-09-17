package be4r.lgwf.pointmanager;

import be4r.lgwf.pointmanager.message.MessageType;
import be4r.lgwf.pointmanager.message.PMMessage;
import be4r.lgwf.pointmanager.sql.SQLDriverManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import com.rhetorical.cod.game.events.KillFeedEvent;
import java.sql.SQLException;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Be4rJP
 */
public class EventListener implements Listener {
    
    @EventHandler
    public void onPlayerKilledOnCOM(KillFeedEvent event){
        Player killer = event.getKiller();
        Player victim = event.getVictim();
        if(killer == victim) return;
        
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                try{
                    SQLDriverManager.addPlayerPoint(killer.getUniqueId().toString().replaceAll("-", ""), 1);
                    if(Main.config.getConfig().getBoolean("debug"))
                        PMMessage.sendMessage("You have received 1 point.", MessageType.PLAYER, killer);
                } catch (SQLException e) {//------------Error message-----------
                    PMMessage.sendMessage("Failed to update the player's points.", MessageType.CONSOLE);
                    System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());         
                } catch (Exception e) {
                        e.printStackTrace();
                }
            }
        };
        task.runTaskAsynchronously(Main.getPlugin());
    }
}
