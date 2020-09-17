package be4r.lgwf.pointmanager;

import java.io.File;
import static org.bukkit.Bukkit.getLogger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author Be4rJP
 */
public class Config {
    private FileConfiguration config;
    private File configFile = new File("plugins/PointManager", "config.yml");
    
    public synchronized void LoadConfig(){
        config = YamlConfiguration.loadConfiguration(configFile);
    }
    
    public synchronized void SaveConfig(){
        try{
        }catch(Exception e){
            getLogger().warning("Failed to save config files!");
        }
    }
    
    public FileConfiguration getConfig(){
        return config;
    }
}
