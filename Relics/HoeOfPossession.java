package Evolution.Relics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Evolution.Main.Core;

public class HoeOfPossession implements Listener{
	
	private ItemStack relic;
	public HashSet<Entity> possessedEntities;
	
	public HoeOfPossession(){
		relic = new ItemStack(Material.DIAMOND_HOE, 1);
		ItemMeta meta = relic.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Hoe of Possession");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.DARK_RED + "Ancient Relic!");
		lore.add(ChatColor.DARK_RED + "Just some random hoe I found.");
		meta.setLore(lore);
		relic.setItemMeta(meta);
		
		Core.relics.add(relic);
		possessedEntities = new HashSet<Entity>();
	}
	
	public ItemStack getRelic(){
		return relic.clone();
	}
	
	@EventHandler
	public void onRightClickHoeOfPosession(PlayerInteractAtEntityEvent e){
		if ( e.getPlayer().getInventory().getItemInMainHand().getType() == Material.DIAMOND_HOE
				&& e.getPlayer().getInventory().getItemInMainHand().hasItemMeta()
				&& e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasDisplayName()
				&& e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.RED + "Hoe of Possession")
				&& e.getRightClicked() instanceof LivingEntity){
			
			e.setCancelled(true); // prevent regular entity interaction -- ex: villager trading
			if (possessedEntities.contains(e.getRightClicked())){
				return; // already contains the entity, so don't do anything
			}
			@SuppressWarnings("unused")
			HoeOfPossessionWatcher watcher = new HoeOfPossessionWatcher(e.getPlayer(), e.getRightClicked());
		}
		
		// end if
	}
	
	@EventHandler
	public void onEntityDamage_HoeTool(EntityDamageEvent e){
		if (possessedEntities != null 
				&& possessedEntities.size() > 0 
				&& possessedEntities.contains(e.getEntity())){
			e.setCancelled(true); // prevent possessed entities from taking damage
		}
	}
}
