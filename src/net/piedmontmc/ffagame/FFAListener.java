package net.piedmontmc.ffagame;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.util.Vector;

public class FFAListener implements Listener {
	private Main m = Main.getInstance();
	ArrayList<Location> temp = new ArrayList<Location>();

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = (Player) e.getPlayer();
		mineCheck(p);
		jumpCheck(p);
	}

	public void mineCheck(Player p) {
		for (Arena a : m.arenas) {
			if (a.name.equalsIgnoreCase(m.curArena)) {
				for (Location loc : a.mines) {
					if (Math.abs(p.getLocation().getX() - loc.getX()) < 2) {
						if (Math.abs(p.getLocation().getY() - loc.getY()) < 1) {
							if (Math.abs(p.getLocation().getZ() - loc.getZ()) < 2) {
								loc.getWorld().createExplosion(loc, 5.0F);
								temp.add(loc);
								a.mines.remove(loc);
							}
						}
					}
				}
			}
		}
	}

	public void jumpCheck(Player p) {
		double x, z, y;
		Vector vector = new Vector(x, z, y);
		Vector v = p.getVelocity();
		if (p.getWorld().getBlockAt(p.getLocation().getBlockX(),(p.getLocation().getBlockY() - 1),p.getLocation().getBlockZ()).equals(Material.WOOL)) {

		}
	}
}
