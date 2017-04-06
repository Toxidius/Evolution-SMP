package Evolution.GameMechanics;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

import Evolution.Main.Core;

public class FixChunkUnloading implements Listener{

	private World mainOverworld;
	
	public FixChunkUnloading(){
		mainOverworld = Bukkit.getWorlds().get(0);
	}
	
	@EventHandler
	public void onChunkUnload(ChunkUnloadEvent e){
		if (e.getWorld().equals(mainOverworld) == false){
			return; // ignore non-overworld worlds
		}
		
		Chunk chunk = e.getChunk();
		
		int numDespawnableHostiles = 0;
		for (Entity entity : chunk.getEntities()){
			if (entity instanceof Monster
					&& ((Monster) entity).getRemoveWhenFarAway() == true
					&& isEligibleMonster((Monster) entity)){
				numDespawnableHostiles++;
			}
		}
		
		if (numDespawnableHostiles > 0){
			e.setCancelled(true); // prevent the chunk to unload
			despawnAllHostiles(chunk); // force the hostiles to despawn
			
			// wait 10 ticks until we allow the chunk to correctly unload
			//Bukkit.getServer().broadcastMessage("Prevented " + chunk.getX() + "-" + chunk.getZ() + " from unloading. num despawnables: " + numDespawnableHostiles );
			Bukkit.getScheduler().scheduleSyncDelayedTask(Core.thisPlugin, new Runnable(){
				@Override
				public void run() {
					chunk.unload();
					//Bukkit.getServer().broadcastMessage("Unloaded " + chunk.getX() + "-" + chunk.getZ());
				}
			}, 30L); // 30 ticks later force said chunk to unload
		}
	}
	
	public boolean isEligibleMonster(Monster monster){
		if (monster instanceof Zombie
				|| monster instanceof Skeleton
				|| monster instanceof Creeper
				|| monster instanceof Spider
				|| monster instanceof Enderman
				|| monster instanceof Witch){
			return true;
		}
		return false;
	}
	
	public boolean hasEquipment(Monster monster){
		if (monster.getType() == EntityType.SKELETON){
			return false; // ignore bow in hand for skeletons
		}
		else if (monster.getEquipment().getItemInMainHand() == null
				|| monster.getEquipment().getItemInMainHand().getType() == Material.AIR){
			return false;
		}
		else{
			return true;
		}
	}
	
	public void despawnAllHostiles(Chunk chunk){
		for (Entity entity : chunk.getEntities()){
			if (entity instanceof Monster
					&& ((Monster) entity).getRemoveWhenFarAway() == true
					&& isEligibleMonster((Monster) entity)){
				entity.remove();
			}
		}
	}
	
}
