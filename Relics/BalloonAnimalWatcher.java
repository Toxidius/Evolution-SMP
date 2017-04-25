package Evolution.Relics;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import Evolution.Main.Core;

public class BalloonAnimalWatcher implements Runnable{

	private LivingEntity balloon;
	private Player owner;
	private boolean goingUp;
	private int id;
	
	public BalloonAnimalWatcher(LivingEntity balloon, Player owner) {
		this.balloon = balloon;
		this.owner = owner;
		goingUp = true;
		balloon.setVelocity(new Vector(0.0, 0.05, 0.0));
		
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 1, 1); // every tick
	}
	
	public void end(){
		balloon.setLeashHolder(null); // remove leash so it doesn't drop
		balloon.remove();
		if (Core.balloonAnimal.activePlayers.contains(owner)){
			Core.balloonAnimal.activePlayers.remove(owner);
		}
		if (Core.balloonAnimal.playerBalloonWatchers.containsKey(owner)){
			Core.balloonAnimal.playerBalloonWatchers.remove(owner);
		}
		Bukkit.getScheduler().cancelTask(id);
	}
	
	@Override
	public void run() {
		if (owner == null
				|| owner.isOnline() == false){
			end();
			return;
		}
		else if (balloon == null
				|| balloon.isDead()){
			end();
			return;
		}
		else if (balloon.getWorld().equals(owner.getWorld()) == false){
			end();
			return;
		}
		
		double distance = balloon.getLocation().distance(owner.getLocation());
		if (distance > 6.0){
			// getting too far away, teleport near the players location
			if (balloon instanceof LivingEntity){
				((LivingEntity)balloon).setLeashHolder(null); // remove the leash cause it'll glitch during the teleport
			}
			balloon.teleport(owner.getLocation().add(0.0, 1.5, 0.0));
			if (balloon instanceof LivingEntity){
				((LivingEntity)balloon).setLeashHolder(owner); // set leash again
			}
		}
		else if (distance > 5.0){
			double multiplier = owner.getEyeLocation().distance(balloon.getLocation())*0.1;
			Vector diff = owner.getEyeLocation().subtract(balloon.getLocation()).toVector().normalize().multiply(multiplier);
			
			balloon.setVelocity(diff);
		}
		else{
			double upper = owner.getLocation().getY()+3.5;
			double lower = owner.getLocation().getY()+2.5;
			
			if (goingUp == true){
				if (balloon.getLocation().getY() < upper){
					// move up more
					balloon.setVelocity(new Vector(balloon.getVelocity().getX(), 0.03, balloon.getVelocity().getZ())); // 0.05 old
				}
				else{
					// reached max height, go down now
					goingUp = false;
				}
			}
			else if (goingUp == false){
				if (balloon.getLocation().getY() > lower){
					// move down more
					balloon.setVelocity(new Vector(balloon.getVelocity().getX(), -0.03, balloon.getVelocity().getZ())); // -0.005 old
				}
				else{
					// reached lowest height, go up now
					goingUp = true;
				}
			}
		}
	}
}