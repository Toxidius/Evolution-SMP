package Evolution.Bosses;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import Evolution.Main.Core;
import Evolution.Main.ParticleEffect;

public class CreeperDetonateEffect implements Runnable{

	private Creeper creeper;
	private double maxRadius;
	private double radius;
	private int id;
	
	public CreeperDetonateEffect(Creeper creeper, double maxRadius) {
		this.creeper = creeper;
		this.maxRadius = maxRadius;
		this.radius = 0.25;
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 5, 5); // every 5 ticks
	}
	
	public void end(){
		Bukkit.getScheduler().cancelTask(id);
	}
	
	public void explosion(){
		TNTPrimed tnt = (TNTPrimed) creeper.getWorld().spawnEntity(creeper.getLocation(), EntityType.PRIMED_TNT);
		tnt.setMetadata("radius", new FixedMetadataValue(Core.thisPlugin, new Integer((int) maxRadius-1)));
		tnt.setMetadata("noBlockDamage", new FixedMetadataValue(Core.thisPlugin, true));
		tnt.setFuseTicks(1);
		
	}
	
	@Override
	public void run() {
		if (creeper == null
				|| creeper.isDead()){
			end();
			return;
		}
		if (radius > maxRadius){
			explosion();
			end();
			return;
		}
		
		// "warning" effect
		Location center = creeper.getLocation();
		double xOff, zOff;
		double angle;
		Location temp;
		for (int i = 0; i <= 64; i++){
			angle = i * (Math.PI/32);
			xOff = Math.sin(angle)*radius;
			zOff = Math.cos(angle)*radius;
			temp = new Location(center.getWorld(), (center.getX()+xOff), center.getY(), (center.getZ()+zOff));
			ParticleEffect.FLAME.display(new Vector(0.0, 0.2, 0.0), 0.1F, temp, 40);
		}
		
		radius += 0.25;
	}

}
