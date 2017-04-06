package Evolution.Bosses;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import Evolution.Main.Core;

public class PumpkinBoss implements CommandExecutor, Listener{
	
	@SuppressWarnings({ "unused", "deprecation" })
	public void createBoss(Location spawnLocation){
		Skeleton skeleton = (Skeleton) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.WITHER_SKELETON);
		skeleton.setRemoveWhenFarAway(false);
		skeleton.setMaxHealth(100.0);
		skeleton.setHealth(100.0);
		skeleton.getEquipment().setHelmet(new ItemStack(Material.PUMPKIN));
		skeleton.getEquipment().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
		skeleton.getEquipment().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
		skeleton.getEquipment().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
		skeleton.getEquipment().setItemInMainHand(new ItemStack(Material.IRON_AXE));
		skeleton.setCustomName(ChatColor.RED + "Pumpkin Boss");
		skeleton.setCustomNameVisible(true);
		
		// create a new zombie boss watcher
		PumpkinBossWatcher watcher = new PumpkinBossWatcher(skeleton);
		
		Core.bossLoader.addBoss(skeleton);
	}
	
	@EventHandler
	public void onPumpkinBossDamage(EntityDamageEvent e){
		if (e.getEntityType() == EntityType.WITHER_SKELETON
				&& e.getEntity().getCustomName() != null
				&& e.getEntity().getCustomName().contains("Pumpkin Boss")){
			Skeleton skeleton = (Skeleton) e.getEntity();
			
			if (e.getCause() == DamageCause.FALL
					|| e.getCause() == DamageCause.SUFFOCATION){
				e.setCancelled(true); // no fall or suffocation dmg
			}
			
			// update health
			if (e.isCancelled() == false){
				skeleton.setCustomName(ChatColor.RED + "Pumpkin Boss " + ChatColor.WHITE + round(skeleton.getHealth()-e.getFinalDamage()) + ChatColor.RED + Core.heart);
			}
			
			if (e.getCause() == DamageCause.PROJECTILE){
				// no knockback from ranged attacks
				Bukkit.getScheduler().scheduleSyncDelayedTask(Core.thisPlugin, new Runnable(){
					@Override
					public void run() {
						skeleton.setVelocity(new Vector(0.0, -0.5, 0.0));
					}
				}, 1L);
				
				// launch arrow back at the player
				EntityDamageByEntityEvent e2 = (EntityDamageByEntityEvent) e;
				Projectile projectile = (Projectile) e2.getDamager();
				if (!(projectile.getShooter() instanceof LivingEntity)){
					return; // must be living entity
				}
				LivingEntity target = (LivingEntity) projectile.getShooter();
				double distance = target.getLocation().distance(projectile.getLocation())*0.25;
				Vector direction = target.getEyeLocation().subtract(projectile.getLocation()).toVector().normalize().multiply(distance);
				
				Arrow arrow = skeleton.launchProjectile(Arrow.class, direction);
				arrow.setCustomName("Barrage");
				arrow.setShooter(skeleton);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPumpkinBossDamageMelee(EntityDamageByEntityEvent e){
		if (e.getEntityType() == EntityType.WITHER_SKELETON
				&& e.getEntity().getCustomName() != null
				&& e.getEntity().getCustomName().contains("Pumpkin Boss")
				&& e.getCause() == DamageCause.ENTITY_ATTACK 
				&& e.getDamager() instanceof LivingEntity){
			LivingEntity living = (LivingEntity) e.getDamager();
			Skeleton skeleton = (Skeleton) e.getEntity();
			
			// check if low on health (less than 1/4th) -- if so throw the attacker in the air
			if (skeleton.getHealth() <= skeleton.getMaxHealth()*0.25){
				Bukkit.getScheduler().scheduleSyncDelayedTask(Core.thisPlugin, new Runnable(){
					@Override
					public void run() {
						living.setVelocity(new Vector(0.0, 1.0, 0.0));
					}
				}, 1L);
				return; // don't want to execute the throw attack too
			}
			
			// throw towards player 50% of attacks
			if (Math.random() <= 0.5){
				Bukkit.getScheduler().scheduleSyncDelayedTask(Core.thisPlugin, new Runnable(){
					@Override
					public void run() {
						Vector diff = living.getLocation().subtract(skeleton.getLocation()).toVector().normalize();
						skeleton.setVelocity(diff);
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
		if (cmd.getName().equalsIgnoreCase("pumpkinboss")){
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
