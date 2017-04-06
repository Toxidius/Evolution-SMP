package Evolution.GameMechanics;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

public class PigmenNoDamageSwipeAttack implements Listener{

	public PigmenNoDamageSwipeAttack(){
	}
	
	@EventHandler
	public void onPigmanDamageBySwipeAttack(EntityDamageByEntityEvent e){
		if (e.getEntity() instanceof PigZombie
				&& e.getCause() == DamageCause.ENTITY_ATTACK
				&& e.getDamage() == 1.0
				&& e.getDamager() instanceof Player){
			if (playerUsingSword((Player)e.getDamager()) == true){
				// this pigman was damaged by swipe attack
				e.setCancelled(true); // prevent damage
				
				// spawn in a new pigman at this location
				// do this becasue apparently we cannot prevent pigmen getting angered even if the damage event is canceled
				PigZombie originalPigman = (PigZombie) e.getEntity();
				PigZombie pigman = (PigZombie) e.getEntity().getWorld().spawnEntity(e.getEntity().getLocation(), EntityType.PIG_ZOMBIE);
				pigman.teleport(originalPigman.getLocation()); // set pitch/yaw
				pigman.getEquipment().setArmorContents(originalPigman.getEquipment().getArmorContents());
				pigman.getEquipment().setItemInMainHand(originalPigman.getEquipment().getItemInMainHand());
				originalPigman.remove(); // remove original pigman
				
				/*
				// set not angry and null target 1 tick later
				Bukkit.getScheduler().scheduleSyncDelayedTask(Core.thisPlugin, new Runnable(){
					@Override
					public void run() {
						((PigZombie)e.getEntity()).setAngry(false);
						((PigZombie)e.getEntity()).setAnger(0);
						((PigZombie)e.getEntity()).setTarget(null);
					}
				}, 3);
				*/
			}
		}
	}
	
	public boolean playerUsingSword(Player player){
		ItemStack handItem = player.getInventory().getItemInMainHand();
		if (handItem == null){
			return false;
		}
		else if (handItem.getType() == Material.DIAMOND_SWORD
				|| handItem.getType() == Material.IRON_SWORD
				|| handItem.getType() == Material.STONE_SWORD
				|| handItem.getType() == Material.GOLD_SWORD
				|| handItem.getType() == Material.WOOD_SWORD){
			return true;
		}
		return false;
	}
}
