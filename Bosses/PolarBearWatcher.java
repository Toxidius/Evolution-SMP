package Evolution.Bosses;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.PolarBear;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Evolution.Main.Core;

public class PolarBearWatcher implements Runnable{

	private PolarBear bear;
	private Random r;
	private int id;
	
	public PolarBearWatcher(PolarBear bear){
		this.bear = bear;
		r = new Random();
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 2L, 2L);
	}
	
	@Override
	public void run() {
		if (bear == null || bear.isDead()){
			Bukkit.getScheduler().cancelTask(id);
			return;
		}
		
		if (bear.getTarget() != null){
			if (r.nextInt(100) == 22){
				// 1% snowball effect
				@SuppressWarnings("unused")
				SnowballFight effect = new SnowballFight(bear.getTarget(), bear);
			}
			else if (r.nextInt(25) == 15){
				// 4% chance charge effect
				if (bear.hasPotionEffect(PotionEffectType.SPEED)){
					bear.removePotionEffect(PotionEffectType.SPEED);
				}
				bear.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 1)); // speed 2 for 10 seconds
			}
			else if (r.nextInt(50) == 45){
				// 2% chance slow target effect
				bear.getTarget().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1)); // slowness 2 for 5 seconds
			}
			else if (r.nextInt(50) == 22){
				// 2% snowball barrage effect
				@SuppressWarnings("unused")
				SnowballBarrage barrage = new SnowballBarrage(bear.getTarget(), bear);
			}
		}
		
	}

}
