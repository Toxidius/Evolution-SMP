package Evolution.GameMechanics;

import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class RelicDestroyByExplosionFix implements Listener{

	public RelicDestroyByExplosionFix(){
	}
	
	@EventHandler
	public void onRelicDestory(EntityDamageEvent e){
		if (e.getEntity() instanceof Item
				&& (e.getCause() == DamageCause.BLOCK_EXPLOSION || e.getCause() == DamageCause.ENTITY_EXPLOSION)){
			Item item = (Item) e.getEntity();
			if (item.getItemStack().getItemMeta() != null
					&& item.getItemStack().getItemMeta().getDisplayName() != null
					&& item.getItemStack().getItemMeta().getDisplayName().contains(ChatColor.RED.toString())){
				e.setCancelled(true); // prevent relics from getting damaged by explosions
			}
		}
	}
}
