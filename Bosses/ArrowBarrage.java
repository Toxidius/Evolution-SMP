package Evolution.Bosses;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import Evolution.Main.Core;

public class ArrowBarrage implements Runnable{
	
	private LivingEntity target;
	private int calls;
	private int id;

	public ArrowBarrage(LivingEntity target){
		this.target = target;
		calls = 0;
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 3L, 3L);
	}

	
	@Override
	public void run() {
		if (calls > 20){
			// end task
			Bukkit.getScheduler().cancelTask(id);
		}
		else{
			// summon snowball barrage
			Location center = target.getLocation();
			center.add(0.0, 10.0, 0.0);
			
			Location temp;
			Arrow arrow;
			double xOff, zOff;
			for (int i = 0; i < 10; i++){
				xOff = Math.random()*6 - 3; // -3 to 3
				zOff = Math.random()*6 - 3; // -3 to 3
				temp = center.clone().add(xOff, 0.0, zOff);
				arrow = (Arrow) center.getWorld().spawnEntity(temp, EntityType.ARROW);
				//arrow.setFireTicks(300);
				arrow.setCustomName("Barrage");
			}
		}
		
		calls++;
	}
}
