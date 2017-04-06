package Evolution.Relics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Evolution.Main.Core;
import Evolution.Main.ParticleEffect;

public class HealingElixir implements Listener{
	
	private ItemStack relic;
	private HashMap<String, Long> cooldown;
	
	public HealingElixir(){
		relic = new ItemStack(Material.DRAGONS_BREATH, 1);
		ItemMeta meta = relic.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Healing Elixir");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.DARK_RED + "Ancient Relic!");
		lore.add(ChatColor.DARK_RED + "Right click entity to fully heal it.");
		lore.add(ChatColor.DARK_RED + "Shift-Right click entity to see it's current health.");
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
	public void onRightClickHealingElixir(PlayerInteractAtEntityEvent e){
		if ( e.getPlayer().getInventory().getItemInMainHand().getType() == Material.DRAGONS_BREATH
				&& e.getHand() == EquipmentSlot.HAND
				&& e.getPlayer().getInventory().getItemInMainHand().hasItemMeta()
				&& e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasDisplayName()
				&& e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.RED + "Healing Elixir")
				&& e.getRightClicked() instanceof LivingEntity){
			
			e.setCancelled(true); // prevent regular entity interaction -- ex: villager trading
			e.getPlayer().closeInventory();
			
			if ( !(e.getRightClicked() instanceof LivingEntity) ){
				return; // clicked non-living entity
			}
			
			LivingEntity entity = (LivingEntity) e.getRightClicked();
			if (e.getPlayer().isSneaking() == true){
				e.getPlayer().sendMessage(ChatColor.GRAY + entity.getType().name() + "'s health: " + round(entity.getHealth()) + "/" + round(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()) );
			}
			else{
				if (canUse(e.getPlayer())){
					double maxHealth = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
					entity.setHealth(maxHealth);
					
					Location temp;
					for (double y = 0; y <= 2.0; y+=0.1){
						temp = entity.getLocation().add(Math.random()-0.5, y, Math.random()-0.5);
						//ParticleEffect.ENDROD.display(0F, 0F, 0F, 0.01F, 1, temp, 40.0);
						ParticleEffect.DRAGONBREATH.display(0F, 0F, 0F, 0.01F, 1, temp, 40.0);
						ParticleEffect.DRAGONBREATH.display(0F, 0F, 0F, 0.01F, 1, temp, 40.0);
						ParticleEffect.DRAGONBREATH.display(0F, 0F, 0F, 0.01F, 1, temp, 40.0);
					}
				}
			}
		}
	}
	
	public boolean canUse(Player player){
		// check cooldown
		long cooldownTimeInMinutes = 1;
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
