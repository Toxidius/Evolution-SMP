package Evolution.GameMechanics;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerReplenishTradeEvent;

import Evolution.Main.ParticleEffect;

public class VillagerTradeReplenishEffect implements Listener{
	
	public VillagerTradeReplenishEffect() {
		// TODO Auto-generated constructor stub
	}
	
	@EventHandler
	public void onVillagerTradeReplenish(VillagerReplenishTradeEvent e){
		
		Location temp;
		for (double y = 0; y <= 2.0; y+=0.1){
			temp = e.getEntity().getLocation().add(Math.random()-0.5, y, Math.random()-0.5);
			ParticleEffect.ENDROD.display(0F, 0F, 0F, 0.01F, 1, temp, 40.0);
			//ParticleEffect.DRAGONBREATH.display(0F, 0F, 0F, 0.01F, 1, temp, 40.0);
			//ParticleEffect.DRAGONBREATH.display(0F, 0F, 0F, 0.01F, 1, temp, 40.0);
		}
	}

}
