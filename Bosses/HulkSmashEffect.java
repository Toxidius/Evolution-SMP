package Evolution.Bosses;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import Evolution.Main.Core;

public class HulkSmashEffect implements Runnable{

	private Monster monster;
	private int id;
	
	public HulkSmashEffect(Monster monster, boolean jump){
		this.monster = monster;
		if (jump == true){
			monster.setVelocity(new Vector(0.0, 1.0, 0.0));
		}
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 3L, 2L); // check every 2 ticks
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		if (monster.isDead()){
			Bukkit.getScheduler().cancelTask(id); // end task
			return;
		}
		if (monster.isOnGround()){
			// hulk smash effect
			Location center = monster.getLocation();
			Location temp;
			int radius = 5;
			
			int startX = center.getBlockX()-radius;
			int startY = center.getBlockY()-1;
			int startZ = center.getBlockZ()-radius;
			int endX = center.getBlockX()+radius;
			int endY = center.getBlockY()-1;
			int endZ = center.getBlockZ()+radius;
			for (int y = startY; y <= endY; y++){
				for (int x = startX; x <= endX; x++){
					for (int z = startZ; z <= endZ; z++){
						temp = new Location(center.getWorld(), x, y, z);
						if (temp.distance(center) <= radius){
							temp.getWorld().playEffect(temp, Effect.STEP_SOUND, temp.getBlock().getTypeId(), 10); // block break effect
						}
					}
				}
			}
			
			// throw players within radius away
			for (Entity entity : monster.getNearbyEntities(radius, radius, radius)){
				if (entity instanceof Player){
					Player player = (Player) entity;
					Vector difference;
					double distanceMultiplier;
					distanceMultiplier = Math.abs( radius - monster.getLocation().distance(player.getLocation()) ) * 0.60;
					difference = monster.getLocation().subtract(player.getLocation()).toVector().normalize().multiply(-1.0).multiply(distanceMultiplier);
					difference.setY(0.5);
					player.setVelocity(difference);
					player.damage(3.0, monster);
				}
			}
			
			// end task
			Bukkit.getScheduler().cancelTask(id);
			return;
		}
	}

}
