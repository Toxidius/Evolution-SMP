package Evolution.HolidayRelics;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import Evolution.Main.Core;
import Evolution.Main.ParticleEffect;
import Evolution.Main.ParticleEffect.ItemData;

public class NewYearsPartyEffect implements Runnable{

	private Player player;
	private int calls;
	private int id;
	public boolean isRunning;
	private double radiusMin = 1.0;
	private double radiusMax = 10.0;
	private double currentRadius = radiusMin;
	private boolean increasing = true;
	
	public NewYearsPartyEffect(Player player) {
		this.player = player;
		calls = 0;
		isRunning = true;
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 1, 1); // every 2 ticks run
	}
	
	public void end(){
		Bukkit.getScheduler().cancelTask(id);
		isRunning = false;
	}
	
	@Override
	public void run() {
		if (player == null || player.isOnline() == false){
			end();
			return;
		}
		if (calls > 200){
			calls = 0;
			//end();
			return;
		}
		
		calls++;
		Material material = Material.WOOL;
		ItemData data;
		double xOff, zOff;
		double radius = currentRadius;
		double angle;
		Location center = player.getLocation();
		Location temp;
		Vector upDirection = new Vector(0.0, 1.0, 0.0);
		for (int calls = 0; calls < 120; calls++){
			angle = calls * (Math.PI/60);
			xOff = Math.sin(angle)*radius;
			zOff = Math.cos(angle)*radius;
			temp = center.clone().add(xOff, 0.0, zOff);
			data = new ItemData(material, (byte)Core.r.nextInt(16));
			ParticleEffect.ITEM_CRACK.display(data, upDirection, 0.5F, temp, 40);
			//ParticleEffect.SPIT.display(upDirection, 0.25F, temp, 40);
		}
		//Bukkit.getServer().broadcastMessage("" + currentRadius);
		if (increasing){
			currentRadius += ((radiusMax-radiusMin)/200.0)*4.0;
		}
		else{
			currentRadius -= ((radiusMax-radiusMin)/200.0)*4.0;
		}
		
		if (currentRadius > radiusMax){
			increasing = false;
		}
		else if (currentRadius < radiusMin){
			currentRadius = radiusMin;
			increasing = true;
		}
	}

}
