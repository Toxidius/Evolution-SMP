package Evolution.GameMechanics;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import Evolution.Main.Core;

public class SantaPresentsEffect implements Runnable{

	private Location spawn;
	private int id;
	private int calls;
	
	public SantaPresentsEffect(Location spawn){
		this.spawn = spawn;
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 0, 60); // run every 60 ticks
		calls = 0;
	}
	
	@Override
	public void run() {
		if (calls >= 5){
			Bukkit.getScheduler().cancelTask(id);
			return;
		}
		calls++;
		
		Firework firework;
		FireworkMeta meta;
		FireworkEffect effect;
		firework = (Firework) spawn.getWorld().spawnEntity(spawn.clone().add(0.5, 0.0, 0.5), EntityType.FIREWORK);
		meta = firework.getFireworkMeta();
		meta.clearEffects();
		meta.setPower(2);
		effect = FireworkEffect.builder().trail(true).with(Type.BALL_LARGE).withColor(Color.RED).build();
		meta.addEffect(effect);
		firework.setFireworkMeta(meta);
	}
}
