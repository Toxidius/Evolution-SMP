package Evolution.GameMechanics;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Evolution.Main.Core;

public class BlockPlacer implements Listener{

	private ItemStack item;
	
	public BlockPlacer(){
		item = new ItemStack(Material.DISPENSER, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Block Placer");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.DARK_RED + "Places any blocks in it's inventory");
		lore.add(ChatColor.DARK_RED + "when supplied with a redstone current.");
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
			if (inv.getName() != null && inv.getName().equals(ChatColor.RED + "Block Placer")){
				ItemStack item = e.getItem();
				if (item.getType().isBlock() == false 
						&& item.getType() != Material.SEEDS
						&& item.getType() != Material.POTATO_ITEM
						&& item.getType() != Material.CARROT_ITEM
						&& item.getType() != Material.PUMPKIN_SEEDS
						&& item.getType() != Material.MELON_SEEDS
						&& item.getType() != Material.NETHER_STALK
						&& item.getType() != Material.BEETROOT_SEEDS
						&& item.getType() != Material.SUGAR_CANE){
					return; // don't continue with a non block
				}
				e.setCancelled(true); // prevent dispensing of the block as an entity
				
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
				
				// place the block in front of the dispenser
				block = block.getRelative(facing);
				if (block.getType() == Material.AIR){
					if (item.getType().isBlock() == false){
						cropPlacing(item, block);
					}
					else{
						block.setType(item.getType());
						block.setData((byte) item.getDurability());
						block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, item.getType(), 10); // break effect and sound
					}
					
					// copy the current contents manually cause weird shit is weird
					// the current contents represent the contents that should be in the inventory after
					ItemStack[] newContents = new ItemStack[9];
					for (int i = 0; i < 9; i++){
						if (inv.getItem(i) != null){
							newContents[i] = inv.getItem(i).clone();
						}
					}
					
					// remove item from dispenser inventory
					Bukkit.getScheduler().scheduleSyncDelayedTask(Core.thisPlugin, new Runnable(){
						@Override
						public void run() {
							inv.setContents(newContents);
						}
					}, 0L);
					
				}
				else{
					return; // can't place block so just end
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void cropPlacing(ItemStack stack, Block block){
		Material material = stack.getType();

		switch (material){
			case SEEDS: block.setType(Material.CROPS);
				break;
			case POTATO_ITEM: block.setType(Material.POTATO);
				break;
			case CARROT_ITEM: block.setType(Material.CARROT);
				break;
			case PUMPKIN_SEEDS: block.setType(Material.PUMPKIN_STEM);
				break;
			case MELON_SEEDS: block.setType(Material.MELON_STEM);
				break;
			case NETHER_STALK: block.setType(Material.NETHER_WARTS);
				break;
			case BEETROOT_SEEDS: block.setType(Material.BEETROOT_BLOCK);
				break;
			case SUGAR_CANE: block.setType(Material.SUGAR_CANE_BLOCK);
				break;
			default:
				break;
		}
		
		block.setData((byte)0);
		block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType(), 10); // break effect and sound
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e){
		if (e.getBlock().getType() == Material.DISPENSER){
			Dispenser dispenser = (Dispenser) e.getBlock().getState();
			if (dispenser.getInventory().getName() != null && dispenser.getInventory().getName().equals(ChatColor.RED + "Block Placer")){
				e.getBlock().breakNaturally(new ItemStack(Material.AIR)); // remove
				e.getBlock().getWorld().dropItem(e.getBlock().getLocation().add(0.5, 0.5, 0.5), getItem());
			}
		}
	}
	
	@EventHandler
	public void onHopperItemMove(InventoryMoveItemEvent e){
		if (e.getSource().getTitle() != null && e.getSource().getTitle().equals(ChatColor.RED + "Block Placer") 
				&& ((Dispenser)e.getSource().getHolder()).getBlock().isBlockPowered()){
			e.setCancelled(true); // prevent taking of items from block placers that are active
		}
	}
}
