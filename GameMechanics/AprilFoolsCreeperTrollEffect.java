package Evolution.GameMechanics;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import Evolution.Main.Core;

public class AprilFoolsCreeperTrollEffect implements Runnable{
	
	private Player player;
	private Creeper creeper1;
	
	public AprilFoolsCreeperTrollEffect(Player player) {
		this.player = player;
		Bukkit.getScheduler().scheduleSyncDelayedTask(Core.thisPlugin, this, 100); // 5 seconds later
	}
	
	@Override
	public void run() {
		Location spawnLocation = player.getLocation().add( player.getLocation().getDirection().multiply(-1.5) );
		
		creeper1 = (Creeper) player.getWorld().spawnEntity(spawnLocation, EntityType.CREEPER);
		creeper1.setCustomName("AprilFoolsTrollCreeper");
		creeper1.setTarget(creeper1); // tell it to target itself to explode automatically
	}

}
