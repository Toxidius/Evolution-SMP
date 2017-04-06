package Evolution.GameMechanics;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Evolution.Main.Core;

public class BlockBreaker implements Listener{

	private ItemStack item;
	
	public BlockBreaker(){
		item = new ItemStack(Material.DISPENSER, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Block Breaker");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.DARK_RED + "Breaks blocks when supplied with");
		lore.add(ChatColor.DARK_RED + "a pickaxe and a redstone current.");
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
			if (inv.getName() != null && inv.getName().equals(ChatColor.RED + "Block Breaker")){
				Material type = e.getItem().getType();
				if (type == Material.DIAMOND_PICKAXE || type == Material.IRON_PICKAXE || type == Material.STONE_PICKAXE || type == Material.WOOD_PICKAXE){
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
					
					// break block in front -- first check if it's a breakable block
					block = block.getRelative(facing);
					Material breakType = block.getType();
					if (breakType != Material.AIR 
							&& breakType != Material.BEDROCK 
							&& breakType != Material.ENDER_PORTAL_FRAME 
							&& breakType != Material.ENDER_PORTAL){
						if (block.getState() instanceof InventoryHolder 
								&& ((InventoryHolder)block.getState()).getInventory().getName().contains(".") == false){
							Bukkit.getServer().broadcastMessage( ((InventoryHolder)block.getState()).getInventory().getName() );
							block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, breakType.getId(), 10); // break effect and sound
							Location temp = block.getLocation().add(0.5, 0.0, 0.5);
							ItemStack stack = new ItemStack(block.getType(), 1, block.getData());
							ItemMeta meta = stack.getItemMeta();
							meta.setDisplayName(((InventoryHolder)block.getState()).getInventory().getName());
							stack.setItemMeta(meta);
							temp.getWorld().dropItem(temp, stack);
							block.setType(Material.AIR);
						}
						else{
							block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, breakType.getId(), 10); // break effect and sound
							block.breakNaturally(new ItemStack(type)); // break the block with the material that was dispensed
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e){
		if (e.getBlock().getType() == Material.DISPENSER){
			Dispenser dispenser = (Dispenser) e.getBlock().getState();
			if (dispenser.getInventory().getName() != null && dispenser.getInventory().getName().equals(ChatColor.RED + "Block Breaker")){
				e.getBlock().breakNaturally(new ItemStack(Material.AIR)); // remove
				e.getBlock().getWorld().dropItem(e.getBlock().getLocation().add(0.5, 0.5, 0.5), getItem());
			}
		}
	}
}
