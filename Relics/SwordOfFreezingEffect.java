package Evolution.Relics;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import Evolution.Main.Core;

public class SwordOfFreezingEffect implements Runnable{

	private Player player;
	private int calls;
	private int id;
	
	public SwordOfFreezingEffect(Player player) {
		this.player = player;
		calls = 0;
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 2, 2); // every 2 ticks, spawn another ice
	}
	
	@Override
	public void run() {
		if (calls > 30){
			Bukkit.getScheduler().cancelTask(id);
			return;
		}
		
		for (int i = 0; i < 2; i++){
			Location spawnLocation = player.getEyeLocation().add(player.getEyeLocation().getDirection());
			Vector direction = player.getEyeLocation().getDirection().normalize().multiply(1.0);
			direction.add(new Vector(Math.random()*0.25-0.125, Math.random()*0.25-0.125, Math.random()*0.25-0.125));
			
			@SuppressWarnings("deprecation")
			FallingBlock fallingBlock = player.getWorld().spawnFallingBlock(spawnLocation, Material.ICE, (byte)0);
			fallingBlock.setDropItem(false);
			fallingBlock.setVelocity(direction);
			
			@SuppressWarnings("unused")
			SwordOfFreezingIceWatcher watcher = new SwordOfFreezingIceWatcher(fallingBlock);
		}
		
		//if (calls%3 == 0){
			player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 0.5F, 2.0F); // volume, pitch
		//}
		
		calls++;
	}
}