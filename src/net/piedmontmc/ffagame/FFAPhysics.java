package net.piedmontmc.ffagame;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class FFAPhysics {

	public static void jumpCheck(Player p) {
		if (p.getWorld()
				.getBlockAt(p.getLocation().getBlockX(),
						(p.getLocation().getBlockY() - 1),
						p.getLocation().getBlockZ()).getType()
				.equals(Material.WOOL)) { // Player proximity detection.
			p.getLocation().setY(p.getLocation().getY() + 1);
			p.setVelocity(new Vector(p.getVelocity().getX() * 3.0, 0.8, p
					.getVelocity().getZ() * 3.0)); // Jump boost.
		}
	}

	public static void wallCheck(Player p) {
		// (EAST) = X increment, -90 yaw
		// (NORTH) = Z decrement, 180/-180 yaw
		// (WEST) = X decrement, 90 yaw
		// (SOUTH) = Z increment, 0 yaw

		if (p.getVelocity().getX() != 0) {
			if (p.getWorld()
					.getBlockAt(p.getLocation().getBlockX() + 1,
							p.getLocation().getBlockY(),
							p.getLocation().getBlockZ()).getType().isSolid()) {
				p.getEyeLocation().setYaw(p.getLocation().getYaw() * (-1));
				p.setVelocity(new Vector(p.getVelocity().getX() * (-2), p
						.getVelocity().getY(), p.getVelocity().getZ() * 2));
			}
			if (p.getWorld()
					.getBlockAt(p.getLocation().getBlockX() - 1,
							p.getLocation().getBlockY(),
							p.getLocation().getBlockZ()).getType().isSolid()) {
				p.getEyeLocation().setYaw(p.getLocation().getYaw() * (-1));
				p.setVelocity(new Vector(p.getVelocity().getX() * (-2), p
						.getVelocity().getY(), p.getVelocity().getZ() * 2));
			}
		}
		if (p.getVelocity().getZ() != 0) {
			if (p.getWorld()
					.getBlockAt(p.getLocation().getBlockX(),
							p.getLocation().getBlockY(),
							p.getLocation().getBlockZ() + 1).getType()
					.isSolid()) {
				if (p.getLocation().getYaw() > 0)
					p.getEyeLocation().setYaw(180 - (p.getLocation().getYaw()));
				if (p.getLocation().getYaw() < 0)
					p.getEyeLocation()
							.setYaw(-180 - (p.getLocation().getYaw()));
				p.setVelocity(new Vector(p.getVelocity().getX() * 2, p
						.getVelocity().getY(), p.getVelocity().getZ() * (-2)));
			}
			if (p.getWorld()
					.getBlockAt(p.getLocation().getBlockX(),
							p.getLocation().getBlockY(),
							p.getLocation().getBlockZ() - 1).getType()
					.isSolid()) {
				if (p.getLocation().getYaw() > 0) {
					p.getEyeLocation().setYaw(p.getLocation().getYaw() * (-1));
					p.setVelocity(new Vector(p.getVelocity().getX() * 2, p
							.getVelocity().getY(), p.getVelocity().getZ()
							* (-2)));
				}
				if (p.getLocation().getYaw() < 0) {
					p.getEyeLocation().setYaw(p.getLocation().getYaw() * (-1));
					p.setVelocity(new Vector(p.getVelocity().getX() * 2, p
							.getVelocity().getY(), p.getVelocity().getZ()
							* (-2)));
				}
			}
		}
	}
}