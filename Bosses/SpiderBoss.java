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
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Spider;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import Evolution.Main.Core;

public class SpiderBoss implements CommandExecutor, Listener{
	
	@SuppressWarnings({ "unused", "deprecation" })
	public void createBoss(Location spawnLocation){
		Spider spider = (Spider) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.SPIDER);
		spider.setRemoveWhenFarAway(false);
		spider.setMaxHealth(60.0);
		spider.setHealth(60.0);
		spider.setCustomName(ChatColor.RED + "Spider Boss");
		spider.setCustomNameVisible(true);
		spider.setRemoveWhenFarAway(false);
		/*
		Skeleton skeleton = (Skeleton) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.SKELETON);
		skeleton.setMaxHealth(60.0);
		skeleton.setHealth(60.0);
		skeleton.getEquipment().setHelmet(new ItemStack(Material.GOLD_HELMET));
		skeleton.getEquipment().setChestplate(new ItemStack(Material.GOLD_CHESTPLATE));
		skeleton.getEquipment().setLeggings(new ItemStack(Material.GOLD_LEGGINGS));
		skeleton.getEquipment().setBoots(new ItemStack(Material.GOLD_BOOTS));
		spider.setPassenger(skeleton);
		*/
		
		// create a new spider boss watcher
		SpiderBossWatcher spiderBossWatcher = new SpiderBossWatcher(spider);
		
		Core.bossLoader.addBoss(spider);
	}
	
	@EventHandler
	public void onSpiderBossDamage(EntityDamageEvent e){
		if (e.getEntityType() == EntityType.SPIDER
				&& e.getEntity().getCustomName() != null
				&& e.getEntity().getCustomName().contains("Spider Boss")){
			Spider spider = (Spider) e.getEntity();
			
			// update health
			spider.setCustomName(ChatColor.RED + "Spider Boss " + ChatColor.WHITE + round(spider.getHealth()-e.getFinalDamage()) + ChatColor.RED + Core.heart);
			
			if (e.getCause() == DamageCause.PROJECTILE){
				// no knockback from ranged attacks
				Bukkit.getScheduler().scheduleSyncDelayedTask(Core.thisPlugin, new Runnable(){
					@Override
					public void run() {
						spider.setVelocity(new Vector(0.0, -0.5, 0.0));
					}
				}, 1L);
				EntityDamageByEntityEvent e2 = (EntityDamageByEntityEvent) e;
				Projectile projectile = (Projectile) e2.getDamager();
				if (!(projectile.getShooter() instanceof LivingEntity)){
					return; // must be living entity
				}
				LivingEntity target = (LivingEntity) projectile.getShooter();
				double distance = target.getLocation().distance(projectile.getLocation());
				
				spider.setTarget(target);
				if (distance >= 7.0 && distance < 10.0 && Math.random() < 0.5){
					// speed up move
					if (spider.hasPotionEffect(PotionEffectType.SPEED)){
						spider.removePotionEffect(PotionEffectType.SPEED);
					}
					spider.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, 1)); // speed 2, 4 seconds
				}
				else if (distance >= 10.0){
					// speed up move
					if (spider.hasPotionEffect(PotionEffectType.SPEED)){
						spider.removePotionEffect(PotionEffectType.SPEED);
					}
					spider.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 2)); // speed 3, 8 seconds
				}
			}
			else if (e.getCause() == DamageCause.FALL
					|| e.getCause() == DamageCause.SUFFOCATION){
				e.setCancelled(true); // no fall or suffocation dmg
			}
		}
	}
	
	public double round(double input){
		input = Math.round( (input*10.0) )/10.0;
		return input;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if (cmd.getName().equalsIgnoreCase("spiderboss")){
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
