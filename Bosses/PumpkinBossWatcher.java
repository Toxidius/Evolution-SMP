package Evolution.Bosses;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.util.Vector;

import Evolution.Main.Core;

public class PumpkinBossWatcher implements Runnable{

	private Skeleton skeleton;
	private Random r;
	private int id;
	private int calls;
	
	public PumpkinBossWatcher(Skeleton skeleton){
		this.skeleton = skeleton;
		r = new Random();
		calls = 0;
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 2L, 2L);
	}
	
	public void end(){
		if (skeleton != null && skeleton.isDead() == false){
			skeleton.remove();
		}
		if (skeleton != null && Core.bossLoader.loadedBosses.contains(skeleton)){
			Core.bossLoader.removeBoss(skeleton); // remove the entity from the loaded bosses
		}
		Bukkit.getScheduler().cancelTask(id); // prevent runnable from continuing
	}
	
	@Override
	public void run() {
		calls++;
		//Bukkit.getServer().broadcastMessage(calls + " " + skeleton.getLocation().getChunk().isLoaded());
		if (skeleton == null){
			//Bukkit.getServer().broadcastMessage("is null");
			end();
			return;
		}
		else if (skeleton.getLocation().getChunk().isLoaded() == false){
			//Bukkit.getServer().broadcastMessage("is no longer loaded");
			end();
			return;
		}
		else if (skeleton.isDead()){
			//Bukkit.getServer().broadcastMessage("is dead");
			// death effect
			@SuppressWarnings("unused")
			BlockVolcanoEffect effect = new BlockVolcanoEffect(skeleton.getLocation(), Material.PUMPKIN, (byte)0);
			effect = new BlockVolcanoEffect(skeleton.getLocation(), Material.PUMPKIN, (byte)0);
			effect = new BlockVolcanoEffect(skeleton.getLocation(), Material.PUMPKIN, (byte)0);
			
			end();
			return;
		}
		
		if (skeleton.getTarget() != null){
			// make sure target isn't dead
			if (skeleton.getTarget().isDead()){
				skeleton.setTarget(null); // remove this target so he can set a new one
				return;
			}
			// make sure target isn't himself
			if (skeleton.getTarget().equals(skeleton)){
				skeleton.setTarget(null); // prevent a total fuck up where he is targeting himself
				return;
			}
			//
			// check if the target is running away
			double distance = skeleton.getTarget().getLocation().distance(skeleton.getLocation());
			int yDiff = Math.abs(skeleton.getLocation().getBlockY()-skeleton.getTarget().getLocation().getBlockY());
			if (distance >= 10.0 && distance <= 40.0 && yDiff <= 10){
				// Player is running away! Fuck em up!
				LivingEntity target = skeleton.getTarget();
				
				double multiplier = distance*0.25;
				Vector diff = target.getLocation().subtract(skeleton.getLocation()).toVector().normalize().multiply(multiplier);
				diff.setY(diff.getY()*2.0);
				skeleton.setVelocity(diff);
			}
			else if (distance > 40.0){
				skeleton.setTarget(null); // untarget
				return; // don't want any other moves taking place on a null target
			}
			//
			// check if being crowded every 10 calls (half second)
			if (calls%10 == 0){
				int numAttackers = 0;
				for (Entity entity : skeleton.getNearbyEntities(2.5, 2.5, 2.5)){
					if (entity instanceof LivingEntity){
						numAttackers++;
					}
				}
				if (numAttackers >= 3){
					// hulk smash effect
					customHulkSmash();
				}
			}
			//
			// special moves
			if (r.nextInt(40) == 15){
				// 2.5% chance throw pumpkin at target
				@SuppressWarnings("unused")
				ThrowBlockEffect effect = new ThrowBlockEffect(skeleton, skeleton.getTarget(), skeleton.getEyeLocation(), Material.PUMPKIN, (byte)0);
			}
		}
		else{
			if (calls%10 == 0){
				// no current target, this fag is super fucking hostile, so find one!
				for (Entity entity : skeleton.getNearbyEntities(10.0, 10.0, 10.0)){
					if (entity instanceof LivingEntity){
						LivingEntity living = (LivingEntity) entity;
						if (living instanceof Player){
							Player player = (Player) living;
							if (player.getGameMode() == GameMode.SURVIVAL){
								skeleton.setTarget(living);
								break;
							}
						}
						else if (entity instanceof Monster
								&& ((Monster)entity).getCustomName() != null
								&& ((Monster)entity).getCustomName().equals(ChatColor.DARK_PURPLE + "Structure Guardian") == true){
							// do nothing because we don't want him attacking structure guardians
						}
						else{
							skeleton.setTarget(living);
							break;
						}
						
					}
				}
			}
			
		}
		
	}
	
	@SuppressWarnings("deprecation")
	public void customHulkSmash(){
		// hulk smash effect
		Location center = skeleton.getLocation();
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
						temp.getWorld().playEffect(temp, Effect.STEP_SOUND, temp.getBlock().getTypeId(), 10); // break break effect
					}
				}
			}
		}
		
		// throw players within radius away
		for (Entity entity : skeleton.getNearbyEntities(radius, radius, radius)){
			if (entity instanceof LivingEntity){
				LivingEntity living = (LivingEntity) entity;
				Vector difference;
				double distanceMultiplier;
				distanceMultiplier = Math.abs( radius - skeleton.getLocation().distance(living.getLocation()) ) * 0.60;
				difference = skeleton.getLocation().subtract(living.getLocation()).toVector().normalize().multiply(-1.0).multiply(distanceMultiplier);
				difference.setY(0.75);
				living.setVelocity(difference);
			}
		}
	}

}
