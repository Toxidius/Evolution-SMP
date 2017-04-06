package Evolution.Bosses;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import Evolution.Main.Core;

public class ZombieBoss implements CommandExecutor, Listener{
	
	@SuppressWarnings({ "unused", "deprecation" })
	public void createBoss(Location spawnLocation){
		Zombie zombie = (Zombie) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.ZOMBIE);
		zombie.setRemoveWhenFarAway(false);
		zombie.setMaxHealth(100.0);
		zombie.setHealth(100.0);
		zombie.setBaby(false);
		zombie.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
		zombie.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
		zombie.getEquipment().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
		zombie.getEquipment().setBoots(new ItemStack(Material.IRON_BOOTS));
		zombie.getEquipment().setItemInMainHand(new ItemStack(Material.DIAMOND_SWORD));
		zombie.setCustomName(ChatColor.RED + "Zombie Boss");
		zombie.setCustomNameVisible(true);
		
		// create a new zombie boss watcher
		ZombieBossWatcher watcher = new ZombieBossWatcher(zombie);
		
		Core.bossLoader.addBoss(zombie);
	}
	
	@EventHandler
	public void onZombieBossDamage(EntityDamageEvent e){
		if (e.getEntityType() == EntityType.ZOMBIE
				&& e.getEntity().getCustomName() != null
				&& e.getEntity().getCustomName().contains("Zombie Boss")){
			Zombie zombie = (Zombie) e.getEntity();
			
			// update health
			zombie.setCustomName(ChatColor.RED + "Zombie Boss " + ChatColor.WHITE + round(zombie.getHealth()-e.getFinalDamage()) + ChatColor.RED + Core.heart);
			
			if (e.getCause() == DamageCause.FALL
					|| e.getCause() == DamageCause.SUFFOCATION
					|| e.getCause() == DamageCause.FIRE
					|| e.getCause() == DamageCause.FIRE_TICK){
				e.setCancelled(true); // no fall, suffocation, or fire dmg
			}
			else if (e.getCause() == DamageCause.PROJECTILE){
				// no knockback from ranged attacks
				Bukkit.getScheduler().scheduleSyncDelayedTask(Core.thisPlugin, new Runnable(){
					@Override
					public void run() {
						zombie.setVelocity(new Vector(0.0, -0.5, 0.0));
					}
				}, 1L);
				
				// throw towards player 75% of ranged attacks by players
				if (Math.random() <= 0.75){
					EntityDamageByEntityEvent e2 = (EntityDamageByEntityEvent) e;
					Projectile projectile = (Projectile) e2.getDamager();
					if (!(projectile.getShooter() instanceof LivingEntity)){
						return; // must be living entity
					}
					LivingEntity target = (LivingEntity) projectile.getShooter();
					Bukkit.getScheduler().scheduleSyncDelayedTask(Core.thisPlugin, new Runnable(){
						@Override
						public void run() {
							int yDiff = Math.abs(zombie.getLocation().getBlockY()-target.getLocation().getBlockY());
							double multiplier = zombie.getLocation().distance(target.getLocation())*0.15;
							Vector direction = zombie.getLocation().subtract(target.getLocation()).toVector().normalize().multiply(-multiplier);
							direction.setY(0.75 + yDiff*0.08);
							
							zombie.setVelocity(direction); // throw towards attacker (player)
						}
					}, 2L);
				}
			}
			
		}
	}
	
	@EventHandler
	public void onZombieBossDamageMelee(EntityDamageByEntityEvent e){
		if (e.getEntityType() == EntityType.ZOMBIE
				&& e.getEntity().getCustomName() != null
				&& e.getEntity().getCustomName().contains("Zombie Boss")
				&& e.getCause() == DamageCause.ENTITY_ATTACK 
				&& e.getDamager() instanceof LivingEntity){
			LivingEntity damager = (LivingEntity) e.getDamager();
			Zombie zombie = (Zombie) e.getEntity();
			
			// throw towards the target 50% of attacks
			if (Math.random() <= 0.5){
				Bukkit.getScheduler().scheduleSyncDelayedTask(Core.thisPlugin, new Runnable(){
					@Override
					public void run() {
						//Vector diff = damager.getLocation().subtract(zombie.getLocation()).toVector().normalize();
						//zombie.setVelocity(diff);
						
						int yDiff = Math.abs(zombie.getLocation().getBlockY()-damager.getLocation().getBlockY());
						Vector direction = null;
						if (Math.random() <= 0.5){
							// 50% of the time high jump
							double multiplier = zombie.getLocation().distance(damager.getLocation())*0.15;
							direction = zombie.getLocation().subtract(damager.getLocation()).toVector().normalize().multiply(-multiplier);
							direction.setY(0.75 + yDiff*0.08);
						}
						else{
							// 50% of the time now jump with 100% chance of damaging the player
							double multiplier = zombie.getLocation().distance(damager.getLocation())*0.25; // much more likely to hit the player
							direction = zombie.getLocation().subtract(damager.getLocation()).toVector().normalize().multiply(-multiplier);
							direction.setY(0.15 + yDiff*0.08);
						}
						
						zombie.setVelocity(direction); // throw towards attacker (player)
					}
				}, 1L);
			}
			
		}
	}
	
	public double round(double input){
		input = Math.round( (input*10.0) )/10.0;
		return input;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if (cmd.getName().equalsIgnoreCase("zombieboss")){
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
