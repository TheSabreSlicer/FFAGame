package net.piedmontmc.ffagame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
		cfg.set(name + ".spawns", spawns);
		cfg.set(name + ".mines", mines);
	}

	/**
	 * @param string
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Arena> load() {
		
		List<Arena> arenas = new ArrayList<Arena>();
		
		for (String key : Main.getInstance().getConfig().getKeys(false)) {
			Arena arena = new Arena(key);
			FileConfiguration cfg = Main.getInstance().getConfig();
			if (cfg.contains(key + ".spawns")) {
				try {
					arena.spawns = (ArrayList<Location>) cfg.getList(key + ".spawns");
				}
				catch (Exception e) { e.printStackTrace(); }
			}
			if (cfg.contains(key + ".mines")) {
				try {
					arena.mines = (ArrayList<Location>) cfg.getList(key + ".mines");
				}
				catch (Exception e) { e.printStackTrace(); }
			}
			arenas.add(arena);
		}
		return arenas;
	}
}
