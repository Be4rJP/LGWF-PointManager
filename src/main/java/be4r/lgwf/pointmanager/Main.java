package be4r.lgwf.pointmanager;

import be4r.lgwf.pointmanager.command.PMCommandExecutor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Be4rJP
 */
public class Main extends JavaPlugin{
        
    private static Main plugin;
    
    public static Config config;
    
    public static boolean lgw = false;
    
    public static ItemStack item;
    
    @Override
    public void onEnable() {
        plugin = this;
        getLogger().info("Starting...");
        getLogger().info("Loading config files...");
        this.saveDefaultConfig();
        config = new Config();
        config.LoadConfig();
        
        lgw = config.getConfig().getString("mode").equalsIgnoreCase("lgw");
        
        if(lgw){
            item = new ItemStack(Material.getMaterial(config.getConfig().getString("item.material")));
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',config.getConfig().getString("item.name")));
            item.setItemMeta(meta);
        }
        
        //setupCommandExecutor
        getCommand("lpm").setExecutor(new PMCommandExecutor());
        getCommand("lpm").setTabCompleter(new PMCommandExecutor());
        
        //RegisteredEvents
        PluginManager pm = getServer().getPluginManager();
        if(!lgw)
            pm.registerEvents(new EventListener(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("Ending...");
        config = null;
    }
    
    public static Main getPlugin(){
        return plugin;
    }
    
    public static boolean isNumber(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
