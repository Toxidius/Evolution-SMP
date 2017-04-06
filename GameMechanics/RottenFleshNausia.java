package Evolution.GameMechanics;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Evolution.Main.Core;

public class RottenFleshNausia implements Runnable{

	@SuppressWarnings("unused")
	private int id;
	
	public RottenFleshNausia() {
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 80, 80);
	}
	
	@Override
	public void run() {
		if (Bukkit.getOnlinePlayers().size() == 0){
			return;
		}
		
		for (Player player : Bukkit.getOnlinePlayers()){
			if (player.isOnline() == false){
				continue;
			}
			
			if (player.getInventory().getItemInMainHand().getType() == Material.ROTTEN_FLESH){
				player.removePotionEffect(PotionEffectType.CONFUSION);
				player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 80, 0, false, false)); // nausia 1 for 4 seconds
			}
		}
	}

}
