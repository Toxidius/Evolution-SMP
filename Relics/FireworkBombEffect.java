package Evolution.Relics;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

import Evolution.Main.Core;

public class FireworkBombEffect implements Runnable{

	private Location spawn;
	private int id;
	private int calls;
	
	public FireworkBombEffect(Location spawn){
		this.spawn = spawn;
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 0, 15); // run every 15 ticks
		calls = 0;
	}
	
	@Override
	public void run() {
		if (calls >= 30){
			Bukkit.getScheduler().cancelTask(id);
			return;
		}
		calls++;
		
		Firework firework;
		FireworkMeta meta;
		FireworkEffect effect;
		Builder builder;
		firework = (Firework) spawn.getWorld().spawnEntity(spawn, EntityType.FIREWORK);
		firework.setVelocity(new Vector(Math.random()*0.04-0.02, Math.random()*0.5, Math.random()*0.04-0.02));
		meta = firework.getFireworkMeta();
		meta.clearEffects();
		meta.setPower(1);
		builder = FireworkEffect.builder().trail(true);
		//effect = FireworkEffect.builder().trail(true).with(Type.BALL_LARGE).withColor(Color.WHITE).build();
		
		switch (Core.r.nextInt(5)){
			case 1: builder = builder.with(Type.BALL);
					break;
			case 2: builder = builder.with(Type.BALL_LARGE);
					break;
			case 3: builder = builder.with(Type.BURST);
					break;
			case 4: builder = builder.with(Type.CREEPER);
					break;
			default: builder = builder.with(Type.STAR);
					break;
		}
		
		switch (Core.r.nextInt(12)){
			case 1: builder = builder.withColor(Color.WHITE);
					break;
			case 2: builder = builder.withColor(Color.BLACK);
					break;
			case 3: builder = builder.withColor(Color.RED);
					break;
			case 4: builder = builder.withColor(Color.ORANGE);
					break;
			case 5: builder = builder.withColor(Color.YELLOW);
					break;
			case 6: builder = builder.withColor(Color.GREEN);
					break;
			case 7: builder = builder.withColor(Color.LIME);
					break;
			case 8: builder = builder.withColor(Color.BLUE);
					break;
			case 9: builder = builder.withColor(Color.AQUA);
					break;
			case 10: builder = builder.withColor(Color.PURPLE);
					break;
			case 11: builder = builder.withColor(Color.TEAL);
					break;
			default: builder = builder.withColor(Color.fromRGB(255, 80, 230)); // pink
					break;
		}
		
		effect = builder.build();
		meta.addEffect(effect);
		firework.setFireworkMeta(meta);
	}
}
