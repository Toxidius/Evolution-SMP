package Evolution.Relics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Evolution.Main.Core;

public class LamboCart9000 implements Listener{
	
	private ItemStack relic;
	private HashMap<String, Long> cooldown;
	
	public LamboCart9000(){
		relic = new ItemStack(Material.MINECART, 1);
		ItemMeta meta = relic.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "LamboCart 9000");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.DARK_RED + "Ancient Relic!");
		lore.add(ChatColor.DARK_RED + "Drive around in style!");
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
	public void onPlayerRightClickMorphingPowder(PlayerInteractEvent e){
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK
				&& e.getHand() == EquipmentSlot.HAND
				&& e.getPlayer().getInventory().getItemInMainHand().getType() == Material.MINECART
				&& e.getPlayer().getInventory().getItemInMainHand().hasItemMeta()
				&& e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasDisplayName()
				&& e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.RED + "LamboCart 9000")){
			e.setCancelled(true);
			
			if (canUse(e.getPlayer())){
				Minecart minecart = (Minecart) e.getPlayer().getWorld().spawnEntity(e.getClickedBlock().getLocation().add(0.5, 1.5, 0.5), EntityType.MINECART);
				minecart.setCustomName("LamboCart9000");
				minecart.setCustomNameVisible(true);
				minecart.setMaxSpeed(2.0);
				minecart.setSilent(true);
				
				@SuppressWarnings("unused")
				LamboCart9000Watcher watcher = new LamboCart9000Watcher(minecart);
			}
		}
	}
	
	@EventHandler
	public void onLamboCartDestory(VehicleDestroyEvent e){
		if (e.getVehicle() instanceof Minecart
				&& ((Minecart)e.getVehicle()).getCustomName() != null
				&& ((Minecart)e.getVehicle()).getCustomName().equals("LamboCart9000")){
			e.setCancelled(true);
			e.getVehicle().remove(); // remove with no minecart drop
		}
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
