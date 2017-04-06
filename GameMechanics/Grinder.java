package Evolution.GameMechanics;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Evolution.Main.Core;

public class Grinder implements Listener{

	private ItemStack item;
	
	public Grinder(){
		item = new ItemStack(Material.DISPENSER, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Grinder");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.DARK_RED + "Hurts mobs when supplied with a");
		lore.add(ChatColor.DARK_RED + "sword and a redstone current.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		Core.items.add(item);
	}
	
	public ItemStack getItem(){
		return item.clone();
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDispenserTrigger(BlockDispenseEvent e){
		if (e.getBlock().getType() == Material.DISPENSER){
			Dispenser dispenser = (Dispenser) e.getBlock().getState();
			Inventory inv = dispenser.getInventory();
			if (inv.getName() != null && inv.getName().equals(ChatColor.RED + "Grinder")){
				Material type = e.getItem().getType();
				if (type == Material.DIAMOND_SWORD || type == Material.IRON_SWORD || type == Material.STONE_SWORD || type == Material.WOOD_SWORD){
					e.setCancelled(true); // prevent item dispense
					
					Block block = e.getBlock();
					byte data = block.getData();
					BlockFace facing;
					if (data >= 8){
						data = (byte) (data - 8);
					}
					
					if (data == (byte)1){
						facing = BlockFace.UP;
					}
					else if (data == (byte)2){
						facing = BlockFace.NORTH;
					}
					else if (data == (byte)3){
						facing = BlockFace.SOUTH;
					}
					else if (data == (byte)4){
						facing = BlockFace.WEST;
					}
					else if (data == (byte)5){
						facing = BlockFace.EAST;
					}
					else if (data == (byte)0){
						facing = BlockFace.DOWN;
					}
					else{
						facing = BlockFace.NORTH;
					}
					
					float damage;
					if (type == Material.DIAMOND_SWORD){
						damage = 7F;
					}
					else if (type == Material.IRON_SWORD){
						damage = 6F;
					}
					else if (type == Material.STONE_SWORD){
						damage = 5F;
					}
					else if (type == Material.WOOD_SWORD){
						damage = 4F;
					}
					else{
						damage = 0F;
					}
					
					
					// get the block in front of the grinder
					block = block.getRelative(facing);
					World world = block.getWorld();
					for (Entity entity : world.getNearbyEntities(block.getLocation().add(0.5, 0, 0.5), 0.25, 1.0, 0.25)){
						if (entity instanceof LivingEntity){
							((LivingEntity) entity).damage(damage);
						}
					}
					block.getWorld().playEffect(block.getLocation(), Effect.MOBSPAWNER_FLAMES, 10); // effect
					block.getWorld().playEffect(block.getLocation(), Effect.MOBSPAWNER_FLAMES, 10); // effect
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e){
		if (e.getBlock().getType() == Material.DISPENSER){
			Dispenser dispenser = (Dispenser) e.getBlock().getState();
			if (dispenser.getInventory().getName() != null && dispenser.getInventory().getName().equals(ChatColor.RED + "Grinder")){
				e.getBlock().breakNaturally(new ItemStack(Material.AIR)); // remove
				e.getBlock().getWorld().dropItem(e.getBlock().getLocation().add(0.5, 0.5, 0.5), getItem());
			}
		}
	}
}
