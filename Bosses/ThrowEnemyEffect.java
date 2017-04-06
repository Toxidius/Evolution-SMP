package Evolution.Bosses;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.util.Vector;

import Evolution.Main.Core;

public class ThrowEnemyEffect implements Runnable{

	private Monster monster;
	private int id;
	private int calls;
	private boolean throwTarget;
	
	public ThrowEnemyEffect(Monster monster, boolean throwTarget){
		this.monster = monster;
		this.calls = 0;
		this.throwTarget = throwTarget;
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 2L, 2L);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		if (monster.isDead()){
			Bukkit.getScheduler().cancelTask(id); // end task
			return;
		}
		if (calls >= 100){
			Bukkit.getScheduler().cancelTask(id); // end task
			return;
		}
		else if (calls == 20 && throwTarget && monster.getTarget() != null && monster.getLocation().distance(monster.getTarget().getLocation()) <= 7.0){
			LivingEntity target = monster.getTarget();
			//int radius = 5;
			//double distanceMultiplier;
			//distanceMultiplier = Math.abs(radius - monster.getLocation().distance(target.getLocation()) ) * 0.8;
			Vector difference = monster.getLocation().toVector().subtract(target.getLocation().toVector()).normalize().multiply(-1.5);
			difference.setY(0.25);
			target.setVelocity(difference);
		}
		else{
			Location temp = monster.getEyeLocation().add(Math.random()*2-1, 0, Math.random()*2-1);
			//Location temp2 = monster.getLocation().add(Math.random()*2-1, 0, Math.random()*2-1);
			//temp.getWorld().playEffect(temp, Effect.POTION_SWIRL, 0, 0, 100, 100, 100, 1, 0, 16);
			temp.getWorld().playEffect(temp, Effect.POTION_SWIRL, 10);
			//temp.getWorld().playEffect(temp2, Effect.HAPPY_VILLAGER, 10);
			//temp.getWorld().spigot().playEffect(temp, Effect.POTION_SWIRL, 0, 0, 0, 0, 0, 0, 10, 1);
		}
		
		calls++;
	}
}
