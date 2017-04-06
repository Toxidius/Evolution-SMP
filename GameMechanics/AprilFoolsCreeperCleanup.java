package Evolution.GameMechanics;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import Evolution.Main.Core;

public class AprilFoolsCreeperCleanup implements Runnable{

	private Location location;
	
	public AprilFoolsCreeperCleanup(Location location) {
		this.location = location;
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Core.thisPlugin, this, 200); // 10 seconds later
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		// remove all fake dropped items
		for (Entity entity : location.getWorld().getNearbyEntities(location, 20.0, 20.0, 20.0)){
			if (entity instanceof Item
					&& entity.getCustomName() != null
					&& entity.getCustomName().equals("AprilFoolsTrollItem")){
				entity.remove();
			}
		}
		
		// "regenerate" chests
		Location temp;
		int radius = 5;
		int startX = location.getBlockX()-radius;
		int startY = location.getBlockY()-radius;
		int startZ = location.getBlockZ()-radius;
		int endX = location.getBlockX()+radius;
		int endY = location.getBlockY()+radius;
		int endZ = location.getBlockZ()+radius;
		for (int y = startY; y <= endY; y++){
			for (int x = startX; x <= endX; x++){
				for (int z = startZ; z <= endZ; z++){
					temp = new Location(location.getWorld(), x, y, z);
					if (temp.getBlock().getType() == Material.CHEST
							|| temp.getBlock().getType() == Material.TRAPPED_CHEST){
						sendAllPlayersBlock(temp.getBlock());
					}
				}
			}
		}
		
		// april fools message
		Player player;
		for (Entity entity : location.getWorld().getNearbyEntities(location, 30.0, 30.0, 30.0)){
			if (entity instanceof Player){
				player = (Player) entity;
				player.sendTitle(ChatColor.DARK_GREEN + "April", ChatColor.GREEN + "Fools!");
				Bukkit.getServer().broadcastMessage(ChatColor.GREEN + player.getName() + " just got pranked!");
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void sendAllPlayersBlock(Block block){
		block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getTypeId(), 10); // block break effect
		
		// tell clients that the block exists again
		for (Player player : Bukkit.getOnlinePlayers()){
			player.sendBlockChange(block.getLocation(), block.getType(), block.getData());
		}
	}
}
