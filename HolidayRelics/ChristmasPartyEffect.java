package Evolution.HolidayRelics;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import Evolution.Main.Core;
import Evolution.Main.ParticleEffect;
import Evolution.Main.ParticleEffect.OrdinaryColor;

public class ChristmasPartyEffect implements Runnable{

	private Player player;
	private Location temp;
	private OrdinaryColor red;
	private OrdinaryColor green;
	@SuppressWarnings("unused")
	private OrdinaryColor white;
	private OrdinaryColor gold;
	private OrdinaryColor chosenColor;
	private ArrayList<Block> leafBlocks;
	private int id;
	private int calls;
	
	public ChristmasPartyEffect(Player player){
		this.player = player;
		calls = 0;
		red = new OrdinaryColor(205, 65, 50);
		green = new OrdinaryColor(34, 139, 34);
		white = new OrdinaryColor(255, 255, 255);
		gold = new OrdinaryColor(255, 215, 0);
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 1, 2); // run every 2 ticks
		leafBlocks = new ArrayList<>();
		updateLeafBlocks();
	}
	
	public void end(){
		Bukkit.getScheduler().cancelTask(id);
		return;
	}
	
	public void updateLeafBlocks(){
		// iterate over nearby blocks looking for leaf blocks
		// for every leaf block, add it to the list of blocks
		leafBlocks = new ArrayList<>();
		Location center = player.getLocation();
		int startX = center.getBlockX()-10;
		int startY = center.getBlockY()-2;
		int startZ = center.getBlockZ()-10;
		int endX = center.getBlockX()+10;
		int endY = center.getBlockY()+13;
		int endZ = center.getBlockZ()+10;
		for (int y = startY; y <= endY; y++){
			for (int x = startX; x <= endX; x++){
				for (int z = startZ; z <= endZ; z++){
					temp = new Location(center.getWorld(), x, y, z);
					if (temp.getBlock().getType() == Material.LEAVES
							|| temp.getBlock().getType() == Material.LEAVES_2){
						if (temp.getBlock().getRelative(BlockFace.DOWN).getType().isSolid() == false){
							leafBlocks.add(temp.getBlock());
						}
					}
				}
			}
		}
	}
	
	@Override
	public void run() {
		if (player == null || player.isOnline() == false){
			end();
			return;
		}
		calls++;
		if (calls%400 == 0){
			// every 20 seconds update leafBlocks
			updateLeafBlocks();
		}
		
		// particles
		Location center = player.getLocation();
		for (int i = 0; i < 30; i++){
			temp = center.clone().add(Math.random()*20-10, Math.random()*4, Math.random()*20-10);
			//temp.getWorld().spigot().playEffect(temp, Effect.COLOURED_DUST, 1, 1, 255, 255, 255, 1, 0, 30);
			switch (Core.r.nextInt(4)){
				case 0: chosenColor = red;
						break;
				case 1: chosenColor = green;
						break;
				case 2: chosenColor = red;
						break;
				case 3: chosenColor = gold;
						break;
				default: chosenColor = red;
						break;
			}
			ParticleEffect.REDSTONE.display(chosenColor, temp, 40);
			//ParticleEffect.REDSTONE.display(new OrdinaryColor(5, 5, 5), temp, 20);
			//temp.getWorld().spawnParticle(Particle.REDSTONE, temp, 1, (short)1);
		}
		
		for (int i = 0; i < leafBlocks.size(); i++){
			if (Math.random() <= 0.01){
				leafParticle(leafBlocks.get(i));
			}
		}
		
		// random sparks
		//ParticleEffect.FIREWORKS_SPARK.display(new Vector(0.0, 0.1, 0.0), 0, temp, 30);
	}
	
	public void leafParticle(Block block){
		ParticleEffect.FIREWORKS_SPARK.display(new Vector(0.0, 0.1, 0.0), 0, block.getLocation(), 40);
		ParticleEffect.FIREWORKS_SPARK.display(new Vector(0.0, 0.1, 0.0), 0, block.getLocation().clone().add(1.0, 0.0, 0.0), 40);
		ParticleEffect.FIREWORKS_SPARK.display(new Vector(0.0, 0.1, 0.0), 0, block.getLocation().clone().add(1.0, 0.0, 1.0), 40);
		ParticleEffect.FIREWORKS_SPARK.display(new Vector(0.0, 0.1, 0.0), 0, block.getLocation().clone().add(0.0, 0.0, 1.0), 40);
	}
}
