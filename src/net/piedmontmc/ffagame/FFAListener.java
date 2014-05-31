package net.piedmontmc.ffagame;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.util.Vector;

public class FFAListener implements Listener {
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Main m = Main.getInstance();
		if (m.inGame) {
			Player p = (Player) e.getPlayer();
			mineCheck(p, m);
			jumpCheck(p);
		}
		if (m.noMove) {
			e.setCancelled(true);
		}
	}

	public void mineCheck(Player p, Main m) {
		for (Location loc : m.curArena.mines) {
			if (Math.abs(p.getLocation().getX() - loc.getX()) < 2) {
				if (Math.abs(p.getLocation().getY() - loc.getY()) < 1) {
					if (Math.abs(p.getLocation().getZ() - loc.getZ()) < 2) {
						loc.getWorld().createExplosion(loc, 5.0F);
						m.curArena.mines.remove(loc);
					}
				}
			}
		}
	}

	public void jumpCheck(Player p) {
		double x = 0, z = 0, y = 0;
		Vector vector = new Vector(x, z, y);
		Vector v = p.getVelocity();
		if (p.getWorld()
				.getBlockAt(p.getLocation().getBlockX(),
						(p.getLocation().getBlockY() - 1),
						p.getLocation().getBlockZ()).equals(Material.WOOL)) {
			vector.setX(v.getX() * 2);
			vector.setY(v.getY() + 10);
			vector.setZ(v.getZ() * 2);
			p.setVelocity(vector);
		}
	}
}
