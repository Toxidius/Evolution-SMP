package Evolution.Relics;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;

import Evolution.Main.Core;

public class SwordOfFreezingIceWatcher implements Runnable{

	private FallingBlock fallingBlock;
	private Location lastActiveLocation;
	private int countdown;
	private boolean waiting;
	private int id;
	
	public SwordOfFreezingIceWatcher(FallingBlock fallingBlock) {
		this.fallingBlock = fallingBlock;
		countdown = 100;
		waiting = false;
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 1, 1); // every tick it runs
	}
	
	@Override
	public void run() {
		if (waiting == true){
			if (countdown <= 0){
				if (lastActiveLocation.getBlock().getType() == Material.ICE){
					lastActiveLocation.getBlock().setType(Material.AIR);
					Bukkit.getScheduler().cancelTask(id);
				}
			}
			else{
				countdown--;
			}
			return;
		}
		
		if (fallingBlock == null){
			// fallingBlock has turned solid at lastActiveLocation
			waiting = true;
		}
		else if (fallingBlock.isOnGround() == true){
			// fallingBlock will turn solid at lastActiveLocation
			lastActiveLocation = fallingBlock.getLocation();
			if (lastActiveLocation.getBlock().getType() == Material.AIR){
				lastActiveLocation.getBlock().setType(Material.ICE);
			}
			fallingBlock.remove();
			waiting = true;
		}
	}
}