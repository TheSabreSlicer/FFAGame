package net.piedmontmc.ffagame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

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
}
