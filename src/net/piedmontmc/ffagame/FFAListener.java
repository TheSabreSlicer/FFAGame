package net.piedmontmc.ffagame;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class FFAListener implements Listener {
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e){
		Player p = e.getEntity();
		Player kl = p.getKiller();
		Main.getInstance().curArena.plys.remove(p.getName());
		Main.getInstance().curArena.specs.add(p.getName());
		Bukkit.broadcastMessage(ChatColor.RED + p.getName() + " was killed by " + kl.getName() + "!");
		for(Player pl:Bukkit.getServer().getOnlinePlayers()){
			pl.playSound(pl.getLocation(), Sound.FIREWORK_LARGE_BLAST, 5.0F, 0.5F);
		}
	}
	@EventHandler
	public void onDeath(PlayerQuitEvent e){
		Player p = e.getPlayer();
		Main.getInstance().curArena.plys.remove(p.getName());
		Main.getInstance().curArena.specs.add(p.getName());
	}
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e){
		e.setRespawnLocation(Main.getInstance().lobby);
	}
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Main m = Main.getInstance();
		Player p = (Player) e.getPlayer();
		if (m.inGame) {
			mineCheck(p, m);
			jumpCheck(p);
		}
		if (m.noMove) {
			e.setCancelled(true);
		}
		if(m.needEnd()){
			m.endGame();
		}
		
		int x = 0;
		int y = 0;
		int z = 0;
		
		Vector vector = new Vector(x, y, z);
		// The following vector calculator is from: https://forums.bukkit.org/threads/ricochet-player-help-detecting-if-player-hits-a-wall.224498/s
        if (p.getEyeLocation().add(p.getVelocity().normalize()).getBlock().getType() != Material.AIR || p.getLocation().add(p.getVelocity().normalize()).getBlock().getType() != Material.AIR) {
            if (p.getEyeLocation().getBlock().getFace(p.getEyeLocation().add(p.getVelocity().normalize()).getBlock()) == BlockFace.UP || p.getLocation().getBlock().getFace(p.getLocation().add(p.getVelocity().normalize()).getBlock()) == BlockFace.DOWN) {
                y = y * (-1);
                p.setMetadata("yVector", new FixedMetadataValue(m, y));
            }
            if (p.getEyeLocation().getBlock().getFace(p.getEyeLocation().add(p.getVelocity().normalize()).getBlock()) == BlockFace.EAST || p.getLocation().getBlock().getFace(p.getLocation().add(p.getVelocity().normalize()).getBlock()) == BlockFace.EAST || p.getEyeLocation().getBlock().getFace(p.getEyeLocation().add(p.getVelocity().normalize()).getBlock()) == BlockFace.WEST || p.getLocation().getBlock().getFace(p.getLocation().add(p.getVelocity().normalize()).getBlock()) == BlockFace.WEST) {
                x = x * (-1);
                p.setMetadata("xVector", new FixedMetadataValue(m, x));
            }
            if (p.getEyeLocation().getBlock().getFace(p.getEyeLocation().add(p.getVelocity().normalize()).getBlock()) == BlockFace.NORTH || p.getLocation().getBlock().getFace(p.getLocation().add(p.getVelocity().normalize()).getBlock()) == BlockFace.NORTH || p.getEyeLocation().getBlock().getFace(p.getEyeLocation().add(p.getVelocity().normalize()).getBlock()) == BlockFace.SOUTH || p.getLocation().getBlock().getFace(p.getLocation().add(p.getVelocity().normalize()).getBlock()) == BlockFace.SOUTH) {
                z = z * (-1);
                p.setMetadata("zVector", new FixedMetadataValue(m, z));
            }
        }

        vector = new Vector(x,y,z);
        p.setVelocity(vector);
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