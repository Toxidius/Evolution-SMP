package Evolution.Relics;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Evolution.Main.Core;

public class SwordOfExperience implements Listener{
	
	private ItemStack relic;
	
	public SwordOfExperience(){
		relic = new ItemStack(Material.GOLD_SWORD, 1);
		ItemMeta meta = relic.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Sword of Experience");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.DARK_RED + "Ancient Relic!");
		lore.add(ChatColor.DARK_RED + "Crafted by an ancient craftsman.");
		lore.add(ChatColor.DARK_RED + "2x exp from mob kills");
		meta.setLore(lore);
		relic.setItemMeta(meta);
		
		Core.relics.add(relic);
	}
	
	public ItemStack getRelic(){
		return relic.clone();
	}
	
	@EventHandler
	public void onMobKillWithSword(EntityDeathEvent e){
		if (e.getEntity().getKiller() != null){
			// this entity is in the process of being killed given they arn't dead yet
			// check if they were killed using a beheading axe
			Player player = (Player) e.getEntity().getKiller();
			ItemStack itemInHand = player.getInventory().getItemInMainHand();
			if (itemInHand != null
					&& itemInHand.hasItemMeta()
					&& itemInHand.getItemMeta().hasDisplayName()
					&& itemInHand.getItemMeta().getDisplayName().equals(ChatColor.RED + "Sword of Experience")){
				// this entity was killed using the sword
				e.setDroppedExp(e.getDroppedExp()*2);
			}
			
		}
		// end if
	}

}
