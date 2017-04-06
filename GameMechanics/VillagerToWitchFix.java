package Evolution.GameMechanics;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;

public class VillagerToWitchFix implements Listener{

	public VillagerToWitchFix(){
	}
	
	@EventHandler
	public void onVillagerHitByLightning(LightningStrikeEvent e){
		for (Entity entity : e.getLightning().getNearbyEntities(4.0, 4.0, 4.0)){
			if (entity instanceof Villager){
				e.setCancelled(true);
				return;
			}
		}
	}
	
}
