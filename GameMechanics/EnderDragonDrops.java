package Evolution.GameMechanics;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EnderDragon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class EnderDragonDrops implements Listener{
	
	public EnderDragonDrops(){
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent e){
		if (e.getEntity() instanceof EnderDragon){
			Location location = e.getEntity().getLocation();
			World world = location.getWorld();
			
			world.dropItem(location , new ItemStack(Material.ELYTRA, 1));
			world.dropItem(location , new ItemStack(Material.DRAGON_EGG, 1));
		}
	}

}
