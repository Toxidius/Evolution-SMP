package Evolution.Relics;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WTF implements Listener{
	
	private ItemStack relic;
	
	public WTF(){
		relic = new ItemStack(Material.COMPASS, 1);
		ItemMeta meta = relic.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "WTF");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.DARK_RED + "Ancient Relic!");
		lore.add(ChatColor.DARK_RED + "Right click a block with this compass to");
		lore.add(ChatColor.DARK_RED + "identify if it's within a slime chunk!");
		meta.setLore(lore);
		relic.setItemMeta(meta);
		relic.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		//Core.relics.add(relic);
	}
	
	public ItemStack getRelic(){
		return relic.clone();
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerRightClickSlimeChunkLocator(PlayerInteractEvent e){
		if (e.isCancelled())
			return;
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK
				&& e.getHand() == EquipmentSlot.HAND
				&& e.getPlayer().getInventory().getItemInMainHand().getType() == Material.COMPASS
				&& e.getPlayer().getInventory().getItemInMainHand().hasItemMeta()
				&& e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasDisplayName()
				&& e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.RED + "WTF")){
			e.setCancelled(true);
			
			e.getPlayer().sendBlockChange(e.getPlayer().getEyeLocation(), Material.STAINED_GLASS, (byte)2);
			
			//e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 100, false, false));
			//e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 100, false, false));
		}
	}
}
