package Evolution.GameMechanics;

import org.bukkit.Location;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class HorseRideFix implements Listener{

	public HorseRideFix(){
	}
	
	@EventHandler
	public void onPlayerClickHorse(PlayerInteractAtEntityEvent e){
		if (e.getRightClicked() instanceof AbstractHorse){
			// the player is getting on a horse
			// set the horses yaw to the same as the players so that their camera angle is the same (prevents disorientation)
			Location newLocation = e.getRightClicked().getLocation();
			newLocation.setYaw(e.getPlayer().getLocation().getYaw());
			e.getRightClicked().teleport(newLocation);
		}
	}
	
}
