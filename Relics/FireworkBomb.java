package Evolution.Relics;

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

import Evolution.Main.Core;

public class FireworkBomb implements Listener{
	
	private ItemStack relic;
	private HashMap<String, Long> cooldown;
	
	public FireworkBomb(){
		relic = new ItemStack(Material.SULPHUR, 1);
		ItemMeta meta = relic.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Firework Bomb");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.DARK_RED + "Ancient Relic!");
		lore.add(ChatColor.DARK_RED + "Boom Boom! Inspired by Nakita-Makita");
		lore.add("");
		lore.add(ChatColor.WHITE + "5 Minute Cooldown");
		meta.setLore(lore);
		relic.setItemMeta(meta);
		relic.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		
		Core.relics.add(relic);
		cooldown = new HashMap<String, Long>();
	}
	
	public ItemStack getRelic(){
		return relic.clone();
	}
	
	@EventHandler
	public void onRightClickFireworkBomb(PlayerInteractEvent e){
		if ( (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)
				&& e.getHand() == EquipmentSlot.HAND
				&& e.getPlayer().getInventory().getItemInMainHand().getType() == Material.SULPHUR
				&& e.getPlayer().getInventory().getItemInMainHand().hasItemMeta()
				&& e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasDisplayName()
				&& e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains(ChatColor.RED + "Firework Bomb")){
			
			// start a firework bomb effect at the players location
			if (canUse(e.getPlayer())){
				@SuppressWarnings("unused")
				FireworkBombEffect effect = new FireworkBombEffect(e.getPlayer().getLocation());
			}
			e.setCancelled(true);
		}
		
		// end if
	}
	
	public boolean canUse(Player player){
		// check cooldown
		long cooldownTimeInMinutes = 5;
		if (cooldown.containsKey(player.getUniqueId().toString())){
			// check if time has passed
			long currentTime = System.currentTimeMillis();
			long cooldownEnd = cooldown.get(player.getUniqueId().toString()).longValue();
			if (currentTime >= cooldownEnd){
				// time has passed
				// update the next cooldown time
				cooldownEnd = System.currentTimeMillis() + 1000*60*cooldownTimeInMinutes;
				cooldown.remove(player.getUniqueId().toString());
				cooldown.put(player.getUniqueId().toString(), cooldownEnd);
				return true;
			}
			else{
				// time has not passed
				// send remaining cooldown time
				long remainingTime = cooldownEnd - currentTime;
				double remainingMinutes = round((remainingTime) / 1000.0 / 60.0);
				player.sendMessage(ChatColor.RED + "You can use this item again in " + ChatColor.WHITE + remainingMinutes + ChatColor.RED + " minutes.");
				return false;
			}
		}
		else{
			// cooldown hashmap doesn't contain the player
			// put next cooldown time
			long cooldownEnd = System.currentTimeMillis() + 1000*60*cooldownTimeInMinutes;
			cooldown.put(player.getUniqueId().toString(), cooldownEnd);
			return true;
		}
	}
	
	public double round(double input){
		input = Math.round( (input*10.0) )/10.0;
		return input;
	}
}
