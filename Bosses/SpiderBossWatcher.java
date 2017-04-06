package Evolution.Bosses;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Spider;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import Evolution.Main.Core;

public class SpiderBossWatcher implements Runnable{
	
	private Spider spider;
	private Random r;
	private int id;
	private int calls;
	private int lastThrow;
	
	public SpiderBossWatcher(Spider spider){
		this.spider = spider;
		r = new Random();
		calls = 0;
		lastThrow = 0;
		
		// start the watcher runnable
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 2L, 2L);
	}
	
	public void removeNearbyCobwebs(LivingEntity entity){
		Location center = entity.getLocation();
		Location temp;
		int radius = 4;
		int startX = center.getBlockX()-radius;
		int startY = center.getBlockY()-radius;
		int startZ = center.getBlockZ()-radius;
		int endX = center.getBlockX()+radius;
		int endY = center.getBlockY()+radius;
		int endZ = center.getBlockZ()+radius;
		for (int y = startY; y <= endY; y++){
			for (int x = startX; x <= endX; x++){
				for (int z = startZ; z <= endZ; z++){
					temp = new Location(center.getWorld(), x, y, z);
					if (temp.getBlock().getType() == Material.WEB){
						temp.getBlock().setType(Material.AIR);
					}
				}
			}
		}
	}

	@Override
	public void run() {
		if (spider == null){
			Bukkit.getScheduler().cancelTask(id);
			return;
		}
		else if (spider.isDead()){
			Bukkit.getScheduler().cancelTask(id);
			return;
		}
		
		calls++;
		if (spider.getTarget() != null){
			LivingEntity target = (LivingEntity) spider.getTarget();
			double distance = target.getLocation().distance(spider.getLocation());
			if (distance <= 1.75 && target.getFallDistance() < 4.0 && (calls-lastThrow) >= 10){
				// throw move
				removeNearbyCobwebs(target);
				target.setVelocity(new Vector(0.0, 1.0, 0.0));
				lastThrow = calls;
			}
			else if (calls%5 == 0 && distance >= 3 && Math.random() < 0.25){
				// one of 2 special moves
				if (Math.random() < 0.80){
					// throw web move
					@SuppressWarnings("unused")
					ThrowBlockEffect effect = new ThrowBlockEffect(spider, target, spider.getEyeLocation(), Material.WEB, (byte)0);
				}
				else{
					// speed up move
					if (spider.hasPotionEffect(PotionEffectType.SPEED)){
						spider.removePotionEffect(PotionEffectType.SPEED);
					}
					spider.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, 1)); // speed 2, 4 seconds
				}
				
			}
			else if (r.nextInt(50) == 20){
				// special web trap move
				if (target.isOnGround() == true && 
						(target.getLocation().getBlock().getType() == Material.AIR || target.getLocation().getBlock().getType() == Material.WEB || target.getLocation().getBlock().getType().isSolid() == false)){
					target.getLocation().getBlock().setType(Material.WEB);
					target.getEyeLocation().getBlock().setType(Material.WEB);
					
					// remove and then add potion effects
					for (PotionEffect effect : spider.getActivePotionEffects()){
						spider.removePotionEffect(effect.getType());
					}
					spider.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, 1)); // speed 2, 4 seconds
					spider.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 80, 2)); // regen 3, 4 seconds
					spider.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 200, 0)); // strength 1, 10 seconds
				}
			}
		}
	}

}
