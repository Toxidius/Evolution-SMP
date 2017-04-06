package Evolution.Bosses;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Evolution.Main.Core;

public class GroundBurnEffect implements Runnable, Listener{

	private Monster monster;
	private int id;
	private int calls;
	private ArrayList<Location> fireLocations;
	boolean fireActive;
	
	public GroundBurnEffect(Monster monster){
		this.monster = monster;
		fireActive = false;
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 2L, 2L);
	}
	
	@EventHandler
	public void onItemBurnByBurnEffect(EntityDamageEvent e){
		if (e.getEntity() instanceof Item
				&& (e.getCause() == DamageCause.FIRE || e.getCause() == DamageCause.FIRE_TICK)){
			Location location = e.getEntity().getLocation().getBlock().getLocation();
			
			if (fireLocations != null
					&& fireLocations.isEmpty() == false
					&& fireLocations.contains(location) == true){
				e.setCancelled(true); // prevent burning
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		if (monster.isDead() && fireActive == false){
			Bukkit.getScheduler().cancelTask(id); // end task
			return;
		}
		if (monster.isDead() && fireActive == true){
			// remove fire
			for (Location location : fireLocations){
				location.getBlock().setType(Material.AIR);
			}
			fireActive = false;
			
			Bukkit.getScheduler().cancelTask(id); // end task
			return;
		}
		if (monster.isOnGround()){
			if (calls < 40){
				// lava particle "warning"
				for (int i = 0; i < 2; i++){
					monster.getWorld().playEffect(monster.getLocation().add(0.0, 1.0, 0.0), Effect.LAVA_POP, 50, 50);
				}
			}
			else if (calls == 41){
				// create fire
				monster.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 200, 1));
				fireLocations = new ArrayList<Location>();
				Location center = monster.getLocation();
				Location temp;
				int radius = 5;
				int startX = center.getBlockX()-radius;
				int startY = center.getBlockY();
				int startZ = center.getBlockZ()-radius;
				int endX = center.getBlockX()+radius;
				int endY = center.getBlockY();
				int endZ = center.getBlockZ()+radius;
				for (int y = startY; y <= endY; y++){
					for (int x = startX; x <= endX; x++){
						for (int z = startZ; z <= endZ; z++){
							temp = new Location(center.getWorld(), x, y, z);
							if (temp.distance(center) <= radius && (temp.getBlock().getType() == Material.AIR || temp.getBlock().getType() == Material.LONG_GRASS)){
								fireLocations.add(temp);
								temp.getBlock().setType(Material.FIRE);
							}
						}
					}
				}
				fireActive = true;
			}
			else if (calls == 100){
				// remove fire
				for (Location location : fireLocations){
					location.getBlock().setType(Material.AIR);
				}
				fireActive = false;
				
				Bukkit.getScheduler().cancelTask(id); // end task
				return;
			}
			calls++;
		}
	}
}
