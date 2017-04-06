package Evolution.Bosses;

import java.util.HashSet;

import org.bukkit.ChatColor;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PolarBear;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

public class BossLoader implements Listener{
	
	public HashSet<String> loadedBosses;
	
	public BossLoader(){
		loadedBosses = new HashSet<String>();
	}
	
	public void addBoss(Entity entity){
		// add a new boss to the loaded bosses and start it's runnable task
		loadedBosses.add(entity.getUniqueId().toString());
	}
	
	public void removeBoss(Entity entity){
		// unload the boss and end it's runnable task
		loadedBosses.remove(entity.getUniqueId().toString());
	}
	
	@SuppressWarnings("unused")
	@EventHandler
	public void onEntityTarget(EntityTargetEvent e){
		if (e.getEntity() instanceof LivingEntity 
				&& e.getEntity().getCustomName() != null 
				&& e.getEntity().getCustomName().contains(ChatColor.RED.toString()) 
				&& e.getEntity().getCustomName().contains("Boss")
				&& loadedBosses.contains(e.getEntity().getUniqueId().toString()) == false){
			Entity entity = e.getEntity();
			//Bukkit.getServer().broadcastMessage("loading the boss " + entity.getCustomName());
			if (entity.getType() == EntityType.ZOMBIE){
				// start a zombie boss watcher
				Zombie zombie = (Zombie) entity;
				ZombieBossWatcher watcher = new ZombieBossWatcher(zombie);
				addBoss(entity); // add entity to loadedBosses
			}
			else if (entity.getType() == EntityType.SPIDER){
				Spider spider = (Spider) entity;
				SpiderBossWatcher watcher = new SpiderBossWatcher(spider);
				addBoss(entity); // add entity to loadedBosses
			}
			else if (entity.getType() == EntityType.POLAR_BEAR){
				PolarBear bear = (PolarBear) entity;
				PolarBearWatcher watcher = new PolarBearWatcher(bear);
				addBoss(entity); // add entity to loadedBosses
			}
			else if (entity.getType() == EntityType.WITHER_SKELETON){
				// start a pumpkin boss watcher
				Skeleton skeleton = (Skeleton) entity;
				PumpkinBossWatcher watcher = new PumpkinBossWatcher(skeleton);
				addBoss(entity); // add entity to loadedBosses
			}
			else if (entity.getType() == EntityType.CREEPER){
				// start a creeper boss watcher
				Creeper creeper = (Creeper) entity;
				CreeperBossWatcher watcher = new CreeperBossWatcher(creeper);
				addBoss(entity); // add entity to loadedBosses
			}
		}
	}
	
	/*
	@EventHandler
	public void onChunkLoad(ChunkLoadEvent e){
		Chunk chunk = e.getChunk();
		
		Entity[] entities = chunk.getEntities();
		
		for (Entity entity : entities) {
			if (entity.getCustomName() != null){
				if (entity.getCustomName().contains(ChatColor.RED.toString()) && entity.getCustomName().contains("Boss")){
					if (loadedBosses.contains(entity) == false){
						Bukkit.getServer().broadcastMessage("loading the boss " + entity.getCustomName());
					}
				}
			}
	    }
	}
	*/
	
	/*
	@EventHandler
	public void onChunkUnload(ChunkUnloadEvent e){
		Chunk chunk = e.getChunk();
		
		Entity[] entities = chunk.getEntities();
		
		for (Entity entity : entities) {
			if (entity.getCustomName() != null){
				if (entity.getCustomName().contains(ChatColor.RED.toString()) && entity.getCustomName().contains("Boss")){
					if (loadedBosses.contains(entity) == true){
						// loadedBosses.add(entity);
					}
				}
			}
	    }
	}
	*/
}
