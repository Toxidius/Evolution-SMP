package Evolution.Bosses;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import Evolution.Main.Core;

public class CreeperBossWatcher implements Runnable{

	private Creeper creeper;
	private Random r;
	private int id;
	private int calls;
	
	public CreeperBossWatcher(Creeper creeper){
		this.creeper = creeper;
		r = new Random();
		calls = 0;
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 2L, 2L);
	}
	
	public void end(){
		if (creeper != null && creeper.isDead() == false){
			creeper.remove();
		}
		if (creeper != null && Core.bossLoader.loadedBosses.contains(creeper)){
			Core.bossLoader.removeBoss(creeper); // remove the entity from the loaded bosses
		}
		Bukkit.getScheduler().cancelTask(id); // prevent runnable from continuing
	}
	
	@Override
	public void run() {
		calls++;
		//Bukkit.getServer().broadcastMessage(calls + " " + skeleton.getLocation().getChunk().isLoaded());
		if (creeper == null){
			//Bukkit.getServer().broadcastMessage("is null");
			end();
			return;
		}
		else if (creeper.getLocation().getChunk().isLoaded() == false){
			//Bukkit.getServer().broadcastMessage("is no longer loaded");
			end();
			return;
		}
		else if (creeper.isDead()){
			//Bukkit.getServer().broadcastMessage("is dead");
			// death effect
			end();
			return;
		}
		
		if (creeper.getTarget() != null){
			// make sure target isn't dead
			if (creeper.getTarget().isDead()){
				creeper.setTarget(null); // remove this target so he can set a new one
				return;
			}
			// make sure target isn't himself
			if (creeper.getTarget().equals(creeper)){
				creeper.setTarget(null); // prevent a total fuck up where he is targeting himself
				return;
			}
			//
			// check if the target is running away
			double distance = creeper.getLocation().distance(creeper.getTarget().getLocation());
			int yDiff = Math.abs(creeper.getLocation().getBlockY()-creeper.getTarget().getLocation().getBlockY());
			if (distance >= 10.0 
					&& distance <= 40.0 
					&& yDiff <= 10){
				
				// the player is running away!
				
				if (calls%10 == 0){
					// Throw tnt at em!
					LivingEntity target = creeper.getTarget();
					
					double multiplier = creeper.getLocation().distance(target.getLocation())*0.06;
					Vector direction = creeper.getLocation().subtract(target.getLocation()).toVector().normalize().multiply(-multiplier);
					direction.setY(0.75 + yDiff*0.08);
					
					TNTPrimed tnt = (TNTPrimed) creeper.getWorld().spawnEntity(creeper.getLocation(), EntityType.PRIMED_TNT);
					tnt.setMetadata("noBlockDamage", new FixedMetadataValue(Core.thisPlugin, true)); // so it doesn't do block damage
					tnt.setVelocity(direction);
				}
				
				// give creeper speed 3
				if (creeper.hasPotionEffect(PotionEffectType.SPEED)){
					creeper.removePotionEffect(PotionEffectType.SPEED);
				}
				creeper.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 2, false, true)); // speed 3 for 5 seconds
				
			}
			else if (distance > 40.0){
				creeper.setTarget(null); // untarget
				return; // don't want any other moves taking place on a null target
			}
			//
			// check if being crowded every 10 calls (half second)
			if (calls%10 == 0){
				int numAttackers = 0;
				for (Entity entity : creeper.getNearbyEntities(3.0, 3.0, 3.0)){
					if (entity instanceof LivingEntity){
						numAttackers++;
					}
				}
				if (numAttackers >= 3){
					// kaboom!
					//creeper.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, creeper.getLocation(), 2);
					//creeper.getWorld().playSound(creeper.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1F, 1F);
					TNTPrimed tnt = (TNTPrimed) creeper.getWorld().spawnEntity(creeper.getLocation(), EntityType.PRIMED_TNT);
					tnt.setMetadata("noBlockDamage", new FixedMetadataValue(Core.thisPlugin, true)); // so it doesn't do block damage
					tnt.setFuseTicks(1); // immediately explode
				}
			}
			//
			// special moves
			if (r.nextInt(50) == 15){
				// 2% chance throw creeper
				// launch creeper at the player
				LivingEntity target = (LivingEntity) creeper.getTarget();
				
				double multiplier = creeper.getLocation().distance(target.getLocation())*0.15;
				Vector direction = creeper.getLocation().subtract(target.getLocation()).toVector().normalize().multiply(-multiplier);
				direction.setY(0.75 + yDiff*0.08);
				
				Creeper creeperMinion = (Creeper) creeper.getWorld().spawnEntity(creeper.getLocation(), EntityType.CREEPER);
				creeperMinion.setVelocity(direction); // throw towards player
			}
			else if (r.nextInt(400) == 15){
				// 0.5% (roughly once every 20 seconds)
				// extreme detonate action
				@SuppressWarnings("unused")
				CreeperDetonateEffect effect = new CreeperDetonateEffect(creeper, 6);
			}
		}
		
	}

}
