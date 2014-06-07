package net.piedmontmc.ffagame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

// TODO add jumping mechanics, fix jitter
// TODO add spectating...

public class Main extends JavaPlugin implements Listener {
	private static Main main;
	public List<Arena> arenas = new ArrayList<Arena>();
	public Random gen = new Random();
	public Location lobby;
	public boolean inGame = false;
	public boolean noMove = false;
	public Arena curArena = null;
	private Countdown cd;

	public Main() {
		main = this;
	}

	@Override
	public void onEnable() {
		this.cd = new Countdown();
		Bukkit.getPluginManager().registerEvents(this, this);
		//load arena
		//this.reloadConfig();
		//arenas = Arena.load();
	}

	@Override
	public void onDisable() {
		//save arena
		//for (Arena arena : arenas)
		//	arena.write();
		//this.saveConfig();
		//try {
		//	this.getConfig().save(this.getDataFolder() + "/config.yml");
		//} catch (IOException e) {
		//	e.printStackTrace();
		//}
	}

	public void displayHelp(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "FFA Help:");
		sender.sendMessage(ChatColor.GRAY + "To view the help, type: /ffa help");
		sender.sendMessage(ChatColor.GRAY
				+ "To start a game, type: /ffa start <arena>");
		sender.sendMessage(ChatColor.GRAY
				+ "To add a spawn or mine, type: /ffa add <spawn|mine> <arena>");
		sender.sendMessage(ChatColor.GRAY
				+ "To set the lobby, type: /ffa set lobby");
		sender.sendMessage(ChatColor.GRAY
				+ "To reset mines and spawns for an arena, type: /ffa reset <arena>");
		sender.sendMessage(ChatColor.GRAY
				+ "To create an arena, type: /ffa create <name>");
		sender.sendMessage(ChatColor.GRAY
				+ "To remove an arena, type: /ffa remove <name>");
		sender.sendMessage(ChatColor.GRAY
				+ "To list all arenas, type: /ffa list");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("ffa")) { // If the player typed /ffa then do the following...
			
			if (args[0].equalsIgnoreCase("start") && args.length == 2) { // This is /ffa start <arena>
				if (inGame) {
					sender.sendMessage(ChatColor.RED
							+ "The game has already been started!");
					return true;
				}
				if (lobby==null){
					sender.sendMessage(ChatColor.RED + "The lobby has not been set!");
				}
				for (Arena a : arenas) {
					if (a.name.equalsIgnoreCase(args[1])) {
						curArena.plys.clear();
						curArena.specs.clear();
						curArena = a;
						startGame(a);
						return true;
					}
				}
			}
			if (sender instanceof Player) {
				if (args.length == 0) {
					displayHelp(sender);
					return true;
				}
				if (args.length == 1 && args[0].equalsIgnoreCase("help")) { // This /ffa help
					displayHelp(sender);
					return true;
				}
				if (args.length == 2 && args[0].equalsIgnoreCase("create") && sender.isOp()) { // This /ffa create <arena>
					for (Arena a : arenas) {
						if (a.name.equalsIgnoreCase(args[1])) {
							sender.sendMessage(ChatColor.RED
									+ "An arena called " + args[1]
									+ " already exists!");
							return true;
						}
					}
					arenas.add(new Arena(args[1]));
					sender.sendMessage(ChatColor.GREEN + "The arena called "
							+ args[1] + " was created!");
					return true;
				}
				if (args.length == 2 && args[0].equalsIgnoreCase("remove") && sender.isOp()) { // This /ffa remove <arena>
					for (Arena a : arenas) {
						if (a.name.equalsIgnoreCase(args[1])) {
							arenas.remove(a);
							sender.sendMessage(ChatColor.GREEN
									+ "The arena called " + args[1]
									+ " was removed!");
							return true;
						}
					}
					sender.sendMessage(ChatColor.RED + "No arena called "
							+ args[1] + " was found!");
					return true;
				}
				if (args.length == 2 && args[0].equalsIgnoreCase("reset") && sender.isOp()) { // This /ffa reset <arena>
					for (Arena a : arenas) {
						if (a.name.equalsIgnoreCase(args[1])) {
							a.spawns.clear();
							a.mines.clear();
							sender.sendMessage(ChatColor.GREEN
									+ "The arena called " + args[1]
									+ " was reset!");
							return true;
						}
					}
					sender.sendMessage(ChatColor.RED + "No arena called "
							+ args[1] + " was found!");
					return true;
				}
				if (args.length == 2 && args[0].equalsIgnoreCase("set")
						&& args[1].equalsIgnoreCase("lobby") && sender.isOp()) { // This is /ffa set lobby
					Player p = (Player) sender;
					lobby = p.getLocation();
					p.sendMessage(ChatColor.GREEN + "Lobby set!");
					return true;
				}
				if (args.length == 1 && args[0].equalsIgnoreCase("list")) { // This is /ffa list
					sender.sendMessage(ChatColor.GREEN + "All arenas:");
					for (Arena a : arenas) {
						sender.sendMessage(ChatColor.GRAY + a.name + " M:" + a.mines.size() + " S:" + a.spawns.size());
					}
					return true;
				}
				if (args.length == 3 && args[0].equalsIgnoreCase("add") && sender.isOp()) { // This is /ffa add <spawn|mine> <arena>
					Player p = (Player) sender;
					for (Arena a : arenas) {
						if (a.name.equalsIgnoreCase(args[2])) {
							if (args[1].equalsIgnoreCase("spawn")) {
								a.spawns.add(p.getLocation());
								p.sendMessage(ChatColor.GREEN
										+ "Spawn added! There are now "
										+ a.spawns.size() + " spawns in "
										+ a.name + ".");
								return true;
							}
							if (args[1].equalsIgnoreCase("mine")) {
								a.mines.add(p.getLocation());
								p.sendMessage(ChatColor.GREEN
										+ "Mine added! There are now "
										+ a.mines.size() + " mines in "
										+ a.name + ".");
								return true;
							} else {
								p.sendMessage(ChatColor.RED
										+ "The 2nd argument must be spawn or mine!");
								return true;
							}
						}
					}
					p.sendMessage(ChatColor.RED
							+ "The specified arena was not found!");
					return true;
				}
				return false;
			} else {
				sender.sendMessage(ChatColor.RED + "You must be an Operator to use that command!");
			}
		}
		sender.sendMessage("You must be a player to use FFA commands.");
		return true;
	}

	public void startGame(Arena a) {
		if (Bukkit.getServer().getOnlinePlayers().length < 2) {
			Bukkit.broadcastMessage(ChatColor.RED
					+ "There needs to be 2 players to start the game!");
			return;
		}
		if (Bukkit.getServer().getOnlinePlayers().length > a.spawns.size()) {
			Bukkit.broadcastMessage(ChatColor.RED
					+ "There are not enough spawns in this arena for all online players!");
			return;
		}
		inGame = true;
		noMove = true;
		for (Location loc : a.mines) {
			loc.getWorld().getBlockAt(loc).setType(Material.TNT);
			curArena.tempmines.add(loc);
		}
		for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
			curArena.plys.add(pl.getName());
		}
		int i = 0;
		for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
			pl.setHealth(20.0);
			pl.setFoodLevel(20);
			pl.setGameMode(GameMode.SURVIVAL);
			Inventory inv = pl.getInventory();
			inv.clear();
			ItemStack is = new ItemStack(Material.STONE_SWORD, 1);
			inv.addItem(is);
			pl.teleport(curArena.spawns.get(i));
			curArena.plspawn.put(pl.getName(), curArena.spawns.get(i));
			i++;
		}
		countdown().start(5, "Game starting");
	}

	public void endGame() {
		Bukkit.broadcastMessage(ChatColor.BLUE + curArena.plys.get(0)
				+ " has won the game!");
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			p.setHealth(20.0);
			Inventory inv = p.getInventory();
			inv.clear();
			p.teleport(lobby);
			clearArrows(p);
		}
		inGame = false;
		for (Arrow arrow : curArena.spawns.get(0).getWorld().getEntitiesByClass(Arrow.class)) {
			  arrow.remove();
		}
		curArena.mines.clear();
		for(Location loc:curArena.tempmines){
			curArena.mines.add(loc);
		}
		curArena.tempmines.clear();
	}

	public boolean needEnd() {
		if ((curArena.plys.size()==1) && inGame) {
			return true;
		} else {
			return false;
		}
	}

	public static Main getInstance() {
		return main;
	}

	public Countdown countdown() {
		return this.cd;
	}
	@EventHandler
	public void cancelDrops(PlayerDropItemEvent e){
		e.setCancelled(true);
	}
	@EventHandler
	public void onEDE(EntityDamageByEntityEvent e){
		if(e.getDamager() instanceof Player && e.getEntity() instanceof Player && inGame){
			Player kl = (Player) e.getDamager();
			Player p = (Player) e.getEntity();
			Damageable d = (Damageable) p;
			if((d.getHealth() - e.getDamage()<=0)&&curArena.plys.contains(p.getName())&&curArena.plys.contains(kl.getName())){
				e.setCancelled(true);
				curArena.plys.remove(p.getName());
				curArena.specs.add(p.getName());
				Bukkit.broadcastMessage(ChatColor.RED + p.getName() + " was killed by "
						+ kl.getName() + "!");
				for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
					pl.playSound(pl.getLocation(), Sound.FIREWORK_LARGE_BLAST, 5.0F,
							1.0F);
				}
				p.setHealth(20.0);
				return;
			}
		} else {
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onExplode(EntityExplodeEvent e) {
		if (e.isCancelled())
			return;

		List<Block> blockListCopy = new ArrayList<Block>();
		blockListCopy.addAll(e.blockList());
		for (Block block : blockListCopy) {
			e.blockList().remove(block);
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if (e.getPlayer().isOp()&&!inGame)
			e.setCancelled(false);
		else
			e.setCancelled(true);
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if (e.getPlayer().isOp()&&!inGame)
			e.setCancelled(false);
		else
			e.setCancelled(true);
	}

	@EventHandler
	public void onPlayerDamage(final EntityDamageEvent e) {
		if (e.getCause() == DamageCause.FALL) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onDeath(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		curArena.plys.remove(p.getName());
		curArena.specs.add(p.getName());
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		e.setRespawnLocation(lobby);
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (noMove) {
			if(!(p.getLocation().getBlock().equals(curArena.plspawn.get(p.getName())))){
				Location loc = curArena.plspawn.get(p.getName());
				Location ploc = new Location(p.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), p.getLocation().getYaw(), p.getLocation().getPitch());
				p.teleport(ploc);
			}
			return;
		}
		if (inGame && !noMove) {
			mineCheck(p);
			FFAPhysics.jumpCheck(p);
			// wallCheck(p);
		}
		if (needEnd()==true && inGame && !noMove) {
			endGame();
		}
	}

	public void mineCheck(Player p) {
		for (Location loc : curArena.mines) {
			if (Math.abs(p.getLocation().getX() - loc.getX()) < 2) {
				if (Math.abs(p.getLocation().getY() - loc.getY()) < 1) {
					if (Math.abs(p.getLocation().getZ() - loc.getZ()) < 2) {
						loc.getWorld().createExplosion(loc, 10.0F);
						curArena.tempmines.add(loc);
						curArena.mines.remove(loc);
						loc.getWorld().getBlockAt(loc).setType(Material.AIR);
					}
				}
			}
		}
	}

	public void clearArrows(Player player) {
		((CraftPlayer) player).getHandle().getDataWatcher().watch(9, (byte)0);
	}
}
