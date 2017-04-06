package Evolution.GameMechanics;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Evolution.Main.Core;

public class FOVEffect implements Runnable{

	private int loops;
	private int maxLoops = 8;
	private int currentSlowness;
	private int maxSlowness = 6;
	private boolean increasing;
	private Player player;
	private int id;
	
	public FOVEffect(Player player) {
		this.player = player;
		loops = 0;
		currentSlowness = 0;
		increasing = true;
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 80L, 2L);
		player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 600, 0));
	}
	
	@Override
	public void run() {
		if (loops > maxLoops){
			// end case
			for (PotionEffect effect : player.getActivePotionEffects()){
				player.removePotionEffect(effect.getType());
			}
			Bukkit.getScheduler().cancelTask(id);
		}
		
		player.removePotionEffect(PotionEffectType.SLOW);
		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5, currentSlowness));
		
		if (increasing == true){
			currentSlowness++;
		}
		else{
			currentSlowness--;
		}
		
		if (currentSlowness >= maxSlowness){
			increasing = false;
		}
		if (currentSlowness <= 0){
			increasing = true;
			loops++;
		}
	}

}
