package Evolution.GameMechanics;

import org.bukkit.Bukkit;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import Evolution.Main.Core;

public class CustomProjectileDamage implements Listener{

	public CustomProjectileDamage(){
	}
	
	@EventHandler
	public void onPlayerDamageByCustomProjectile(EntityDamageByEntityEvent e){
		if (e.getEntity() instanceof Player
				&& e.getDamager() instanceof Projectile
				&& e.getDamager().getCustomName() != null
				&& e.getDamager().getCustomName().contains("Barrage")){
			Player player = (Player) e.getEntity();
			Projectile projectile = (Projectile) e.getDamager();
			Entity shooter = (Entity) projectile.getShooter();
			Bukkit.getScheduler().scheduleSyncDelayedTask(Core.thisPlugin, new Runnable(){
				@Override
				public void run() {
					if (player == null || player.isDead() == true){
						return;
					}
					
					if (shooter != null){
						player.damage(2.0, shooter); // 1 heart damage
					}
					else{
						player.damage(2.0); // 1 heart damage
					}
				}
			}, 1L);
		}
	}
	
	@EventHandler
	public void onEntityDamageByCustomProjectile(EntityDamageByEntityEvent e){
		if (e.getEntity() instanceof LivingEntity
				&& e.getEntity() instanceof Player == false
				&& e.getDamager() instanceof Projectile
				&& e.getDamager().getCustomName() != null
				&& e.getDamager().getCustomName().contains("Barrage")){
			LivingEntity entity = (LivingEntity) e.getEntity();
			Projectile projectile = (Projectile) e.getDamager();
			LivingEntity shooter = (LivingEntity) projectile.getShooter();
			
			if (shooter.equals(entity)){
				e.setCancelled(true); // prevent damaging itself
				return;
			}
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(Core.thisPlugin, new Runnable(){
				@Override
				public void run() {
					if (entity == null || entity.isDead()){
						return;
					}
					entity.damage(2.0, shooter); // 1 heart damage
				}
			}, 1L);
			
		}
		
	}
	
	@EventHandler
	public void onDmgByProjectileEggOrSnowball(EntityDamageByEntityEvent e){
		if (e.getEntity() instanceof Player
				&& e.getDamager() instanceof Projectile
				&& (e.getDamager() instanceof Snowball || e.getDamager() instanceof Egg)){
			e.setCancelled(true); // prevent player armor damaging from snowballs or eggs
			Player player = (Player) e.getEntity();
			player.setVelocity(e.getDamager().getVelocity().multiply(0.45));
			//player.damage(0.001, e.getDamager());
		}
	}
}