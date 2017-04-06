package Evolution.Relics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Evolution.Main.Core;

public class SavingGraceBed implements Listener{
	
	private ItemStack relic;
	private HashMap<String, Long> cooldown;
	
	public SavingGraceBed(){
		relic = new ItemStack(Material.BED, 1);
		ItemMeta meta = relic.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Saving Grace Bed");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.DARK_RED + "Ancient Relic!");
		lore.add(ChatColor.DARK_RED + "In times of danger, right clicking this bed");
		lore.add(ChatColor.DARK_RED + "will teleport you back to the bed you last");
		lore.add(ChatColor.DARK_RED + "slept in.");
		lore.add("");
		lore.add(ChatColor.WHITE + "30 Minute Cooldown");
		meta.setLore(lore);
		relic.setItemMeta(meta);
		
		Core.relics.add(relic);
		cooldown = new HashMap<String, Long>();
	}
	
	public ItemStack getRelic(){
		return relic.clone();
	}
	
	@EventHandler
	public void onRightClickSavingGraceBed(PlayerInteractEvent e){
		if ( (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) 
				&& e.getHand() == EquipmentSlot.HAND
				&& e.getPlayer().getInventory().getItemInMainHand().getType() == Material.BED
				&& e.getPlayer().getInventory().getItemInMainHand().hasItemMeta()
				&& e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasDisplayName()
				&& e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.RED + "Saving Grace Bed") ){
			
			e.setCancelled(true); // prevent placing bed if applicable
			
			// check cooldown
			if (cooldown.containsKey(e.getPlayer().getUniqueId().toString())){
				// check if time has passed
				long currentTime = System.currentTimeMillis();
				long cooldownEnd = cooldown.get(e.getPlayer().getUniqueId().toString()).longValue();
				if (currentTime >= cooldownEnd){
					teleportPlayer(e.getPlayer());
					// update the next cooldown time
					cooldownEnd = System.currentTimeMillis() + 1000*60*30; // 30 minutes
					cooldown.remove(e.getPlayer().getUniqueId().toString());
					cooldown.put(e.getPlayer().getUniqueId().toString(), cooldownEnd);
				}
				else{
					// send remaining cooldown time
					long remainingTime = cooldownEnd - currentTime;
					double remainingMinutes = round((remainingTime) / 1000.0 / 60.0);
					e.getPlayer().sendMessage(ChatColor.RED + "You can use this item again in " + ChatColor.WHITE + remainingMinutes + ChatColor.RED + " minutes.");
				}
			}
			else{
				teleportPlayer(e.getPlayer());
				
				// put next cooldown time
				long cooldownEnd = System.currentTimeMillis() + 1000*60*30; // 30 minutes
				cooldown.put(e.getPlayer().getUniqueId().toString(), cooldownEnd);
			}
		}
		
		// end if
	}
	
	public double round(double input){
		input = Math.round( (input*10.0) )/10.0;
		return input;
	}
	
	public void teleportPlayer(Player player){
		// teleport player to safe location
		if (player.getBedSpawnLocation() != null){
			player.teleport(player.getBedSpawnLocation()); // teleport to bed location
		}
		else{
			player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation()); // teleport to spawn
		}
	}
}
