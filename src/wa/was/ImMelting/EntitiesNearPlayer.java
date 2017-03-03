package wa.was.ImMelting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Witch;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import wa.was.ImMelting.utils.MathHelper;
import wa.was.ImMelting.utils.DmgTask;

public class EntitiesNearPlayer implements Listener {
	
	private static JavaPlugin plugin;
	private static boolean validWorld = false;
	private static List<String> worlds;
	
	public EntitiesNearPlayer() {
		plugin = ImMelting.plugin;
		worlds = ImMelting.config.getStringList("worlds");
	}
	
	@EventHandler
	public void onPlayerMove( PlayerMoveEvent event ) {
		if ( event.isCancelled() ) return;
		Player player = event.getPlayer();
		World world = player.getWorld();
	    for( String str: worlds ) {
	    	if( str.trim().contains(world.getName().toString()) ) {
	    		validWorld = true;
	    	}
	    }
	    if ( ! validWorld ) {
	    	return;
	    }
		List<Entity> nearEntities = getEntitiesAroundPoint(player.getLocation(), ImMelting.radius);
		Iterator<Entity> entityIterator = nearEntities.iterator();
		while ( entityIterator.hasNext() ) {
			Entity entity = entityIterator.next();
			Material m = entity.getLocation().getBlock().getType();
			Biome biome = entity.getWorld().getBiome(entity.getLocation().getBlockX(), entity.getLocation().getBlockZ());
		    if ( m == Material.STATIONARY_WATER || m == Material.WATER ) {
		        // entity is in water
		    	if ( ! ( DmgTask.isRunning ) ) {
		    		damageEntity(entity);
		    	}
		    }
		    if ( world.hasStorm() && isOutside(entity) && biome != Biome.DESERT && biome != Biome.DESERT_HILLS ) {
		    	// is in storm (currently)
		    	if ( ! ( DmgTask.isRunning ) ) {
		    		damageEntity(entity);
		    	}
		    }
		}
	}

	@SuppressWarnings("unused")
	public static void damageEntity ( Entity entity ) {
        if ( ImMelting.dmgRate < 1 ) {
            throw new IllegalArgumentException("[ImMelting]: damage-rate must be greater than 1");
        }
		BukkitTask task = new DmgTask(plugin, entity).runTaskTimer(plugin, 0, ImMelting.dmgRate);
	}
	
    private boolean isOutside(Entity entity) {
        return entity.getLocation().getBlockY() >= entity.getWorld().getHighestBlockYAt(entity.getLocation());
    }
	
	public static List<Entity> getEntitiesAroundPoint( Location location, double radius ) {
	    List<Entity> entities = new ArrayList<Entity>();
	    World world = location.getWorld();

	    int smallX = MathHelper.floor((location.getX() - radius) / 16.0D);
	    int bigX = MathHelper.floor((location.getX() + radius) / 16.0D);
	    int smallZ = MathHelper.floor((location.getZ() - radius) / 16.0D);
	    int bigZ = MathHelper.floor((location.getZ() + radius) / 16.0D);

	    for ( int x = smallX; x <= bigX; x++ ) {
	        for ( int z = smallZ; z <= bigZ; z++ ) {
	            if ( world.isChunkLoaded(x, z) ) {
	               entities.addAll(Arrays.asList(world.getChunkAt(x, z).getEntities()));
	            }
	        }
	    }
	  
	    Iterator<Entity> entityIterator = entities.iterator();
	    while ( entityIterator.hasNext() ) {
	    	Entity entity = entityIterator.next();
	        if ( entity.getLocation().distanceSquared(location) > radius * radius ) { 
	            entityIterator.remove();
	        } else {
	        	if ( ! ( entity instanceof Witch ) ) {
	        		entityIterator.remove();
	        	}
	        }
	    }
	    return entities;
	}
	
}