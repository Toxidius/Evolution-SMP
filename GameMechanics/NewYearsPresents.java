package Evolution.GameMechanics;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import Evolution.Main.Core;

public class NewYearsPresents implements Listener, Runnable{
	
	private int time;
	@SuppressWarnings("unused")
	private int id;
	private Player lastRandomPlayer;
	
	public NewYearsPresents(){
		time = 0;
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 20, 20); // executes every second
	}
	
	@Override
	public void run() {
		time++;
		int dropTime = (int) Math.round((30.0*60.0) / Bukkit.getOnlinePlayers().size()); // every 30 minutes a present spawns
		if (time >= dropTime
				&& Bukkit.getOnlinePlayers().size() > 0){
			// give a player a new years present
			lastRandomPlayer = getRandomPlayer();
			ItemStack drop;
			switch (Core.r.nextInt(2)){
			case 0:
				drop = Core.partyPopper.getRelic().clone();
				break;
			case 1:
				drop = Core.newYearsParty.getRelic().clone();
				break;
			default:
				drop = Core.partyPopper.getRelic().clone();
				break;
			}
			lastRandomPlayer.getWorld().dropItem(lastRandomPlayer.getLocation(), drop);
			Bukkit.getServer().broadcastMessage(ChatColor.GREEN + lastRandomPlayer.getName() + " found a New Years Toy!");
			
			
			time = 0;
		}
		else if (time >= dropTime
				&& Bukkit.getOnlinePlayers().size() == 0){
			time = 0;
		}
	}
	
	public Player getRandomPlayer(){
		if (Bukkit.getWorlds().get(0).getPlayers().size() == 0){
			return null; // nobody online
		}
		
		ArrayList<Player> playersToChoose = new ArrayList<>();
		for (Player player : Bukkit.getWorlds().get(0).getPlayers()){
			if (player.getPlayerListName().contains("(AFK)") == false){
				playersToChoose.add(player);
			}
		}
		
		if (playersToChoose.size() == 0){
			return null;
		}
		
		Player choosenPlayer = null;
		Player tempPlayer = null;
		for (int i = 0; i < 10; i++){
			// try 10 times to find a good player that wasn't previously choosen
			tempPlayer = playersToChoose.get( Core.r.nextInt(playersToChoose.size()) );
			if (tempPlayer.equals(lastRandomPlayer) == false){
				choosenPlayer = tempPlayer;
				break;
			}
			choosenPlayer = tempPlayer; // this player was previously choosen, but no other applicable players are onlines
		}
		
		if (choosenPlayer != null){
			return choosenPlayer;
		}
		else{
			return null;
		}
	}

}
