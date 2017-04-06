package Evolution.GameMechanics;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class FancySounds implements Listener{

	public FancySounds(){
	}
	
	@EventHandler
	public void onPlayerDamageMelee(EntityDamageByEntityEvent e){
		if (e.getEntity() instanceof Player
				&& e.getCause() == DamageCause.ENTITY_ATTACK){
			e.getEntity().getLocation().getWorld().playSound(e.getEntity().getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.25F, (float)Math.random()*2); // 0 to 2 pitch 
		}
	}
	
	public boolean hasArmor(Player player){
		if (player.getInventory().getHelmet() != null
				&& player.getInventory().getHelmet().getType() != Material.AIR){
			return true;
		}
		else if (player.getInventory().getChestplate() != null
				&& player.getInventory().getChestplate().getType() != Material.AIR){
			return true;
		}
		else if (player.getInventory().getLeggings() != null
				&& player.getInventory().getLeggings().getType() != Material.AIR){
			return true;
		}
		else if (player.getInventory().getBoots() != null
				&& player.getInventory().getBoots().getType() != Material.AIR){
			return true;
		}
		else{
			return false;
		}
	}
}
