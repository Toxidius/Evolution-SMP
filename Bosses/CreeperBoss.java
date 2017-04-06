package Evolution.Bosses;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import Evolution.Main.Core;

public class CreeperBoss implements CommandExecutor, Listener{
	
	@SuppressWarnings("deprecation")
	public void createBoss(Location spawnLocation){
		Creeper creeper = (Creeper) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.CREEPER);
		creeper.setRemoveWhenFarAway(false);
		creeper.setMaxHealth(100.0);
		creeper.setHealth(100.0);
		creeper.setPowered(true);
		creeper.setCustomName(ChatColor.RED + "Creeper Boss");
		creeper.setCustomNameVisible(true);
		
		// create a new creeper boss watcher
		@SuppressWarnings("unused")
		CreeperBossWatcher watcher = new CreeperBossWatcher(creeper);
		
		Core.bossLoader.addBoss(creeper);
	}
	
	@EventHandler
	public void onCreeperBossDamage(EntityDamageEvent e){
		if (e.getEntityType() == EntityType.CREEPER
				&& e.getEntity().getCustomName() != null
				&& e.getEntity().getCustomName().contains("Creeper Boss")){
			Creeper creeper = (Creeper) e.getEntity();
			
			if (e.getCause() == DamageCause.FALL
					|| e.getCause() == DamageCause.SUFFOCATION
					|| e.getCause() == DamageCause.BLOCK_EXPLOSION
					|| e.getCause() == DamageCause.ENTITY_EXPLOSION){
				e.setCancelled(true); // no damage from the above
			}
			
			// update health
			if (e.isCancelled() == false){
				creeper.setCustomName(ChatColor.RED + "Creeper Boss " + ChatColor.WHITE + round(creeper.getHealth()-e.getFinalDamage()) + ChatColor.RED + Core.heart);
			}
			
			if (e.getCause() == DamageCause.PROJECTILE){
				// no knockback from ranged attacks
				Bukkit.getScheduler().scheduleSyncDelayedTask(Core.thisPlugin, new Runnable(){
					@Override
					public void run() {
						creeper.setVelocity(new Vector(0.0, -0.5, 0.0));
					}
				}, 1L);
				
				// launch creeper back at the player
				EntityDamageByEntityEvent e2 = (EntityDamageByEntityEvent) e;
				Projectile projectile = (Projectile) e2.getDamager();
				if (!(projectile.getShooter() instanceof LivingEntity)){
					return; // must be living entity
				}
				LivingEntity target = (LivingEntity) projectile.getShooter();
				
				int yDiff = Math.abs( creeper.getLocation().getBlockY()-target.getLocation().getBlockY() );
				double multiplier = creeper.getLocation().distance(target.getLocation())*0.10;
				Vector direction = creeper.getLocation().subtract(target.getLocation()).toVector().normalize().multiply(-multiplier);
				direction.setY(0.75 + yDiff*0.08);
				
				Creeper creeperMinion = (Creeper) creeper.getWorld().spawnEntity(creeper.getLocation(), EntityType.CREEPER);
				creeperMinion.setVelocity(direction); // throw towards player
			}
		}
	}
	
	@EventHandler
	public void onCreeperBossDamageMelee(EntityDamageByEntityEvent e){
		if (e.getEntityType() == EntityType.CREEPER
				&& e.getEntity().getCustomName() != null
				&& e.getEntity().getCustomName().contains("Creeper Boss")
				&& e.getCause() == DamageCause.ENTITY_ATTACK 
				&& e.getDamager() instanceof LivingEntity){
			LivingEntity living = (LivingEntity) e.getDamager();
			Creeper creeper = (Creeper) e.getEntity();
			
			// throw attacker away from the creeper
			Vector awayVector = creeper.getLocation().subtract(living.getLocation()).toVector().normalize().multiply(-1.5);
			awayVector.setY(0.5);
			living.setVelocity(awayVector);
			
		}
	}
	
	@EventHandler
	public void onCreeperBossPrime(ExplosionPrimeEvent e){
		if (e.getEntityType() == EntityType.CREEPER
				&& e.getEntity().getCustomName() != null
				&& e.getEntity().getCustomName().contains("Creeper Boss")){
			e.setCancelled(true); // don't want the creeper boss actually exploding
			
			// simulated explosion
			Creeper creeper = (Creeper) e.getEntity();
			if (creeper.getTarget() != null
					&& creeper.getTarget().isDead() == false){
				TNTPrimed tnt = (TNTPrimed) creeper.getWorld().spawnEntity(creeper.getLocation(), EntityType.PRIMED_TNT);
				tnt.setMetadata("noBlockDamage", new FixedMetadataValue(Core.thisPlugin, true)); // so it doesn't do block damage
				tnt.setFuseTicks(1); // immediately explode
			}
		}
	}
	
	public double round(double input){
		input = Math.round( (input*10.0) )/10.0;
		return input;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if (cmd.getName().equalsIgnoreCase("creeperboss")){
			if (!(sender instanceof Player)){
				sender.sendMessage("Must be a player to use this command.");
				return true;
			}
			if (!(sender.isOp())){
				sender.sendMessage("Must be OP to use this command.");
				return true;
			}
			Player player = (Player) sender;
			createBoss(player.getLocation().add(0.0, 5.0, 0.0));
			return true;
		}
	
		return false;
	}

}
