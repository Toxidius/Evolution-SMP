package Evolution.HolidayRelics;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import Evolution.Main.Core;
import Evolution.Main.ParticleEffect;
public class EnchantedParticlesEffect implements Runnable{

	private int calls;
	private int id;
	private Player player;
	private int radius = 1;
	
	public EnchantedParticlesEffect(Player player){
		this.player = player;
		calls = 0;
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 0, 6); // every 6 ticks
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
		
		Location playerLoc = player.getLocation();
		for (int i = 0; i <= 64; i++){
			double angle = i * (Math.PI/32);
			double xOff = Math.sin(angle)*radius;
			double zOff = Math.cos(angle)*radius;
			Vector direction = new Vector(xOff*15, 0.0, zOff*15);
			Location playLocation = new Location(player.getWorld(), playerLoc.getX()+xOff, playerLoc.getY()+2.0, playerLoc.getZ()+zOff);
			ParticleEffect.ENCHANTMENT_TABLE.display(direction, (float)0.1, playLocation, 20.0);
			
			Vector direction2 = new Vector(xOff*15, -20.0, zOff*15);
			Location playLocation2 = new Location(player.getWorld(), playerLoc.getX()+xOff, playerLoc.getY()+2.0, playerLoc.getZ()+zOff);
			ParticleEffect.ENCHANTMENT_TABLE.display(direction2, (float)0.1, playLocation2, 20.0);
		}
		
		if (calls >= 32){
			calls = 0;
		}
	}

}
