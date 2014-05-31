package net.piedmontmc.ffagame;

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
				
			}
		}
	}
}
