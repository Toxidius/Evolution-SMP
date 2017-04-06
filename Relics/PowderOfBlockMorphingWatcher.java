package Evolution.Relics;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import Evolution.Main.Core;

public class PowderOfBlockMorphingWatcher implements Runnable{

	private Player player;
	private FallingBlock fallingBlock;
	private Material blockType;
	private byte blockData;
	private int calls;
	private int id;
	
	public PowderOfBlockMorphingWatcher(Player player, Material blockType, byte blockData) {
		this.player = player;
		this.blockType = blockType;
		this.blockData = blockData;
		calls = 0;
		
		// spawn fallingBlock
		summon();
		
		// start runnable
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 0, 1); // update every tick
	}
	
	@SuppressWarnings("deprecation")
	public void summon(){
		Location spawnLocation = player.getLocation();
		
		fallingBlock = spawnLocation.getWorld().spawnFallingBlock(spawnLocation, blockType, blockData);
		fallingBlock.setDropItem(false);
		fallingBlock.setGravity(false);
		fallingBlock.setInvulnerable(true);
		fallingBlock.setTicksLived(Integer.MAX_VALUE);
		fallingBlock.setCustomName("noDrop"); // prevent entity to real block thingy
		fallingBlock.setFallDistance(0);
	}
	
	public void stop(){
		fallingBlock.remove();
		player.removePotionEffect(PotionEffectType.INVISIBILITY);
		Bukkit.getScheduler().cancelTask(id);
	}
	
	@Override
	public void run() {
		calls++;
		if (fallingBlock.isDead()){
			summon();
		}
		if (player.getWorld().equals(fallingBlock.getWorld()) == false){
			fallingBlock.teleport(player.getLocation());
			return;
		}
		if (fallingBlock.getLocation().distance(player.getLocation()) >= 2.0){
			fallingBlock.remove();
			summon();
		}
		Vector difference = player.getLocation().subtract(fallingBlock.getLocation()).toVector();
		fallingBlock.setVelocity(difference);
		
		if (calls%40 == 0){
			// every 2 seconds re-add invisibility potion effect
			player.removePotionEffect(PotionEffectType.INVISIBILITY);
			player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 80, 0, false, false)); // invisiblity for 4 seconds with no particles
		}
	}

}
