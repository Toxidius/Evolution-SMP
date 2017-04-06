package Evolution.GameMechanics;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener{
	
	public PlayerQuit(){
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e){
		//Bukkit.getServer().broadcastMessage(e.getQuitMessage());
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent e){
		Bukkit.getServer().broadcastMessage("Kick: " + e.getReason());
	}
}
