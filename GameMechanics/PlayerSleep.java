package Evolution.GameMechanics;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

import Evolution.Main.Core;

public class PlayerSleep implements Listener, CommandExecutor{
	
	public boolean sleepCanceled = false;
	public boolean playerIsSleeping = false;
	
	public PlayerSleep(){
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("c")){
			if (playerIsSleeping){
				sleepCanceled = true;
				Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + sender.getName() + " is a hermit");
				return true;
			}
			return true;
		}
		return false;
	}
	
	@EventHandler
	public void onPlayerSleep(PlayerBedEnterEvent e){
		sleepCanceled = false;
		playerIsSleeping = true;
		int numOnline = Bukkit.getOnlinePlayers().size();
		
		if (Core.bloodMoon.bloodMoonActive == true){
			boolean diamondFound = false;
			for (Entity entity : e.getPlayer().getNearbyEntities(2.0, 2.0, 2.0)){
				if (entity instanceof Item){
					if (((Item)entity).getItemStack().getType() == Material.DIAMOND){
						diamondFound = true;
						entity.remove();
						break;
					}
				}
			}
			
			if (diamondFound == false){
				// cancel players sleep
				e.getPlayer().sendMessage("You can't sleep with all that damn noise outside!");
				e.getPlayer().sendMessage("... But I've heard a diamond is a blood moons best friend. Maybe it'll help you get to sleep if you sleep on it.");
				e.setCancelled(true);
			}
			else{
				// diamond was used to sleep
				playerSleep(e.getPlayer());
			}
			
		}
		else if (numOnline > 1 && ( e.getPlayer().getWorld().getTime() >= 12000 || e.getPlayer().getWorld().hasStorm() )){
			playerSleep(e.getPlayer());
		}
	}
	
	public void playerSleep(Player player){
		Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + player.getName() + " is sleeping...");
		Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "It will become day in 5 seconds. Do /c to cancel");
		
		playerIsSleeping = true;
		sleepCanceled = false;
		@SuppressWarnings("unused")
		PlayerSleepWatcher watcher = new PlayerSleepWatcher(player);
	}

}
