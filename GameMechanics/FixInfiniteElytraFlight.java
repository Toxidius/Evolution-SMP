package Evolution.GameMechanics;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class FixInfiniteElytraFlight implements Listener{
	
	public FixInfiniteElytraFlight(){
	}
	
	@EventHandler
	public void onPlayerBowSelfWhileFlying(EntityDamageByEntityEvent e){
		if (e.getCause() == DamageCause.PROJECTILE
				&& e.getDamager() instanceof Arrow
				&& e.getEntity() instanceof Player
				&& ((Arrow)e.getDamager()).getShooter().equals(e.getEntity())
				&& ((Player)e.getEntity()).isGliding()){
			e.setCancelled(true); // prevent infinite flight using punch 2 bow and elytra
		}
	}

}
