package net.piedmontmc.ffagame;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class FFAPhysics {
	
	public static void jumpCheck(Player p) {
		if (p.getWorld()
				.getBlockAt(p.getLocation().getBlockX(),
						(p.getLocation().getBlockY() - 1),
						p.getLocation().getBlockZ()).getType().equals(Material.WOOL)) {
			p.getLocation().setY(p.getLocation().getY() + 1);
			p.setVelocity(new Vector(p.getVelocity().getX()*2.0, 0.8, p.getVelocity().getZ()*2.0));
		}
	}
	
	public static void wallCheck(Player p) {
		double x = p.getVelocity().getX();
		double y = p.getVelocity().getY();
		double z = p.getVelocity().getZ();
		
	}
}
