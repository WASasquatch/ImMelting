package wa.was.ImMelting;

import java.io.File;
import java.util.List;

import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import wa.was.ImMelting.EntitiesNearPlayer;

public class ImMelting extends JavaPlugin {
	
	public static JavaPlugin plugin;
	public static Server server;
	public static FileConfiguration config;
	public static List<String> validWorlds;
	public static int dmgRate;
	public static int dmgCycles;
	public static double dmgAmount;
	public static double radius;
	
	// Setup globals
	public ImMelting() {
    	plugin = this;
    	server = plugin.getServer();
    	config = plugin.getConfig();
    	validWorlds = config.getStringList("worlds");
    	dmgRate = config.getInt("damage-rate");
    	dmgCycles = config.getInt("damage-cycles");
    	dmgAmount = config.getInt("damage-amount");
    	radius = config.getInt("effect-radius");
	}
	
	// Initialize Event
	@Override
    public void onEnable() {
    	createConfig();
    	server.getPluginManager().registerEvents((Listener) new EntitiesNearPlayer(), plugin);
    }
	
    @Override
    public void onDisable() {
    	// ...
    }
    
    // Create Config
	public void createConfig() {
        try {
            if ( ! getDataFolder().exists() ) {
                getDataFolder().mkdirs();
            }
            File file = new File(getDataFolder(), "config.yml");
            if ( ! file.exists() ) {
            	getLogger().info("Config.yml not found, creating it for you!");
            	saveDefaultConfig();
            } else {
            	getLogger().info("Config.yml found, loading!");
            }
        } catch ( Exception e ) {
            e.printStackTrace();

        }

    }

}
