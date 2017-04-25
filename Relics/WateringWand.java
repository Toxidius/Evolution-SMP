package Evolution.Relics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Evolution.Main.Core;

public class WateringWand implements Listener{
	
	private ItemStack relic;
	public HashSet<Entity> possessedEntities;
	
	public WateringWand(){
		relic = new ItemStack(Material.STICK, 1);
		ItemMeta meta = relic.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Watering Wand");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.DARK_RED + "Ancient Relic!");
		lore.add(ChatColor.DARK_RED + "Created by a farmer who became a wizard.");
		meta.setLore(lore);
		relic.setItemMeta(meta);
		relic.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		
		Core.relics.add(relic);
		possessedEntities = new HashSet<Entity>();
	}
	
	public ItemStack getRelic(){
		return relic.clone();
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onRightClickWateringWand(PlayerInteractEvent e){
		if ( (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)
				&& e.getHand() == EquipmentSlot.HAND
				&& e.getPlayer().getInventory().getItemInMainHand().getType() == Material.STICK
				&& e.getPlayer().getInventory().getItemInMainHand().hasItemMeta()
				&& e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasDisplayName()
				&& e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains(ChatColor.RED + "Watering Wand")){
			
			// loop through 3x3 blocks around the player and bonemeal random ones
			Location center = e.getPlayer().getLocation();
			int xFrom = center.getBlockX()-2;
			int xTo = center.getBlockX()+2;
			int yFrom = center.getBlockY();
			int yTo = center.getBlockY()+1;
			int zFrom = center.getBlockZ()-2;
			int zTo = center.getBlockZ()+2;
			
			Block temp;
			Material type;
			int numGrown = 0;
			for (int y = yFrom; y <= yTo; y++){
				for (int x = xFrom; x <= xTo; x++){
					for (int z = zFrom; z <= zTo; z++){
						temp = center.getWorld().getBlockAt(x, y, z);
						type = temp.getType();
						if (type == Material.CROPS || type == Material.POTATO || type == Material.CARROT || type == Material.PUMPKIN_STEM || type == Material.MELON_STEM){
							if (temp.getData() < (byte)7 && Math.random() < 0.10){
								// bonemeal this crop
								temp.setData((byte) (temp.getData()+1));
								temp.getLocation().getWorld().playEffect(temp.getLocation(), Effect.VILLAGER_PLANT_GROW, 10);
								numGrown++;
							}
						}
						else if (type == Material.NETHER_WARTS){
							if (temp.getData() < (byte)3 && Math.random() < 0.10){
								// bonemeal this crop
								temp.setData((byte) (temp.getData()+1));
								temp.getLocation().getWorld().playEffect(temp.getLocation(), Effect.VILLAGER_PLANT_GROW, 10);
								numGrown++;
							}
						}
						else if (type == Material.BEETROOT_BLOCK){
							if (temp.getData() < (byte)3 && Math.random() < 0.10){
								// bonemeal this crop
								temp.setData((byte) (temp.getData()+1));
								temp.getLocation().getWorld().playEffect(temp.getLocation(), Effect.VILLAGER_PLANT_GROW, 10);
								numGrown++;
							}
						}
					}
				}
			}
			
			// update grown crop count
			long currentGrown;
			String itemName = e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName();
			if (itemName.contains("-")){
				// get the current amount this wand has grown from the display name
				itemName = itemName.split("-")[1];
				itemName = itemName.split(" ")[1];
				currentGrown = Long.valueOf(itemName);
			}
			else{
				// never grown any crops yet
				currentGrown = 0;
			}
			
			long newAmount = currentGrown + numGrown;
			
			ItemMeta meta = e.getPlayer().getInventory().getItemInMainHand().getItemMeta();
			meta.setDisplayName(ChatColor.RED + "Watering Wand - " + newAmount + " Crops Grown");
			e.getPlayer().getInventory().getItemInMainHand().setItemMeta(meta);
			
		}
		
		// end if
	}
}
