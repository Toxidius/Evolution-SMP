package Evolution.GameMechanics;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import Evolution.Main.Core;

public class PlayerJoin implements Listener{

	public PlayerJoin(){
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		// send player documentation link
		Player player = e.getPlayer();
		Bukkit.getScheduler().scheduleSyncDelayedTask(Core.thisPlugin, new Runnable(){
			@Override
			public void run() {
				player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "For help and documentation: " + ChatColor.RESET + "" + ChatColor.GREEN + "" + ChatColor.UNDERLINE + "https://www.dropbox.com/s/1xhoi3xinnjfwmp/EvolutionDoc.txt?dl=0");
			}
		}, 100);
	}
}
