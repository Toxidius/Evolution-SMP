package Evolution.GameMechanics;

import org.bukkit.Location;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.util.Vector;

public class EggThrowFix implements Listener{
	
	public EggThrowFix(){
	}

	@EventHandler
	public void onEggThrow(ProjectileLaunchEvent e){
		if (e.getEntity() instanceof Egg
				&& e.getEntity().getShooter() instanceof Player){
			// fixes eggs that hit the player once thrown
			Vector velocity = e.getEntity().getVelocity();
			Player shooter = (Player) e.getEntity().getShooter();
			Location spawnLocation = shooter.getEyeLocation();
			spawnLocation.add(shooter.getEyeLocation().getDirection().multiply(1.0));
			
			e.getEntity().remove();
			
			Egg egg = (Egg) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.EGG);
			egg.setShooter(shooter);
			egg.setVelocity(velocity);
		}
	}
}
