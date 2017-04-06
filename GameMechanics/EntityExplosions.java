package Evolution.GameMechanics;

import org.bukkit.Effect;
import org.bukkit.Particle;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

public class EntityExplosions implements Listener{
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent e){
		if (e.getEntity() instanceof Creeper){
			e.getEntity().getWorld().playEffect(e.getEntity().getLocation(), Effect.EXPLOSION_LARGE, 1);
			e.getEntity().getWorld().playEffect(e.getEntity().getLocation(), Effect.EXPLOSION_LARGE, 1);
			e.blockList().clear(); // prevent block breaking but not player damage or knockback
		}
		else if (e.getEntity() instanceof TNTPrimed){
			if (e.getEntity().hasMetadata("noBlockDamage")){
				e.blockList().clear(); // prevent the blocks from being broken
				e.getEntity().getWorld().spawnParticle(Particle.EXPLOSION_HUGE, e.getEntity().getLocation(), 2); // simulate explosion particles
			}
		}
	}
	
	@EventHandler
	public void onExplosionPrime(ExplosionPrimeEvent e){
		if (e.getEntity() instanceof TNTPrimed){
			if (e.getEntity().hasMetadata("radius")){
				int radius = e.getEntity().getMetadata("radius").get(0).asInt();
				e.setRadius(radius);
			}
		}
	}
}