package Evolution.Bosses;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Snowball;

import Evolution.Main.Core;

public class SnowballBarrage implements Runnable{
	
	private LivingEntity target;
	private LivingEntity shooter;
	private int calls;
	private int id;

	public SnowballBarrage(LivingEntity target, LivingEntity shooter){
		this.target = target;
		this.shooter = shooter;
		calls = 0;
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 3L, 3L);
	}

	
	@Override
	public void run() {
		if (calls > 20 || target == null || target.isDead()){
			// end task
			Bukkit.getScheduler().cancelTask(id);
		}
		else{
			// summon snowball barrage
			Location center = target.getLocation();
			center.add(0.0, 10.0, 0.0);
			
			Location temp;
			Snowball snowball;
			double xOff, zOff;
			for (int i = 0; i < 7; i++){
				xOff = Math.random()*6 - 4; // -4 to 4
				zOff = Math.random()*6 - 4; // -4 to 4
				temp = center.clone().add(xOff, 0.0, zOff);
				snowball = (Snowball) center.getWorld().spawnEntity(temp, EntityType.SNOWBALL);
				snowball.setCustomName("Barrage");
				snowball.setShooter(shooter);
			}
		}
		
		calls++;
	}
}
