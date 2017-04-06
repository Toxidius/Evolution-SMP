package Evolution.GameMechanics;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class AnvilCombining implements Listener{

	public AnvilCombining(){
	}
	
	@EventHandler
	public void onAnvilCraft(PrepareAnvilEvent e){
		if (e.getInventory().getItem(0) != null
				&& e.getInventory().getItem(0).hasItemMeta()
				&& e.getInventory().getItem(0).getItemMeta().hasDisplayName()
				&& e.getInventory().getItem(0).getItemMeta().getDisplayName().contains(ChatColor.RED.toString())){
			// the item being crafted contains a relic, so preserve it's original name and coloring
			ItemMeta meta = e.getResult().getItemMeta();
			meta.setDisplayName(e.getInventory().getItem(0).getItemMeta().getDisplayName());
			e.getResult().setItemMeta(meta);
		}
	}
	
}
