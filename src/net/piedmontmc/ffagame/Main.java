package net.piedmontmc.ffagame;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

// TODO add jumping mechanics and bomb mechanics...
// TODO add start and endgame mechs, as well as spectating...

public class Main extends JavaPlugin{
	private static Main main;
	public ArrayList<Arena> arenas = new ArrayList<Arena>();
	public Location lobby;
	public boolean inGame = false;
	public boolean noMove = false;
	public String curArena;
	private Countdown cd;
	public Main(){
		main = this;
	}
	@Override
	public void onEnable(){
		getLogger().info("Total playable arenas: " + arenas.size());
		this.cd = new Countdown();
	}
	@Override
	public void onDisable(){
		getLogger().info("Trying to save arenas...maybe?");
	}
	public void displayHelp(CommandSender sender){
		sender.sendMessage(ChatColor.RED + "FFA Help:");
		sender.sendMessage(ChatColor.GRAY + "To view the help, type: /ffa help");
		sender.sendMessage(ChatColor.GRAY + "To start a game, type: /ffa start <arena>");
		sender.sendMessage(ChatColor.GRAY + "To add a spawn or mine, type: /ffa add <spawn|mine> <arena>");
		sender.sendMessage(ChatColor.GRAY + "To set the lobby, type: /ffa set lobby");
		sender.sendMessage(ChatColor.GRAY + "To reset mines and spawns for an arena, type: /ffa reset <arena>");
		sender.sendMessage(ChatColor.GRAY + "To create an arena, type: /ffa create <name>");
		sender.sendMessage(ChatColor.GRAY + "To remove an arena, type: /ffa remove <name>");
		sender.sendMessage(ChatColor.GRAY + "To list all arenas, type: /ffa list");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("ffa") && sender instanceof Player) { // If the player typed /ffa then do the following...
			if(args.length==0){
				displayHelp(sender);
				return true;
			}
			if(args.length==1 && args[0].equalsIgnoreCase("help")){ // This is /ffa help
				displayHelp(sender);
				return true;
			}
			if(args.length==2 && args[0].equalsIgnoreCase("create")){ // This is /ffa create <arena>
				for(Arena a:arenas){
					if(a.name.equalsIgnoreCase(args[1])){
						sender.sendMessage(ChatColor.RED + "An arena called " + args[1] + " already exists!");
						return true;
					}
				}
				arenas.add(new Arena(args[1]));
				return true;
			}
			if(args.length==2 && args[0].equalsIgnoreCase("remove")){ // This is /ffa remove <arena>
				for(Arena a:arenas){
					if(a.name.equalsIgnoreCase(args[1])){
						arenas.remove(a);
						sender.sendMessage(ChatColor.GREEN + "The arena called " + args[1] + " was removed!");
						return true;
					}
				}
				sender.sendMessage(ChatColor.RED + "No arena called " + args[1] + " was found!");
				return true;
			}
			if(args.length==2 && args[0].equalsIgnoreCase("reset")){ // This is /ffa reset <arena>
				for(Arena a:arenas){
					if(a.name.equalsIgnoreCase(args[1])){
						a.spawns.clear();
						a.mines.clear();
						sender.sendMessage(ChatColor.GREEN + "The arena called " + args[1] + " was reset!");
						return true;
					}
				}
				sender.sendMessage(ChatColor.RED + "No arena called " + args[1] + " was found!");
				return true;
			}
			if(args.length==2 && args[0].equalsIgnoreCase("set") && args[1].equalsIgnoreCase("lobby")){ // This is /ffa set lobby
				Player p = (Player) sender;
				lobby = p.getLocation();
				p.sendMessage(ChatColor.GREEN + "Lobby set!");
				return true;
			}
			if(args.length==1 && args[0].equalsIgnoreCase("list")){ // This is /ffa list
				sender.sendMessage(ChatColor.GREEN + "All arenas:");
				for(Arena a:arenas){
					sender.sendMessage(ChatColor.GRAY + a.name);
				}
				return true;
			}
			if(args.length==3 && args[0].equalsIgnoreCase("add")){ // This is /ffa add <spawn|mine> <arena>
				Player p = (Player) sender;
				for(Arena a:arenas){
					if(a.name.equalsIgnoreCase(args[2])){
						if(args[1].equalsIgnoreCase("spawn")){
							a.spawns.add(p.getLocation());
							p.sendMessage(ChatColor.GREEN +
									"Spawn added! There are now " + a.spawns.size() + " spawns in " + a.name + ".");
							return true;
						}
						if(args[1].equalsIgnoreCase("mine")){
							a.mines.add(p.getLocation());
							p.sendMessage(ChatColor.GREEN +
									"Mine added! There are now " + a.mines.size() + " mines in " + a.name + ".");
							return true;
						} else {
							p.sendMessage(ChatColor.RED + "The 2nd argument must be spawn or mine!");
							return true;
						}
					}
				}
				p.sendMessage(ChatColor.RED + "The specified arena was not found!");
				return true;
			}
			if(args[0].equalsIgnoreCase("start") && args.length==2){ // This is /ffa start <arena>
				if(inGame){
					sender.sendMessage(ChatColor.RED + "The game has already been started!");
					return true;
				}
				for(Arena a:arenas){
					if(a.name.equalsIgnoreCase(args[1])){
						curArena = a.name;
						startGame(a, (Player)sender);
						return true;
					}
				}
			}
			return false;
		}
		sender.sendMessage("You must be a player to use FFA commands.");
		return true; 
	}
	public void startGame(Arena a, Player p){
		if(Bukkit.getServer().getOnlinePlayers().length<2){
			p.sendMessage(ChatColor.RED + "There needs to be 2 players to start the game!");
			return;
		}
		if(Bukkit.getServer().getOnlinePlayers().length>a.spawns.size()){
			p.sendMessage(ChatColor.RED + "There are not enough spawns in this arena for all online players!");
			return;
		}
		inGame=true;
		noMove=true;
		for(Location loc:a.mines){
			loc.getWorld().getBlockAt(loc).setType(Material.TNT);
		}
		for(Player pl:Bukkit.getServer().getOnlinePlayers()){
			a.plys.add(pl.getName());
		}
		int i = 0;
		for(Player pl:Bukkit.getServer().getOnlinePlayers()){
			pl.setHealth(20.0);
			pl.teleport(a.spawns.get(i));
			i++;
		}
		countdown().start(15, "Game starting");
		noMove=false;
		Bukkit.broadcastMessage(ChatColor.GREEN + "The game has started!");
	}
	public static Main getInstance(){
		return main;
	}
	public Countdown countdown() {
		return this.cd;
	}
}
