package Evolution.GameMechanics;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import Evolution.Main.Core;

public class PlayerAFK implements Listener, Runnable{

	private HashMap<String, Location> previousLocations;
	private HashMap<String, Integer> idleChecks;
	
	public PlayerAFK(){
		previousLocations = new HashMap<>();
		idleChecks = new HashMap<>();
		
		// set display name incase something messed up from a reload
		for (Player player : Bukkit.getOnlinePlayers()){
			player.setPlayerListName(player.getName());
		}
		
		// start runnable
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 200, 200); // every 10 seconds
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		e.getPlayer().setPlayerListName(e.getPlayer().getName());
		clearIdleChecks(e.getPlayer());
	}
	
	@Override
	public void run() {
		// loop through all players checking if their previous locations equals their current location
		for (Player player : Bukkit.getOnlinePlayers()){
			if (player.isOnline() == false){
				continue;
			}
			
			if (previousLocations.containsKey(player.getName()) == true){
				if (previousLocations.get(player.getName()).equals(player.getLocation())){
					// player is currently afk, increment idle checks
					incrementIdleChecks(player);
				}
				else if (idleChecks.containsKey(player.getName()) 
						&& idleChecks.get(player.getName()).intValue() >= 6){
					// player is no longer afk, update their display name back to normal
					player.setPlayerListName(player.getName());
					// clear idle checks
					clearIdleChecks(player);
				}
				
				// update previous location
				previousLocations.replace(player.getName(), player.getLocation());
			}
			else{
				previousLocations.put(player.getName(), player.getLocation());
			}
		}
	}
	
	public void incrementIdleChecks(Player player){
		if (idleChecks.containsKey(player.getName())){
			int current = idleChecks.get(player.getName()).intValue();
			idleChecks.replace(player.getName(), new Integer(current+1));
			
			if ((current+1) == 6){
				// player is now considered afk (60 seconds afk)
				// update display name to show afk
				player.setPlayerListName(ChatColor.GRAY + "*" + player.getName() + "*");
			}
		}
		else{
			idleChecks.put(player.getName(), new Integer(1));
		}
	}
	
	public void clearIdleChecks(Player player){
		if (idleChecks.containsKey(player.getName())){
			idleChecks.replace(player.getName(), new Integer(0));
		}
		else{
			idleChecks.put(player.getName(), new Integer(0));
		}
	}

}
