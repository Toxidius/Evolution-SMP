package Evolution.GameMechanics;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import Evolution.Main.Core;

public class StripMineTroll implements Listener{

	public StripMineTroll(){
	}
	
	@EventHandler
	public void onStripMineBlockBreak(BlockBreakEvent e){
		if (e.isCancelled()){
			return;
		}
		
		if (Core.r.nextInt(500) == 234){
			// 1 out of 500 chance of checking for stip mining
			if (e.getPlayer().getLocation().getBlockY() <= 16 
					&& e.getPlayer().getLocation().getBlockY() >= 7 
					&& e.getBlock().getType() == Material.STONE 
					&& e.getPlayer().getWorld().getEnvironment() == Environment.NORMAL
					&& e.getPlayer().getGameMode() == GameMode.SURVIVAL){
				int numNeighboringBlocks = 0;
				Location temp;
				
				// first level
				temp = e.getPlayer().getLocation();
				temp.add(1.0, 0.0, 0.0);
				if (temp.getBlock().getType() != Material.AIR){
					numNeighboringBlocks++;
				}
				temp = e.getPlayer().getLocation();
				temp.add(-1.0, 0.0, 0.0);
				if (temp.getBlock().getType() != Material.AIR){
					numNeighboringBlocks++;
				}
				
				temp = e.getPlayer().getLocation();
				temp.add(0.0, 0.0, 1.0);
				if (temp.getBlock().getType() != Material.AIR){
					numNeighboringBlocks++;
				}
				
				temp = e.getPlayer().getLocation();
				temp.add(0.0, 0.0, -1.0);
				if (temp.getBlock().getType() != Material.AIR){
					numNeighboringBlocks++;
				}
				
				// second level
				temp = e.getPlayer().getLocation();
				temp.add(1.0, 1.0, 0.0);
				if (temp.getBlock().getType() != Material.AIR){
					numNeighboringBlocks++;
				}
				temp = e.getPlayer().getLocation();
				temp.add(-1.0, 1.0, 0.0);
				if (temp.getBlock().getType() != Material.AIR){
					numNeighboringBlocks++;
				}
				
				temp = e.getPlayer().getLocation();
				temp.add(0.0, 1.0, 1.0);
				if (temp.getBlock().getType() != Material.AIR){
					numNeighboringBlocks++;
				}
				
				temp = e.getPlayer().getLocation();
				temp.add(0.0, 1.0, -1.0);
				if (temp.getBlock().getType() != Material.AIR){
					numNeighboringBlocks++;
				}
				
				// above player
				temp = e.getPlayer().getLocation();
				temp.add(0.0, 2.0, 0.0);
				if (temp.getBlock().getType() != Material.AIR){
					numNeighboringBlocks++;
				}
				
				// analyze
				if (numNeighboringBlocks >= 5){
					// player is probably strip mining -- trigger troll event
					trollEvent(e.getPlayer());
				}
			}
		}
	}
	
	public void trollEvent(Player player){
		int rand = Core.r.nextInt(2); // 0 to 1
		if (rand == 0){
			// gravel suffocation trap
			Location playerLoc = player.getLocation();
			Location temp;
			World world = player.getWorld();
			
			int startX = playerLoc.getBlockX()-1;
			int startY = playerLoc.getBlockY()+6;
			int startZ = playerLoc.getBlockZ()-1;
			int endX = playerLoc.getBlockX()+1;
			int endY = playerLoc.getBlockY()+2;
			int endZ = playerLoc.getBlockZ()+1;
			for (int y = startY; y >= endY; y--){
				for (int x = startX; x <= endX; x++){
					for (int z = startZ; z <= endZ; z++){
						temp = new Location(world, x, y, z);
						if (y == endY){
							temp.getBlock().setType(Material.AIR);
						}
						else{
							temp.getBlock().setType(Material.GRAVEL);
						}
					}
				}
			}
		}
		else if (rand == 1){
			// lava pocket trap
			Location playerLoc = player.getLocation();
			Location temp;
			
			temp = playerLoc.clone();
			temp.add(1.0, -1.0, 0.0);
			temp.getBlock().setType(Material.LAVA);
			temp = playerLoc.clone();
			temp.add(1.0, -2.0, 0.0);
			temp.getBlock().setType(Material.LAVA);
			
			temp = playerLoc.clone();
			temp.add(-1.0, -1.0, 0.0);
			temp.getBlock().setType(Material.LAVA);
			temp = playerLoc.clone();
			temp.add(-1.0, -2.0, 0.0);
			temp.getBlock().setType(Material.LAVA);
			
			temp = playerLoc.clone();
			temp.add(0.0, -1.0, 1.0);
			temp.getBlock().setType(Material.LAVA);
			temp = playerLoc.clone();
			temp.add(0.0, -2.0, 1.0);
			temp.getBlock().setType(Material.LAVA);
			
			temp = playerLoc.clone();
			temp.add(0.0, -1.0, -1.0);
			temp.getBlock().setType(Material.LAVA);
			temp = playerLoc.clone();
			temp.add(0.0, -2.0, -1.0);
			temp.getBlock().setType(Material.LAVA);
			
			temp = playerLoc.clone();
			temp.add(0.0, -2.0, 0.0);
			temp.getBlock().setType(Material.LAVA);
		}
	}
}
