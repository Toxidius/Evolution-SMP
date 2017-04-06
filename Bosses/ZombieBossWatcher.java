package Evolution.Bosses;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Zombie;

import Evolution.Main.Core;

public class ZombieBossWatcher implements Runnable{

	private Zombie zombie;
	private Random r;
	private int id;
	@SuppressWarnings("unused")
	private boolean effectInProgress = false;
	
	public ZombieBossWatcher(Zombie zombie){
		this.zombie = zombie;
		r = new Random();
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 2L, 2L);
	}
	
	@Override
	public void run() {
		if (zombie == null || zombie.isDead()){
			Bukkit.getScheduler().cancelTask(id);
			return;
		}
		
		if (zombie.getTarget() != null){
			if (r.nextInt(100) == 15
					&& zombie.isOnGround() == true){
				// 1% chance hulk smash move
				@SuppressWarnings("unused")
				HulkSmashEffect effect = new HulkSmashEffect(zombie, true);
			}
			else if (r.nextInt(100) == 45){
				// 1% chance fire burn effect
				GroundBurnEffect effect = new GroundBurnEffect(zombie);
				Bukkit.getPluginManager().registerEvents(effect, Core.thisPlugin); // register as a listener for item burning events
			}
			else if (r.nextInt(100) == 35){
				// 1% chance crush effect
				@SuppressWarnings("unused")
				HulkSmashEffect effect = new HulkSmashEffect(zombie, false);
			}
		}
		
	}

}
