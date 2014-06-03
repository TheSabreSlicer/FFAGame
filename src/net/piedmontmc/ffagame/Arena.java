package net.piedmontmc.ffagame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class Arena {
	public String name;
	public ArrayList<Location> spawns = new ArrayList<Location>();
	public ArrayList<Location> mines = new ArrayList<Location>();
	public ArrayList<String> plys = new ArrayList<String>();
	public ArrayList<String> specs = new ArrayList<String>();
	public Map<String, Location> plspawn = new HashMap<>();
	public ArrayList<Location> tempmines = new ArrayList<Location>();
	
	public Arena(String nm){
		name = nm;
	}

	/**
	 * 
	 */
	public void write() {
		FileConfiguration cfg = Main.getInstance().getConfig();
		cfg.set(name + ".spawns",    spawns);
		cfg.set(name + ".mines",     mines);
		cfg.set(name + ".plys",      plys);
		cfg.set(name + ".specs",     specs);
		cfg.set(name + ".plspawn",   plspawn);
		cfg.set(name + ".tempmines", tempmines);
	}

	/**
	 * @param string
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Arena load(String name) {
		Arena arena = new Arena(name);
		FileConfiguration cfg = Main.getInstance().getConfig();
		if (cfg.contains(name)) {
			if (cfg.contains(name + ".spawns")) {
				try {
					arena.spawns = (ArrayList<Location>) cfg.get(name + ".spawns");
				}
				catch (Exception e) { e.printStackTrace(); }
			}
			if (cfg.contains(name + ".mines")) {
				try {
					arena.mines = (ArrayList<Location>) cfg.get(name + ".mines");
				}
				catch (Exception e) { e.printStackTrace(); }
			}
			if (cfg.contains(name + ".plys")) {
				try {
					arena.plys = (ArrayList<String>) cfg.get(name + ".plys");
				}
				catch (Exception e) { e.printStackTrace(); }
			}
			if (cfg.contains(name + ".specs")) {
				try {
					arena.specs = (ArrayList<String>) cfg.get(name + ".specs");
				}
				catch (Exception e) { e.printStackTrace(); }
			}
			if (cfg.contains(name + ".plspawn")) {
				try {
					arena.plspawn = (Map<String, Location>) cfg.get(name + ".plspawn");
				}
				catch (Exception e) { e.printStackTrace(); }
			}
			if (cfg.contains(name + ".tempmines")) {
				try {
					arena.tempmines = (ArrayList<Location>) cfg.get(name + ".tempmines");
				}
				catch (Exception e) { e.printStackTrace(); }
			}
		}
		return arena;
	}
}
