package net.piedmontmc.ffagame;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class FFAListener implements Listener{
	private Main m = Main.getInstance();
	@EventHandler
	public void onLogin(PlayerMoveEvent e) {
		Player p = (Player) e.getPlayer();
		for(Arena a:m.arenas){
			if(a.name.equalsIgnoreCase(m.curArena)){
				for(Location loc : a.mines) {
					if(Math.abs(p.getLocation().getX() - loc.getX()) < 2) {
						if(Math.abs(p.getLocation().getY() - loc.getY()) < 1) {
							if(Math.abs(p.getLocation().getZ() - loc.getZ()) < 2) {
								loc.getWorld().createExplosion(loc, 5.0F);
								
							}
						}
					}
				}
			}
		}
	}
}
