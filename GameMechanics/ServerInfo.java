package Evolution.GameMechanics;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import Evolution.Main.Core;

public class ServerInfo implements Listener, CommandExecutor, Runnable{

	public long uptime = 0;
	public String lastOnlinePlayer = "";
	
	public ServerInfo(){
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 60*20, 60*20); // every minute
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("info")){
			World world = Bukkit.getWorlds().get(0);
			int numHostile = 0;
			int numPeaceful = 0;
			int numItems = 0;
			int current;
			HashMap<String,Integer> entityOccurances = new HashMap<>();
			for (Entity entity : world.getEntities()){
				if (entity instanceof Monster){
					numHostile++;
				}
				else if (entity instanceof LivingEntity
						&& !(entity instanceof Monster)){
					numPeaceful++;
				}
				else if (entity instanceof Item){
					numItems++;
				}
				
				if (entityOccurances.containsKey(entity.getType().name()) == true){
					current = entityOccurances.get(entity.getType().name()).intValue();
					current++;
					entityOccurances.put(entity.getType().name(), new Integer(current));
				}
				else{
					entityOccurances.put(entity.getType().name(), new Integer(1));
				}
			}
			String mostCommonEntity = "";
			int mostCommonEntityAmount = 0;
			for (String key : entityOccurances.keySet()){
				if (entityOccurances.get(key).intValue() > mostCommonEntityAmount){
					mostCommonEntity = key;
					mostCommonEntityAmount = entityOccurances.get(key).intValue();
				}
			}
			
			sender.sendMessage(ChatColor.GREEN + "Uptime: " + ChatColor.WHITE + uptime + ChatColor.GREEN + " Minutes (" + round(uptime/60.0) + " Hours)");
			sender.sendMessage(ChatColor.GREEN + "Entities: " + ChatColor.WHITE + world.getEntities().size() + ChatColor.GREEN + " (Total) "
					+ ChatColor.WHITE + numHostile + ChatColor.GREEN + " (Hostile) "
					+ ChatColor.WHITE + numPeaceful + ChatColor.GREEN + " (Peaceful) "
					+ ChatColor.WHITE + numItems + ChatColor.GREEN + " (Items) ");
			sender.sendMessage(ChatColor.GREEN + "Most Common Entity: " + ChatColor.WHITE + mostCommonEntity + " (" + mostCommonEntityAmount + ")");
			sender.sendMessage(ChatColor.GREEN + "Chunks: " + ChatColor.WHITE + world.getLoadedChunks().length + ChatColor.GREEN + " (Overworld) "
					+ ChatColor.WHITE + Bukkit.getWorlds().get(1).getLoadedChunks().length + ChatColor.GREEN + " (Nether) "
					+ ChatColor.WHITE + Bukkit.getWorlds().get(2).getLoadedChunks().length + ChatColor.GREEN + " (End) ");
			sender.sendMessage(ChatColor.GREEN + "Last Online Player: " + ChatColor.WHITE + lastOnlinePlayer);
			sender.sendMessage("");
			for (World otherWorld : Bukkit.getWorlds()){
				sender.sendMessage(ChatColor.GREEN + otherWorld.getName() + " Difficulty: " + ChatColor.WHITE + otherWorld.getDifficulty().name());
			}
			return true;
		}
		return false;
	}
	
	public double round(double input){
		input = Math.round( (input*10.0) )/10.0;
		return input;
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e){
		lastOnlinePlayer = e.getPlayer().getName();
	}

	@Override
	public void run() {
		uptime++; // increment uptime
	}

}
