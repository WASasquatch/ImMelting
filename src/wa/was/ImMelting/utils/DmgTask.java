package wa.was.ImMelting.utils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Witch;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import wa.was.ImMelting.ImMelting;

public class DmgTask extends BukkitRunnable {

    @SuppressWarnings("unused")
	private final JavaPlugin plugin;
    
    private int cycles;
    private double dmg;
    private Entity entity;
    public static boolean isRunning = false;

    public DmgTask(JavaPlugin plugin, Entity entity) {
        this.plugin = plugin;
        this.cycles = ImMelting.dmgCycles;
        this.dmg = ImMelting.dmgAmount;
        this.entity = entity;
        if ( cycles < 1 ) {
            throw new IllegalArgumentException("[ImMelting]: damage-cycles must be greater than 1");
        }
        if ( dmg < 0.1 ) {
            throw new IllegalArgumentException("[ImMelting]: damage-amount must be greater than 0.1");
        }
    }

    @Override
    public void run() {
        // What you want to schedule goes here
    	isRunning = true;
        if ( cycles > 0 ) { 
            Witch witch = (Witch) entity;
            if ( witch.isValid() ) {
            	witch.damage(dmg);
            } else {
            	isRunning = false;
            	this.cancel();
            }
            cycles--;
        } else {
        	isRunning = false;
            this.cancel();
        }
    }

}