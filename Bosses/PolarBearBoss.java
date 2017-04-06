package Evolution.Bosses;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.PolarBear;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

import Evolution.Main.Core;

public class PolarBearBoss implements CommandExecutor, Listener{
	
	@SuppressWarnings({ "unused", "deprecation" })
	public void createBoss(Location spawnLocation){
		PolarBear bear = (PolarBear) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.POLAR_BEAR);
		bear.setRemoveWhenFarAway(false);
		bear.setMaxHealth(100.0);
		bear.setHealth(100.0);
		bear.setAdult();
		bear.setCustomName(ChatColor.RED + "Polar Bear Boss");
		bear.setCustomNameVisible(true);
		
		// create a new zombie boss watcher
		PolarBearWatcher watcher = new PolarBearWatcher(bear);
		
		Core.bossLoader.addBoss(bear);
	}
	
	@EventHandler
	public void onPolarBearBossDamage(EntityDamageEvent e){
		if (e.getEntityType() == EntityType.POLAR_BEAR
				&& e.getEntity().getCustomName() != null
				&& e.getEntity().getCustomName().contains("Polar Bear Boss")){
			PolarBear bear = (PolarBear) e.getEntity();
			
			// update health
			bear.setCustomName(ChatColor.RED + "Polar Bear Boss " + ChatColor.WHITE + round(bear.getHealth()-e.getFinalDamage()) + ChatColor.RED + Core.heart);
			
			if (e.getCause() == DamageCause.PROJECTILE){
				// no knockback from ranged attacks
				Bukkit.getScheduler().scheduleSyncDelayedTask(Core.thisPlugin, new Runnable(){
					@Override
					public void run() {
						bear.setVelocity(new Vector(0.0, -0.5, 0.0));
					}
				}, 1L);
				
				// throw towards player 50% of ranged attacks by players
				if (Math.random() <= 0.5){
					EntityDamageByEntityEvent e2 = (EntityDamageByEntityEvent) e;
					Projectile projectile = (Projectile) e2.getDamager();
					if (!(projectile.getShooter() instanceof LivingEntity)){
						return; // must be living entity
					}
					LivingEntity target = (LivingEntity) projectile.getShooter();
					if (bear.equals(target)){
						return; // don't want him targeting himself
					}
					Bukkit.getScheduler().scheduleSyncDelayedTask(Core.thisPlugin, new Runnable(){
						@Override
						public void run() {
							double distance = target.getLocation().distance(bear.getLocation())*0.25;
							Vector diff = target.getLocation().subtract(bear.getLocation()).toVector().normalize().multiply(distance);
							diff.setY(0.5);
							bear.setVelocity(diff);
						}
					}, 2L);
				}
			}
		}
	}
	
	@EventHandler
	public void onPolarBearBossDamageByEntity(EntityDamageByEntityEvent e){
		if (e.getEntityType() == EntityType.POLAR_BEAR
				&& e.getEntity().getCustomName() != null
				&& e.getEntity().getCustomName().contains("Polar Bear Boss")
				&& e.getCause() == DamageCause.ENTITY_ATTACK 
				&& e.getDamager() instanceof LivingEntity){
			PolarBear bear = (PolarBear) e.getEntity();
			
			// no knockback from melee attacks
			Bukkit.getScheduler().scheduleSyncDelayedTask(Core.thisPlugin, new Runnable(){
				@Override
				public void run() {
					bear.setVelocity(new Vector(0.0, -0.5, 0.0));
				}
			}, 1L);
			
		}
	}
	
	public double round(double input){
		input = Math.round( (input*10.0) )/10.0;
		return input;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if (cmd.getName().equalsIgnoreCase("polarbearboss")){
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
