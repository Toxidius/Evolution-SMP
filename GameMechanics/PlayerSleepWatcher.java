package Evolution.GameMechanics;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import Evolution.Main.Core;

public class PlayerSleepWatcher implements Runnable{
	
	Player player;
	int id;
	int calls = 0;
	
	public PlayerSleepWatcher(Player player){ // constructor
		this.player = player;
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 20, 20); // every second
	}
	
	@Override
	public void run() {
		if (calls >= 5){ // after 5 seconds, change it to day
			if (player.isSleeping() && Core.playerSleep.sleepCanceled == false){
				player.getWorld().setTime(0); // make it day
				Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Rise and shine sleepy head!");
				if ((player.getWorld().getWeatherDuration() > 2) && (player.getWorld().hasStorm())){
					player.getWorld().setWeatherDuration(1); // get rid of rain
				}
			}
			else if (player.isSleeping() && Core.playerSleep.sleepCanceled == true){
				player.setFireTicks(20); // eject them
				Bukkit.getServer().broadcastMessage("ejected on 5 calls");
			}
			
			Core.playerSleep.sleepCanceled = false;
			Core.playerSleep.playerIsSleeping = false;
			Bukkit.getScheduler().cancelTask(id); // end scheduled task
			return;
		}
		else if (Core.playerSleep.sleepCanceled == true){
			Core.playerSleep.sleepCanceled = false;
			Core.playerSleep.playerIsSleeping = false;
			player.setFireTicks(20); // eject them
			Bukkit.getServer().broadcastMessage("ejected before 5 calls");
			Bukkit.getScheduler().cancelTask(id); // end scheduled task
			return;
		}
		calls++;
	}
}

