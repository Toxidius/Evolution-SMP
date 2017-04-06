package Evolution.GameMechanics;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class Loggers implements Listener{
	
	public Loggers(){
	}

	@EventHandler
	public void onSignChange(SignChangeEvent e){
		Block block = e.getBlock();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		
		String logText = dateFormat.format(date) + " " +e.getPlayer().getName() + " placed " + block.getType() + " at " + block.getWorld().getName() + " " + block.getX() + " " + block.getY() + " " + block.getZ();
		logText += " Line0:\"" + e.getLine(0) + "\"";
		logText += " Line1:\"" + e.getLine(1) + "\"";
		logText += " Line2:\"" + e.getLine(2) + "\"";
		logText += " Line3:\"" + e.getLine(3) + "\"";
		
		// write the logText to the file
		writeToLog("SignLog.txt", logText);
	}
	
	@EventHandler
	public void onNamedEntityDeath(EntityDamageEvent e){
		if (e.getEntity().getCustomName() != null
				&& e.getEntity() instanceof LivingEntity
				&& ((LivingEntity)e.getEntity()).getHealth()-e.getFinalDamage() <= 0.0
				&& ((LivingEntity)e.getEntity()).isDead() == false){
			// entity died that has a custom name
			
			Entity entity = e.getEntity();
			String entityName = entity.getCustomName();
			String logText = "";
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			logText += dateFormat.format(date) + " "; //2013/10/15 16:16:39
			
			if (entityName.contains("Structure Guardian") == true){
				return; // we don't want structure guardian kills to be logged
			}
			
			// log creation below
			if (e.getCause() == DamageCause.ENTITY_ATTACK){
				EntityDamageByEntityEvent e2 = (EntityDamageByEntityEvent)e; // cast to EntityDamageByEntity event so we can get the Damager entity
				
				if (e2.getDamager() instanceof Player){
					// killed by player
					String killerName = ((Player)e2.getDamager()).getName();
					logText += " " + entityName + " (" + entity.getType().name() + ") was killed by " + killerName + " at " + entity.getWorld().getName() + " " + entity.getLocation().getBlockX() + " " + entity.getLocation().getBlockY() + " " + entity.getLocation().getBlockZ();
				}
				else{
					// killed by some other entity
					logText += " " + entityName + " (" + entity.getType().name() + ") was killed by " + e2.getDamager().getType().name() + " at " + entity.getWorld().getName() + " " + entity.getLocation().getBlockX() + " " + entity.getLocation().getBlockY() + " " + entity.getLocation().getBlockZ();
				}
			}
			else{
				// killed by some other cause
				if (e.getCause() == DamageCause.PROJECTILE){
					EntityDamageByEntityEvent e2 = (EntityDamageByEntityEvent)e; // cast to EntityDamageByEntity event so we can get the Damager entity
					if (((Projectile)e2.getDamager()).getShooter() instanceof Player){
						// killed by projectile from player
						String projectileOwner = ((Player)((Projectile)e2.getDamager()).getShooter()).getName();
						logText += " " + entityName + " (" + entity.getType().name() + ") was killed by " + e.getCause().name() + " from " + projectileOwner + " at " + entity.getWorld().getName() + " " + entity.getLocation().getBlockX() + " " + entity.getLocation().getBlockY() + " " + entity.getLocation().getBlockZ();
					}
					else{
						// killed by projectile from some other source
						logText += " " + entityName + " (" + entity.getType().name() + ") was killed by " + e.getCause().name() + " at " + entity.getWorld().getName() + " " + entity.getLocation().getBlockX() + " " + entity.getLocation().getBlockY() + " " + entity.getLocation().getBlockZ();
					}					
				}
				else{
					logText += " " + entityName + " (" + entity.getType().name() + ") was killed by " + e.getCause().name() + " at " + entity.getWorld().getName() + " " + entity.getLocation().getBlockX() + " " + entity.getLocation().getBlockY() + " " + entity.getLocation().getBlockZ();
				}
			}
			
			// log writing below
			writeToLog("EntityLog.txt", logText);
		}
	}
	
	@EventHandler
	public void onChestBreak(BlockBreakEvent e){
		if (e.getBlock().getType() != Material.CHEST
				&& e.getBlock().getType() != Material.TRAPPED_CHEST){
			return; // not a chest block -- ignore
		}
		
		String logText = "";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		logText += dateFormat.format(date) + " "; // 2013/10/15 16:16:39
		
		// log creation below
		logText += e.getPlayer().getName() + " broke " + e.getBlock().getType().name() + " at " 
				+ e.getBlock().getWorld().getName() + " " + e.getBlock().getX() + " " + e.getBlock().getY() + " " + e.getBlock().getZ();
		
		// log writing below
		writeToLog("ChestBreakLog.txt", logText);
	}
	
	@EventHandler
	public void onExpExtractorCombustAttempt(ExplosionPrimeEvent e){
		if (e.getEntity() instanceof EnderCrystal
				&& e.getEntity().getCustomName() != null
				&& e.getEntity().getCustomName().equals(ChatColor.RED + "Exp Extractor")){
			Player nearest = null;
			for (Entity entity : e.getEntity().getNearbyEntities(40.0, 40.0, 40.0)){
				if (entity instanceof Player){
					nearest = (Player) entity;
					break;
				}
			}
			
			if (nearest != null){
				String logText = "";
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date();
				logText += dateFormat.format(date) + " "; // 2013/10/15 16:16:39
				
				// log creation below
				logText += nearest.getName() + " attempted to break an exp extractor at " + e.getEntity().getWorld().getName() 
						+ " " + e.getEntity().getLocation().getX() + " " + e.getEntity().getLocation().getY() + " " + e.getEntity().getLocation().getZ();
				
				// log writing below
				writeToLog("ExpExtractorCombustLog.txt", logText);
			}
		}
	}
	
	public void writeToLog(String logFileName, String logText){
		// Try writing the logText to the file. Must be in try catch block to handle the IOException if one is called.
		try{
			FileWriter fileWriter = new FileWriter(logFileName, true); // open the file name in logFileName for writing in append mode
			BufferedWriter bw = new BufferedWriter(fileWriter); // create new buffered writer using the fileWriter object
			bw.write(logText);
			bw.newLine();
			bw.flush();
			bw.close();
		} catch(IOException ex){
			System.out.println("Error when writing " + logFileName+ ": " + ex.getMessage());
		}
	}
	
}
