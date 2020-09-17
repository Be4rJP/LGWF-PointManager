package be4r.lgwf.pointmanager.sql;


import be4r.lgwf.pointmanager.Main;
import java.sql.SQLException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Be4rJP
 */
public class SQLDriverManager {
    public static void addPlayerPoint(Player player, int addPoint) throws Exception{
        String uuid = player.getUniqueId().toString();
        FileConfiguration conf = Main.config.getConfig();
                
        SQLDriver driver = new SQLDriver(conf.getString("MySQL.host"), conf.getString("MySQL.port"), conf.getString("MySQL.database"), conf.getString("MySQL.user"), conf.getString("MySQL.password"), conf.getBoolean("debug"));
        String tableName = conf.getString("MySQL.table");
        driver.execute("CREATE TABLE IF NOT EXISTS " + tableName + " (uuid VARCHAR(32), lgwfp INT);");
        int point = driver.getInt(tableName, "lgwfp", "uuid", uuid, "INSERT INTO " + tableName + "(uuid, lgwfp) VALUES('" + uuid + "', 0);");
        point+=addPoint;
        if(point < 0)
            point = 0;
        driver.updateValue(tableName, "lgwfp = " + String.valueOf(point), "uuid = '" + uuid + "'");
        driver.close();
    }
    
    public static void addPlayerPoint(String uuid, int addPoint) throws Exception{
        FileConfiguration conf = Main.config.getConfig();
        
        SQLDriver driver = new SQLDriver(conf.getString("MySQL.host"), conf.getString("MySQL.port"), conf.getString("MySQL.database"), conf.getString("MySQL.user"), conf.getString("MySQL.password"), conf.getBoolean("debug"));
        String tableName = conf.getString("MySQL.table");
        driver.execute("CREATE TABLE IF NOT EXISTS " + tableName + " (uuid VARCHAR(32), lgwfp INT);");
        int point = driver.getInt(tableName, "lgwfp", "uuid", uuid, "INSERT INTO " + tableName + "(uuid, lgwfp) VALUES('" + uuid + "', 0);");
        point+=addPoint;
        if(point < 0)
            point = 0;
        driver.updateValue(tableName, "lgwfp = " + String.valueOf(point), "uuid = '" + uuid + "'");
        driver.close(); 
    }
    
    public static int getPlayerPoint(String uuid) throws Exception{
        FileConfiguration conf = Main.config.getConfig();
        
        SQLDriver driver = new SQLDriver(conf.getString("MySQL.host"), conf.getString("MySQL.port"), conf.getString("MySQL.database"), conf.getString("MySQL.user"), conf.getString("MySQL.password"), conf.getBoolean("debug"));
        String tableName = conf.getString("MySQL.table");
        driver.execute("CREATE TABLE IF NOT EXISTS " + tableName + " (uuid VARCHAR(32), lgwfp INT);");
        return driver.getInt(tableName, "lgwfp", "uuid", uuid, "INSERT INTO " + tableName + "(uuid, lgwfp) VALUES('" + uuid + "', 0);");
    }
}
