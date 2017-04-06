package Evolution.Relics;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Evolution.Main.Core;

public class SpeedyMcGeePickaxe {
	
	private ItemStack relic;
	
	public SpeedyMcGeePickaxe(){
		relic = new ItemStack(Material.DIAMOND_PICKAXE, 1);
		ItemMeta meta = relic.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Speedy McGee Pickaxe");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.DARK_RED + "Ancient Relic!");
		meta.setLore(lore);
		relic.setItemMeta(meta);
		relic.addUnsafeEnchantment(Enchantment.DIG_SPEED, 6);
		
		Core.relics.add(relic);
	}
	
	public ItemStack getRelic(){
		return relic.clone();
	}

}
