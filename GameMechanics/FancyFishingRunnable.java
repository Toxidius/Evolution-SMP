package Evolution.GameMechanics;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FishHook;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import Evolution.Main.Core;

public class FancyFishingRunnable implements Runnable{
	
	private int maxY;
	private int id;
	//private int cooldownTime;
	private ArmorStand stand;
	//private Location destination;
	private int numMoves;
	FancyFishing fancyFishing;
	
	public FancyFishingRunnable(FishHook hook, FancyFishing fancyFishing, float yaw){
		maxY = hook.getLocation().getBlockY();
		this.fancyFishing = fancyFishing;
		numMoves = 20;
		//cooldownTime = 0;
		
		Location base = hook.getLocation().add(0.0, -3.0, 0.0); // spawn offset from the bobbers location
		Location spawnLocation = getSpawnLocation(base);
		spawnLocation.setYaw(yaw);
		stand = (ArmorStand) hook.getWorld().spawnEntity(spawnLocation, EntityType.ARMOR_STAND);
		stand.setSilent(true);
		stand.setGravity(true);
		stand.setMarker(true); // prevent interacting with by players
		stand.setVisible(false);
		stand.setSmall(false);
		stand.setHelmet(new ItemStack(Material.RAW_FISH, 1, (byte)Core.r.nextInt(4))); // random fish
		fancyFishing.activeArmorStands.add(stand);
		
		//Item item = hook.getWorld().dropItem(spawnLocation, new ItemStack(Material.RAW_FISH));
		//stand.setPassenger(item);
		
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, Core.r.nextInt(20), 20); // start runnable
	}

	@Override
	public void run() {
		if (stand == null || stand.isDead()){
			Bukkit.getScheduler().cancelTask(id);
			fancyFishing.activeArmorStands.remove(stand);
			return;
		}
		
		if (numMoves == 0){
			stand.remove();
			Bukkit.getScheduler().cancelTask(id);
			fancyFishing.activeArmorStands.remove(stand);
			return;
		}
		
		double yComponent = 0.3;
		if (stand.getLocation().getBlock().isLiquid() == false 
				|| stand.getEyeLocation().getBlock().isLiquid() == false){
			yComponent = 0.0;
		}
		
		stand.setVelocity(new Vector(Math.random()-0.5, yComponent, Math.random()-0.5));
		
		numMoves--;
	}
	
	public Location getSpawnLocation(Location base){
		double xOff = Math.random()*3;
		double zOff = Math.random()*3;
		for (int i = 0; i < 15; i++){
			// try 15 times to get a valid spawning location
			if (base.clone().add(xOff, 0.0, zOff).getBlock().isLiquid()){
				return base.clone().add(xOff, 0.0, zOff);
			}
		}
		return base;
	}
	
	public Location getNextDestination(){
		Location current = stand.getLocation().clone();
		Location temp;
		for (int i = 0; i < 10; i++){
			// try for new destination 10 times
			temp = current.add(Math.random()*4.0-2, maxY, Math.random()*4.0-2);
			if (temp.getBlock().isLiquid()){
				return temp;
			}
		}
		return null;
	}
}
