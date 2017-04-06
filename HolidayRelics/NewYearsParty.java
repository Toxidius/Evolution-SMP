package Evolution.HolidayRelics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class NewYearsParty implements Listener{
	
	private ItemStack relic;
	private HashMap<String, NewYearsPartyEffect> activePlayers;
	
	public NewYearsParty(){
		relic = new ItemStack(Material.CARROT_ITEM, 1);
		ItemMeta meta = relic.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "New Years Party");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.DARK_RED + "Ancient Relic!");
		lore.add(ChatColor.DARK_RED + "Lets get the party started!");
		meta.setLore(lore);
		relic.setItemMeta(meta);
		relic.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		
		activePlayers = new HashMap<>();
	}
	
	public ItemStack getRelic(){
		return relic.clone();
	}
	
	@EventHandler
	public void onRightClickFireworkBomb(PlayerInteractEvent e){
		if ( (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)
				&& e.getHand() == EquipmentSlot.HAND
				&& e.getPlayer().getInventory().getItemInMainHand().getType() == Material.CARROT_ITEM
				&& e.getPlayer().getInventory().getItemInMainHand().hasItemMeta()
				&& e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasDisplayName()
				&& e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains(ChatColor.RED + "New Years Party")){
			e.setCancelled(true);
			
			// check if player has a runnable effect going
			if (activePlayers.containsKey(e.getPlayer().getName()) == true){
				// end effect
				NewYearsPartyEffect effect = activePlayers.get(e.getPlayer().getName());
				effect.end();
				activePlayers.remove(e.getPlayer().getName());
			}
			else{
				// start new effect
				NewYearsPartyEffect effect = new NewYearsPartyEffect(e.getPlayer());
				activePlayers.put(e.getPlayer().getName(), effect);
			}
		}
		
		// end if
	}
	
	public double round(double input){
		input = Math.round( (input*10.0) )/10.0;
		return input;
	}
}
