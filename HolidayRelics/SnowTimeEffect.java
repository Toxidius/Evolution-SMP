package Evolution.HolidayRelics;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import Evolution.Main.Core;
import Evolution.Main.ParticleEffect;
public class SnowTimeEffect implements Runnable{

	private int calls;
	private int id;
	private Player player;
	private int radius = 2;
	
	public SnowTimeEffect(Player player){
		this.player = player;
		calls = 0;
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 0, 2); // every 2 ticks
	}
	
	public void end(){
		Bukkit.getScheduler().cancelTask(id);
		return;
	}
	
	@Override
	public void run() {
		if (player == null || player.isOnline() == false){
			end();
			return;
		}
		
		calls++;
		double angle = calls * (Math.PI/16);
		double xOff = Math.sin(angle)*radius;
		double zOff = Math.cos(angle)*radius;
		Location temp = player.getLocation().add(xOff, 0.0, zOff);
		
		// circular particles
		ParticleEffect.FIREWORKS_SPARK.display(new Vector(0.0, 5.0, 0.0), 0.05F, temp, 40);
		
		// helix particles
		Vector direction = new Vector(xOff*0.25, 0.0, zOff*0.25);
		Vector direction2 = new Vector(-xOff*0.25, 0.0, -zOff*0.25);
		//Bukkit.getServer().broadcastMessage("angle: " + calls + " x: " + xOff  + " z: " + zOff);
		temp = player.getLocation().add(0.0, 0.1, 0.0);
		for (int i = 0; i <= 5; i++){
			ParticleEffect.FIREWORKS_SPARK.display(direction, (float)0.7, temp, 40.0);
		}
		for (int i = 0; i <= 5; i++){
			ParticleEffect.FIREWORKS_SPARK.display(direction2, (float)0.7, temp, 40.0);
		}
		
		if (calls >= 32){
			calls = 0;
		}
	}

}
