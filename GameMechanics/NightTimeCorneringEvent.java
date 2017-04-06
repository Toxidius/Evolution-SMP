package Evolution.GameMechanics;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Evolution.Main.Core;

public class NightTimeCorneringEvent implements Runnable{
	
	private HashMap<String, Long> lastOccurance;
	
	public NightTimeCorneringEvent(){
		lastOccurance = new HashMap<>();
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 20L, 20L); // execute every second
	}

	@Override
	public void run() {
		// loop through all players checking if they are outside
		for (Player player : Bukkit.getServer().getOnlinePlayers()){
			if (player.isOnline() 
					&& isNight()
					&& player.getLocation().getBlock().getLightFromSky() >= (byte)12 
					&& player.getLocation().getBlock().getLightFromBlocks() == (byte)0
					&& player.getGameMode() == GameMode.SURVIVAL
					&& Math.random() <= 0.005
					&& player.getWorld().equals(Bukkit.getWorlds().get(0))
					&& isThereNearbyVillagers(player) == false){ // 0.5% chance (roughly every 3 minutes)
				// this player satisfies all requirements for cornering event to occur
				// now just check if it hasn't occured recently
				int secondsSinceLastOccurance = 300; // default of 300 or 5 minutes
				if (lastOccurance.containsKey(player.getName())){
					secondsSinceLastOccurance = (int) ((System.currentTimeMillis() - lastOccurance.get(player.getName()).longValue())/1000.0);
				}
				
				if (secondsSinceLastOccurance < 300){
					continue; // minimum time hasn't passed
				}
				
				// satisfies all requirements -- trigger "cornering" event
				int numSpawned = 0;
				Zombie zombie;
				Location center = player.getLocation();
				Location temp;
				int radius = 6; // 6 block radius
				double angle, xOff, zOff;
				for (int i = 0; i < 6; i++){
					angle = i * (Math.PI/3);
					xOff = Math.sin(angle)*radius;
					zOff = Math.cos(angle)*radius;
					
					temp = getRelativeHighest(new Location(player.getWorld(), center.getX()+xOff, center.getY(), center.getZ()+zOff), 6);
					if (temp != null){
						// spawn a zombie at this location
						zombie = (Zombie) player.getWorld().spawnEntity(temp, EntityType.ZOMBIE);
						zombie.setBaby(false);
						zombie.getEquipment().clear();
						//zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1200, 0, false, true)); // speed 1 for 1 minute
						numSpawned++;
					}
				}
				
				if (numSpawned > 0){
					// give player blindness for 5 seconds
					player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 0)); // blindness 1 for 5 seconds
					// spooky cave sound
					player.playSound(center, Sound.AMBIENT_CAVE, 3F, 0F); // low pitched spooky cave sound
					int rand = Core.r.nextInt(3); // 0 to 2
					if (rand == 0){
						player.sendMessage(ChatColor.DARK_GREEN + "Suddenly zombies approach you from out of no-where!");
					}
					else if (rand == 1){
						player.sendMessage(ChatColor.DARK_GREEN + "The earth begins to shake as zombies are resurected from the ground.");
					}
					else{
						player.sendMessage(ChatColor.DARK_GREEN + "An eery feeling overcomes your body. " + ChatColor.ITALIC + "I shouldn't be out here you think.");
					}
					// update time since last "cornering event"
					if (lastOccurance.containsKey(player.getName())){
						lastOccurance.replace(player.getName(), new Long(System.currentTimeMillis()));
					}
					else{
						lastOccurance.put(player.getName(), new Long(System.currentTimeMillis()));
					}
				}
			}
		}
	}
	
	public boolean isNight(){
		World world = Bukkit.getWorlds().get(0);
		if (world.getTime() >= 14000){ // 12000 is technically night, 14000 is after the sun has fully set
			return true;
		}
		return false;
	}
	
	public boolean isThereNearbyVillagers(Player player){
		int numVillagers = 0;
		for (Entity entity : player.getNearbyEntities(20.0, 20.0, 20.0)){
			if (entity instanceof Villager){
				numVillagers++;
				break;
			}
		}
		if (numVillagers > 0){
			return true;
		}
		return false;
	}
	
	public Location getRelativeHighest(Location location, int range){
		// gets the highest block at a specific x, z location given a specified range and a starting location
		// ignores blocks like water/stationary_water
		World world = location.getWorld();
		int startY = location.getBlockY() + range;
		int endY = location.getBlockY() - range;
		int x = location.getBlockX();
		int z = location.getBlockZ();
		Location tempLoc;
		
		for (int y = startY; y > endY; y--){
			tempLoc = new Location(world, x, y, z);
			Block current = tempLoc.getBlock();
			Block below = current.getRelative(BlockFace.DOWN);
			if ( ( current.getType().equals(Material.AIR) || current.getType().equals(Material.WATER) || current.getType().equals(Material.STATIONARY_WATER)) 
					&& ( !below.getType().equals(Material.AIR) && !below.getType().equals(Material.WATER) && !below.getType().equals(Material.STATIONARY_WATER) ) ){
				return current.getLocation();
			}
		}
		
		return null;
	}
}
