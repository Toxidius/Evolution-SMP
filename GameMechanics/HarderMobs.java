package Evolution.GameMechanics;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import Evolution.Bosses.ThrowEnemyEffect;
import Evolution.Main.Core;

public class HarderMobs implements Listener{
	
	public HarderMobs(){
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onMobHurtByPlayer(EntityDamageByEntityEvent e){
		if (e.getCause() == DamageCause.ENTITY_ATTACK 
				&& e.getEntity() instanceof Monster
				&& e.getEntityType() != EntityType.WITHER
				&& e.getEntity().getCustomName() == null){
			Monster monster = (Monster) e.getEntity();
			LivingEntity attacker = (LivingEntity) e.getDamager();
			
			// check if first damage & random chance variable
			if (monster.getHealth() == monster.getMaxHealth() && Math.random() <= 0.05){ // 5% of happening on first hit
				// rampage speed effect
				if (monster.hasPotionEffect(PotionEffectType.SPEED)){
					monster.removePotionEffect(PotionEffectType.SPEED);
				}
				
				// no knockback
				Bukkit.getScheduler().scheduleSyncDelayedTask(Core.thisPlugin, new Runnable(){
					@Override
					public void run() {
						monster.setVelocity(new Vector(0.0, -0.5, 0.0));
					}
				}, 1L);
				Location temp = monster.getEyeLocation();
				temp.getWorld().playEffect(temp, Effect.VILLAGER_THUNDERCLOUD, 10);
				temp.getWorld().playEffect(temp.clone().add(0.1, 0.0, 0.1), Effect.VILLAGER_THUNDERCLOUD, 10);
				temp.getWorld().playEffect(temp.clone().add(-0.1, 0.0, -0.1), Effect.VILLAGER_THUNDERCLOUD, 10);
				
				// speed potion effect
				if (monster instanceof WitherSkeleton
						|| monster instanceof PigZombie
						|| (monster instanceof Zombie && ((Zombie)monster).isBaby())
						|| monster instanceof Creeper
						|| monster instanceof CaveSpider){
					monster.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 0, false, true)); // speed 1 for 10 seconds
				}
				else{
					monster.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 1, false, true)); // speed 2 for 10 seconds
				}
			}
			else if (monster.getHealth() == monster.getMaxHealth() && Math.random() <= 0.05){ // 5% of happening on first hit
				// magic spell effect
				// throws the player after a "warning" period
				@SuppressWarnings("unused")
				ThrowEnemyEffect effect = new ThrowEnemyEffect(monster, true);
			}
			else{
				// NOT first hit code
				// chance of special move
				if (Math.random() < 0.05){ // 5% chance of happening
					int rand = Core.r.nextInt(3); // 0 to 2
					if (rand == 0){
						// no knockback for enemy
						Bukkit.getScheduler().scheduleSyncDelayedTask(Core.thisPlugin, new Runnable(){
							@Override
							public void run() {
								monster.setVelocity(new Vector(0.0, -0.5, 0.0));
							}
						}, 1L);
					}
					else if (rand == 1 && !(monster instanceof Creeper)){
						// fling enemy towards player if not creeper
						Bukkit.getScheduler().scheduleSyncDelayedTask(Core.thisPlugin, new Runnable(){
							@Override
							public void run() {
								int yDiff = Math.abs(monster.getLocation().getBlockY()-attacker.getLocation().getBlockY());
								double multiplier = monster.getLocation().distance(attacker.getLocation())*0.15;
								Vector direction = monster.getLocation().subtract(attacker.getLocation()).toVector().normalize().multiply(-multiplier);
								direction.setY(0.75 + yDiff*0.08);
								
								monster.setVelocity(direction); // throw towards attacker (player)
								
								// old code:
								//Vector difference = attacker.getLocation().subtract(monster.getLocation()).toVector().normalize();
								//monster.setVelocity(difference);
							}
						}, 1L);
					}
					else{
						// enemy gets strength 2
						if (monster.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)){
							monster.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
						}
						monster.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 400, 1, false, true)); // strength 2 for 20 seconds
					}
					
				}
			}
		}
	}
}