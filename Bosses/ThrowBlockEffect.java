package Evolution.Bosses;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import Evolution.Main.Core;

public class ThrowBlockEffect implements Runnable{

	private Material blockType;
	private byte blockData;
	private LivingEntity thrower;
	private FallingBlock fallingBlock;
	private int id;
	
	@SuppressWarnings("deprecation")
	public ThrowBlockEffect(LivingEntity thrower, LivingEntity target, Location spawnLocation, Material blockType, byte blockData){
		this.thrower = thrower;
		this.blockType = blockType;
		this.blockData = blockData;
		
		// create the falling block
		fallingBlock = spawnLocation.getWorld().spawnFallingBlock(spawnLocation, this.blockType, this.blockData);
		fallingBlock.setDropItem(false);
		
		// set the velocity so it flys towards the player
		double multiplier = target.getEyeLocation().distance(spawnLocation)*0.20;
		Vector diff = target.getEyeLocation().subtract(spawnLocation).toVector().normalize().multiply(multiplier);
		//diff.setY(0.5);
		
		fallingBlock.setVelocity(diff);
		
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 2L, 2L);
	}
	
	@Override
	public void run() {
		if (fallingBlock == null || fallingBlock.isOnGround()){
			// end the runnable task
			Bukkit.getScheduler().cancelTask(id);
			return;
		}
		else{
			// check if the falling block has "collided" with another living entity
			for (Entity entity : fallingBlock.getNearbyEntities(1.25, 1.25, 1.25)){
				if (entity instanceof LivingEntity){
					LivingEntity living = (LivingEntity) entity;
					if (living.equals(thrower) == false){
						// collided with this entity
						living.damage(2.0, thrower);
						// end the runnable task
						Bukkit.getScheduler().cancelTask(id);
						return;
					}
				}
			}
		}
		
	}

}
