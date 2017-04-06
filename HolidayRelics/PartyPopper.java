package Evolution.HolidayRelics;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import Evolution.Main.Core;
import Evolution.Main.ParticleEffect;
import Evolution.Main.ParticleEffect.ItemData;

public class PartyPopper implements Listener{
	
	private ItemStack relic;
	
	public PartyPopper(){
		relic = new ItemStack(Material.GOLDEN_CARROT, 1);
		ItemMeta meta = relic.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Party Poppers");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.DARK_RED + "Ancient Relic!");
		lore.add(ChatColor.DARK_RED + "Lets get the party started!");
		meta.setLore(lore);
		relic.setItemMeta(meta);
		relic.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
	}
	
	public ItemStack getRelic(){
		return relic.clone();
	}
	
	@EventHandler
	public void onRightClickFireworkBomb(PlayerInteractEvent e){
		if ( (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)
				&& e.getHand() == EquipmentSlot.HAND
				&& e.getPlayer().getInventory().getItemInMainHand().getType() == Material.GOLDEN_CARROT
				&& e.getPlayer().getInventory().getItemInMainHand().hasItemMeta()
				&& e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasDisplayName()
				&& e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains(ChatColor.RED + "Party Poppers")){
			
			// confetti effect from player
			Location spawnLocation = e.getPlayer().getEyeLocation();
			Vector baseDirection = e.getPlayer().getEyeLocation().getDirection();
			Vector modifiedDirection;
			Material material = Material.WOOL;
			ItemData data;
			for (int i = 0; i < 100; i++){
				data = new ItemData(material, (byte)Core.r.nextInt(16));
				modifiedDirection = baseDirection.clone().add(new Vector(Math.random()/2.0-0.25, Math.random()/2.0, Math.random()/2.0-0.25));
				ParticleEffect.ITEM_CRACK.display(data, modifiedDirection, 0.5F, spawnLocation, 40);
			}
			
			e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.ENTITY_CHICKEN_EGG, 1.0F, 0.0F);
			e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.ENTITY_CHICKEN_EGG, 1.0F, 2.0F);	
			e.setCancelled(true);
		}
		
		// end if
	}
	
	public double round(double input){
		input = Math.round( (input*10.0) )/10.0;
		return input;
	}
}
