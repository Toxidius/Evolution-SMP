package Evolution.HolidayRelics;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import Evolution.Main.Core;
import Evolution.Main.ParticleEffect;

public class SnowTimeEffect2 implements Runnable{

	private Location center;
	private Location temp;
	private int id;
	private int calls;
	
	public SnowTimeEffect2(Location center){
		this.center = center;
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 0, 2); // run every 2 ticks
		calls = 0;
	}
	
	@Override
	public void run() {
		if (calls >= 1800){ // 3 minutes it lasts
			Bukkit.getScheduler().cancelTask(id);
			return;
		}
		calls++;
		
		// particles
		for (int i = 0; i < 20; i++){
			temp = center.clone().add(Math.random()*20-10, 0.0, Math.random()*20-10);
			ParticleEffect.FIREWORKS_SPARK.display(new Vector(0.0, 10.0, 0.0), 0.05F, temp, 40);
			//ParticleEffect.FIREWORKS_SPARK.display(new Vector(0.0, 0.1, 0.0), 0, block.getLocation(), 40);
		}
	}
}