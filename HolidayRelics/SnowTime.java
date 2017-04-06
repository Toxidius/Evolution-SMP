package Evolution.HolidayRelics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SnowTime implements Listener{

	private ItemStack relic;
	private HashMap<Player, SnowTimeEffect> activePlayers;
	
	public SnowTime(){
		relic = new ItemStack(Material.INK_SACK, 1,(short)15);
		ItemMeta meta = relic.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Snowtime Particles");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.DARK_RED + "Ancient Relic!");
		lore.add(ChatColor.DARK_RED + "Ooo Purdy!");
		meta.setLore(lore);
		relic.setItemMeta(meta);
		relic.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		
		activePlayers = new HashMap<>();
	}
	
	public ItemStack getRelic(){
		return relic.clone();
	}
	
	@EventHandler
	public void onRightClickSnowTimeParticles(PlayerInteractEvent e){
		if ( (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)
				&& e.getHand() == EquipmentSlot.HAND
				&& e.getPlayer().getInventory().getItemInMainHand().getType() == Material.INK_SACK
				&& e.getPlayer().getInventory().getItemInMainHand().hasItemMeta()
				&& e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasDisplayName()
				&& e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains(ChatColor.RED + "Snowtime Particles")){
			
			e.setCancelled(true);
			
			if (activePlayers.containsKey(e.getPlayer())){
				activePlayers.get(e.getPlayer()).end();
				activePlayers.remove(e.getPlayer());
			}
			else{
				// snow time particles
				SnowTimeEffect effect = new SnowTimeEffect(e.getPlayer());
				activePlayers.put(e.getPlayer(), effect);
			}
		}
	}
}