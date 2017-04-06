package Evolution.Relics;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.block.Block;

import Evolution.Main.Core;

public class LumberAxeEffect implements Runnable{

	private ArrayList<Block> logs;
	private int calls;
	private int id;
	
	public LumberAxeEffect(ArrayList<Block> logs) {
		this.logs = logs;
		calls = 0;
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 1, 1); // every 5 ticks run
	}
	
	public void end(){
		Bukkit.getScheduler().cancelTask(id);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		if (calls >= logs.size()){
			end(); // end the runnable
			return;
		}
		
		logs.get(calls).getWorld().playEffect(logs.get(calls).getLocation(), Effect.STEP_SOUND, logs.get(calls).getTypeId(), 10); // block break effect
		logs.get(calls).breakNaturally();
		calls++;
	}
}
