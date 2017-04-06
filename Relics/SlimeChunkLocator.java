package Evolution.Relics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Evolution.Main.Core;

public class SlimeChunkLocator implements Listener{
	
	private ItemStack relic;
	
	public SlimeChunkLocator(){
		relic = new ItemStack(Material.COMPASS, 1);
		ItemMeta meta = relic.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Slime Chunk Locator");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.DARK_RED + "Ancient Relic!");
		lore.add(ChatColor.DARK_RED + "Right click a block with this compass to");
		lore.add(ChatColor.DARK_RED + "identify if it's within a slime chunk!");
		meta.setLore(lore);
		relic.setItemMeta(meta);
		relic.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		Core.relics.add(relic);
	}
	
	public ItemStack getRelic(){
		return relic.clone();
	}
	
	@EventHandler
	public void onPlayerRightClickSlimeChunkLocator(PlayerInteractEvent e){
		if (e.isCancelled())
			return;
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK
				&& e.getHand() == EquipmentSlot.HAND
				&& e.getPlayer().getInventory().getItemInMainHand().getType() == Material.COMPASS
				&& e.getPlayer().getInventory().getItemInMainHand().hasItemMeta()
				&& e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasDisplayName()
				&& e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.RED + "Slime Chunk Locator")
				&& e.getPlayer().getWorld().equals(Bukkit.getWorlds().get(0)) == true){
			e.setCancelled(true);
			
			long worldSeed = e.getPlayer().getWorld().getSeed();
			Chunk chunk = e.getPlayer().getWorld().getChunkAt(e.getClickedBlock());
			int xChunk = chunk.getX();
			int zChunk = chunk.getZ();

			Random random = new Random(worldSeed + 
					xChunk * xChunk * 4987142 + 
					xChunk * 5947611 + 
					zChunk * zChunk * 4392871L + 
					zChunk * 389711 ^ 0x3AD8025F);
			if (random.nextInt(10) == 0) {
				e.getPlayer().sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Slime chunk!");
			} else {
				e.getPlayer().sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "No slime chunk :(");
			}
		}
	}
}
