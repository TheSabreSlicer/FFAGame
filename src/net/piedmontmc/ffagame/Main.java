package net.piedmontmc.ffagame;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
// TODO add methods for adding mines and spawns...
// TODO add jumping mechanics and bomb mechanics...
// TODO add start and endgame mechs, as well as spectating...
public class Main extends JavaPlugin{
	private static Main main;
	public ArrayList<Arena> arenas = new ArrayList<Arena>();
	public Location lobby;
	@Override
	public void onEnable(){
		getLogger().info("Total playable arenas: " + arenas.size());
	}
	@Override
	public void onDisable(){
		getLogger().info("Trying to save arenas...maybe?");
	}
	public void displayHelp(CommandSender sender){
		sender.sendMessage(ChatColor.RED + "FFA Help:");
		sender.sendMessage(ChatColor.GRAY + "To view the help, type: /ffa help");
		sender.sendMessage(ChatColor.GRAY + "To start a game, type: /ffa start <arena>");
		sender.sendMessage(ChatColor.GRAY + "To add a spawn, type: /ffa add spawn <arena>");
		sender.sendMessage(ChatColor.GRAY + "To set the lobby, type: /ffa set lobby");
		sender.sendMessage(ChatColor.GRAY + "To add a mine, type: /ffa add mine <arena>");
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
			if(args.length==1 && args[0].equalsIgnoreCase("help")){ //This is /ffa help
				displayHelp(sender);
				return true;
			}
			if(args.length==2 && args[0].equalsIgnoreCase("create")){ //This is /ffa create <arena>
				for(Arena a:arenas){
					if(a.name.equalsIgnoreCase(args[1])){
						sender.sendMessage(ChatColor.RED + "An arena called " + args[1] + " already exists!");
						return true;
					}
				}
				arenas.add(new Arena(args[1]));
				return true;
			}
			if(args.length==2 && args[0].equalsIgnoreCase("remove")){ //This is /ffa remove <arena>
				for(Arena a:arenas){
					if(a.name.equalsIgnoreCase(args[1])){
						arenas.remove(a);
						sender.sendMessage(ChatColor.RED + "The arena called " + args[1] + " was removed!");
						return true;
					}
				}
				sender.sendMessage(ChatColor.RED + "No arena called " + args[1] + " was found!");
				return true;
			}
			if(args.length==2 && args[0].equalsIgnoreCase("reset")){ //This is /ffa reset <arena>
				for(Arena a:arenas){
					if(a.name.equalsIgnoreCase(args[1])){
						a.spawns.clear();
						a.mines.clear();
						sender.sendMessage(ChatColor.RED + "The arena called " + args[1] + " was reset!");
						return true;
					}
				}
				sender.sendMessage(ChatColor.RED + "No arena called " + args[1] + " was found!");
				return true;
			}
			if(args.length==2 && args[0].equalsIgnoreCase("set") && args[1].equalsIgnoreCase("lobby")){ //This is /ffa set lobby
				Player p = (Player) sender;
				lobby = p.getLocation();
				p.sendMessage(ChatColor.RED + "Lobby set!");
				return true;
			}
			if(args.length==1 && args[0].equalsIgnoreCase("list")){ //This is /ffa list
				sender.sendMessage(ChatColor.RED + "All arenas:");
				for(Arena a:arenas){
					sender.sendMessage(ChatColor.GRAY + a.name);
				}
				return true;
			}
			if(args.length==3 && args[0].equalsIgnoreCase("add")){
				
			}
			return false;
		}
		sender.sendMessage("You must be a player to use FFA commands.");
		return true; 
	}
	public static Main getInstance(){
		return main;
	}
}
