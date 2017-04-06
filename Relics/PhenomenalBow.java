package Evolution.Relics;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Evolution.Main.Core;

public class PhenomenalBow {
	
	private ItemStack relic;
	
	public PhenomenalBow(){
		relic = new ItemStack(Material.BOW, 1);
		ItemMeta meta = relic.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Phenominal Bow");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.DARK_RED + "Ancient Relic!");
		meta.setLore(lore);
		relic.setItemMeta(meta);
		relic.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
		relic.addUnsafeEnchantment(Enchantment.MENDING, 1);
		relic.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 5);
		
		Core.relics.add(relic);
	}
	
	public ItemStack getRelic(){
		return relic.clone();
	}

}
