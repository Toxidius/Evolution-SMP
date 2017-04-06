package Evolution.Bosses;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import Evolution.Main.Core;

public class BossDrops implements Listener{

	public BossDrops(){
	}
	
	@EventHandler
	public void onBossDeath(EntityDeathEvent e){
		if (e.getEntity() instanceof LivingEntity
				&& e.getEntity().getCustomName() != null
				&& e.getEntity().getCustomName().contains(ChatColor.RED.toString())
				&& e.getEntity().getCustomName().contains("Boss")){
			e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 1000F, 0F);
			e.setDroppedExp(200);
			e.getDrops().clear();
			int index = Core.r.nextInt( Core.relics.size() );
			e.getDrops().add(Core.relics.get(index).clone());
			
			// remove any nearby fire where the relic will drop
			Location center = e.getEntity().getLocation();
			Location temp;
			int radius = 3;
			int startX = center.getBlockX()-radius;
			int startY = center.getBlockY()-3;
			int startZ = center.getBlockZ()-radius;
			int endX = center.getBlockX()+radius;
			int endY = center.getBlockY()+3;
			int endZ = center.getBlockZ()+radius;
			for (int y = startY; y <= endY; y++){
				for (int x = startX; x <= endX; x++){
					for (int z = startZ; z <= endZ; z++){
						temp = new Location(center.getWorld(), x, y, z);
						if (temp.getBlock().getType() == Material.FIRE){
							temp.getBlock().setType(Material.AIR);
						}
					}
				}
			}
		}
	}
}
