package Evolution.HolidayRelics;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import Evolution.Main.Core;

public class MistleToeEffect implements Runnable{

	private Player kisser;
	private Player kissed;
	private int id;
	private int calls;
	
	public MistleToeEffect(Player kisser, Player kissed){
		this.kisser = kisser;
		this.kissed = kissed;
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 0, 20); // run every 20 ticks
		calls = 0;
	}
	
	@Override
	public void run() {
		if (calls >= 10){
			Bukkit.getScheduler().cancelTask(id);
			return;
		}
		calls++;
		
		kisser.getWorld().spawnParticle(Particle.HEART, kisser.getEyeLocation().add(0.0, 0.5, 0.0), 1);
		kissed.getWorld().spawnParticle(Particle.HEART, kissed.getEyeLocation().add(0.0, 0.5, 0.0), 1);
	}
}
