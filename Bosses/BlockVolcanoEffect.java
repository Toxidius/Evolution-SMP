package Evolution.Bosses;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.util.Vector;

import Evolution.Main.Core;

public class BlockVolcanoEffect implements Runnable{

	private Location spawnLocation;
	private Material blockType;
	private byte blockData;
	private int id;
	private int calls;
	
	public BlockVolcanoEffect(Location spawnLocation, Material blockType, byte blockData){
		this.spawnLocation = spawnLocation;
		this.blockType = blockType;
		this.blockData = blockData;
		calls = 0;

		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 0L, 0L);
	}
	
	@Override
	public void run() {
		if (calls >= 100){
			// end task
			Bukkit.getScheduler().cancelTask(id);
			return;
		}
		
		// spew another block
		double xOff, zOff;
		double yOff = Math.random();
		xOff = Math.random()*2-1; // -1 to 1
		zOff = Math.random()*2-1; // -1 to 1
		
		@SuppressWarnings("deprecation")
		FallingBlock fallingBlock = spawnLocation.getWorld().spawnFallingBlock(spawnLocation, blockType, blockData);
		fallingBlock.setDropItem(false);
		fallingBlock.setVelocity(new Vector(xOff, yOff, zOff));
		
		calls++;
	}

}
