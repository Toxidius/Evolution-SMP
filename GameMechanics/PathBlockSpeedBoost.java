package Evolution.GameMechanics;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Evolution.Main.Core;

public class PathBlockSpeedBoost implements Runnable{

	@SuppressWarnings("unused")
	private int id;
	
	public PathBlockSpeedBoost() {
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 5, 5);
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
			
			if (player.getLocation().add(0.0, -0.5, 0.0).getBlock().getType() == Material.GRASS_PATH){
				player.removePotionEffect(PotionEffectType.SPEED);
				player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, 0, true, false)); // speed 1 for 4 seconds
			}
		}
	}

}
