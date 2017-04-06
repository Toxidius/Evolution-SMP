package Evolution.GameMechanics;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import Evolution.Main.Core;

public class BalloonAnimalWatcher implements Runnable{

	private LivingEntity balloon;
	private Player owner;
	private int id;
	
	public BalloonAnimalWatcher(LivingEntity balloon, Player owner) {
		this.balloon = balloon;
		this.owner = owner;
		balloon.setVelocity(new Vector(0.0, 0.05, 0.0));
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 1, 1); // every tick
	}
	
	public void end(){
		Bukkit.getScheduler().cancelTask(id);
	}
	
	@Override
	public void run() {
		if (owner == null
				|| owner.isOnline() == false){
			balloon.setLeashHolder(null);
			balloon.remove();
			end();
		}
		else if (balloon == null
				|| balloon.isDead()){
			end();
		}
		else if (balloon.getWorld().equals(owner.getWorld()) == false){
			balloon.setLeashHolder(null);
			balloon.remove();
			end();
		}
		else{
			double upper = owner.getLocation().getY()+4.0;
			double lower = owner.getLocation().getY()+3.0;
			if (balloon.getLocation().getY() < lower){
				balloon.setVelocity(new Vector(0.0, 0.05, 0.0));
				//Location newLocation = balloon.getLocation();
				//newLocation.setY(owner.getLocation().getY()+3.0);
				//balloon.teleport(newLocation);
			}
			else if (balloon.getLocation().getY() > upper){
				balloon.setVelocity(new Vector(0.0, -0.005, 0.0));
			}
		}
	}

}
