package Evolution.Relics;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import Evolution.Main.Core;

public class LamboCart9000Watcher implements Runnable{

	private Minecart minecart;
	private int id;
	
	public LamboCart9000Watcher(Minecart minecart){
		this.minecart = minecart;
		
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 2, 2); // run every 2 ticks
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		if (minecart == null || minecart.isDead()){
			Bukkit.getScheduler().cancelTask(id);
			return;
		}
		
		if (minecart.getPassenger() != null && minecart.getPassenger() instanceof Player){
			Player player = (Player) minecart.getPassenger();
			
			Vector tempVector = player.getLocation().getDirection();
			tempVector.setY(0.0);
			Location tempLocation = minecart.getLocation().add(player.getLocation().getDirection());
			tempLocation.setY(minecart.getLocation().getY()+0.06); // 0.25
			Block blockInFront = tempLocation.getBlock();
			
			//blockInFront.setType(Material.STONE);
			//Bukkit.getServer().broadcastMessage("" + blockInFront.getType());
			if (blockInFront.getType().isSolid()){
				minecart.setVelocity(new Vector(0.0, 0.35, 0.0));
			}
			else{
				Vector velocity = player.getEyeLocation().getDirection().normalize();
				if (minecart.isOnGround() == false){
					velocity.multiply(0.75);
					minecart.getPassenger().setFallDistance(0);
					minecart.setFallDistance(0);
				}
				velocity.setY(minecart.getVelocity().getY());
				minecart.setVelocity(velocity);
			}
		}
		
	}
}
