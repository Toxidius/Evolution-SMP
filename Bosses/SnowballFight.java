package Evolution.Bosses;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;

import Evolution.Main.Core;

public class SnowballFight implements Runnable{
	
	private LivingEntity target;
	private LivingEntity shooter;
	private int calls;
	private int id;

	public SnowballFight(LivingEntity target, LivingEntity shooter){
		this.target = target;
		this.shooter = shooter;
		calls = 0;
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 2L, 8L);
	}

	
	@Override
	public void run() {
		if (calls > 10 || shooter == null || shooter.isDead() || target == null || target.isDead()){
			Bukkit.getScheduler().cancelTask(id); // end task
		}
		else{
			// summon snowball barrage
			// set the velocity so it flys towards the player
			double multiplier = target.getEyeLocation().distance(shooter.getEyeLocation())*0.25;
			Vector diff = target.getEyeLocation().subtract(shooter.getEyeLocation()).toVector().normalize().multiply(multiplier);
			
			Snowball snowball = shooter.launchProjectile(Snowball.class, diff);
			snowball.setCustomName("Barrage");
			snowball.setShooter(shooter);
		}
		
		calls++;
	}
}
