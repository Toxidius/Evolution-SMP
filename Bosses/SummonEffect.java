package Evolution.Bosses;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;

import Evolution.Bosses.BossSummoner.BossType;
import Evolution.Main.Core;

public class SummonEffect implements Runnable{
	
	private Location summonLocation;
	private Location temp;
	private BossType bossType;
	private int calls;
	private int iterations = 0;
	private int radius = 2;
	private int id;
	
	public SummonEffect(Location summonLocation, BossType bossType){
		this.summonLocation = summonLocation;
		this.bossType = bossType;
		calls = 0;
		iterations = 0;
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 1L, 1L);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		if (iterations > 3){
			// end the effect
			summonLocation.getWorld().playSound(summonLocation, Sound.ENTITY_ELDER_GUARDIAN_DEATH, 5F, 0F);
			//summonLocation.getWorld().playSound(summonLocation, Sound.ENTITY_SKELETON_HORSE_DEATH, 5F, 0F);
			summonLocation.getWorld().playEffect(summonLocation.clone().add(0.0, 1.0, 0.0), Effect.EXPLOSION_HUGE, 10);
			summonLocation.getWorld().playEffect(summonLocation.clone().add(0.0, 1.0, 0.0), Effect.EXPLOSION_HUGE, 10);
			Bukkit.getScheduler().cancelTask(id);
			Core.bossSummoner.spawnBoss(bossType, summonLocation);// summon the mob
			return;
		}
		if (calls > 60){
			iterations++;
			calls = 0;
		}
		double xOff, zOff;
		double angle = calls * (Math.PI/30);
		xOff = Math.sin(angle)*radius;
		zOff = Math.cos(angle)*radius;
		for (double yOff = 0; yOff <= 3.0; yOff+=0.1){
			temp = new Location(summonLocation.getWorld(), (summonLocation.getX()+xOff), summonLocation.getY()+yOff, (summonLocation.getZ()+zOff));
			summonLocation.getWorld().playEffect(temp, Effect.WITCH_MAGIC, 1);
		}
		if (calls%30 == 0 && iterations != 3){
			summonLocation.getWorld().playSound(summonLocation, Sound.AMBIENT_CAVE, 2F, 0F);
		}
		
		calls++;
	}

}
