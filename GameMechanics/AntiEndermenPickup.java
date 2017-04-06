package Evolution.GameMechanics;

import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

public class AntiEndermenPickup implements Listener{

	public AntiEndermenPickup(){
	}
	
	@org.bukkit.event.EventHandler
	public void onEndermanPickupBlock(EntityChangeBlockEvent e){
		if (e.getEntity() instanceof org.bukkit.entity.Enderman){
			//Bukkit.getServer().broadcastMessage("Enderman tried to pickup block at " + e.getBlock().getX() + " " + e.getBlock().getY() + " " + e.getBlock().getZ());
			e.setCancelled(true); // disable endermen block pickup
		}
	}
	
}
