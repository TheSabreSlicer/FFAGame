package net.piedmontmc.ffagame;

import java.util.ArrayList;

import org.bukkit.Location;

public class Arena {
	public String name;
	public ArrayList<Location> spawns = new ArrayList<Location>();
	public ArrayList<Location> mines = new ArrayList<Location>();
	public ArrayList<String> plys = new ArrayList<String>();
	public ArrayList<String> specs = new ArrayList<String>();
	
	public Arena(String nm){
		name = nm;
	}
}
