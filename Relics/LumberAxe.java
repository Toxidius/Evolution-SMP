package Evolution.Relics;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Evolution.Main.Core;

public class LumberAxe implements Listener{
	
	private ItemStack relic;
	
	public LumberAxe(){
		relic = new ItemStack(Material.DIAMOND_AXE, 1);
		ItemMeta meta = relic.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Lumber Axe");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.DARK_RED + "Ancient Relic!");
		lore.add(ChatColor.DARK_RED + "Why not chop the WHOLE tree down??");
		meta.setLore(lore);
		relic.setItemMeta(meta);
		
		Core.relics.add(relic);
	}
	
	public ItemStack getRelic(){
		return relic.clone();
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onLumberAxeBlockBreak(BlockBreakEvent e){
		if (e.getPlayer().getInventory().getItemInMainHand() != null
				&& e.getPlayer().getInventory().getItemInMainHand().hasItemMeta()
				&& e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasDisplayName()
				&& e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.RED + "Lumber Axe")
				&& (e.getBlock().getType() == Material.LOG || e.getBlock().getType() == Material.LOG_2)
				&& (e.getBlock().getRelative(BlockFace.UP).getType() == Material.LOG || e.getBlock().getRelative(BlockFace.UP).getType() == Material.LOG_2)
				&& (e.getBlock().getType() == e.getBlock().getRelative(BlockFace.UP).getType())
				&& (e.getBlock().getData() == e.getBlock().getRelative(BlockFace.UP).getData())){
			
			// player is breaking what we hope is a tree with the lumber axe
			Block block = e.getBlock();
			Player player = e.getPlayer();
			ItemStack itemInHand = player.getInventory().getItemInMainHand();
			
			ArrayList<Block> logs = new ArrayList<>();
			int numLogsFound = 0;
		
			// find all the logs near the one that was broken
			// ignore the actual block that was broken
			if (block.getType() == Material.LOG
					&& block.getData() == (byte)3){
				logs = getJungleLogBlocks(block, 10); // jungle block searching algorithm
				numLogsFound = logs.size();
			}
			else{
				logs = getJungleLogBlocks(block, 4); // basic (recursive) block searching algorithm TODO
				numLogsFound = logs.size();
			}
			
			// check if this isn't actually a tree
			// the last (top) log doesn't have a leaf block by it
			Block lastLog = logs.get(logs.size()-1);
			if (lastLog.getRelative(BlockFace.UP).getType() != Material.LEAVES
					&& lastLog.getRelative(BlockFace.UP).getType() != Material.LEAVES_2
					&& lastLog.getRelative(BlockFace.NORTH).getType() != Material.LEAVES
					&& lastLog.getRelative(BlockFace.NORTH).getType() != Material.LEAVES_2
					&& lastLog.getRelative(BlockFace.SOUTH).getType() != Material.LEAVES
					&& lastLog.getRelative(BlockFace.SOUTH).getType() != Material.LEAVES_2
					&& lastLog.getRelative(BlockFace.EAST).getType() != Material.LEAVES
					&& lastLog.getRelative(BlockFace.EAST).getType() != Material.LEAVES_2
					&& lastLog.getRelative(BlockFace.WEST).getType() != Material.LEAVES
					&& lastLog.getRelative(BlockFace.WEST).getType() != Material.LEAVES_2){
				// this isn't an actual tree... it could be some other structure like a building
				return; // don't perform the lumber chop effect on non-tree structures
			}
			
			// check if axe has unbreaking
			double percentage = 1.0; // percentage is used to modify the durability used if the axe has some sort of unbreaking
			if (itemInHand.containsEnchantment(Enchantment.DURABILITY)){
				int level = itemInHand.getEnchantmentLevel(Enchantment.DURABILITY);
				if (level == 1){
					percentage = 0.75;
				}
				else if (level == 2){
					percentage = 0.65;
				}
				else if (level == 3){
					percentage = 0.50;
				}
			}
			
			// deduct durability
			int numberOfDurabilityToRemove = (int) Math.round((numLogsFound * percentage) + 1); // plus 1 for the log the axe breaks on it's own
			int durabilityRemaining = itemInHand.getType().getMaxDurability() - itemInHand.getDurability();
			if (durabilityRemaining > numberOfDurabilityToRemove){
				Bukkit.getScheduler().scheduleSyncDelayedTask(Core.thisPlugin, new Runnable() {
					@Override
					public void run() {
						short newDurability = (short) ((int)itemInHand.getDurability() + Math.round(numberOfDurabilityToRemove));
						itemInHand.setDurability(newDurability);
					}
				}, 1); // 1 tick later set the new durability of the axe (delayed because the break event updates durability after this event is called)
			}
			else{
				// can't do this because it'll take too much durability
				player.sendMessage(ChatColor.GRAY + "You don't have enough durability to do this!");
				return;
			}
			
			// break all of the logs inside the arraylist
			@SuppressWarnings("unused")
			LumberAxeEffect effect = new LumberAxeEffect(logs);
		}
	}
	
	public ArrayList<Block> getLogBlocks(Block startingBlock){
		ArrayList<Block> logs = new ArrayList<>();
		logs.add(startingBlock);
		
		// start the recursive lookup for blocks
		checkNearbyBlocks(startingBlock, logs);
		
		// remove the starting block because it's automatically removed after by the event call
		logs.remove(startingBlock); 
		
		return logs;
	}
	
	public void checkNearbyBlocks(Block currentBlock, ArrayList<Block> logs){
		if (isLog(currentBlock.getRelative(BlockFace.NORTH))
					&& logs.contains(currentBlock.getRelative(BlockFace.NORTH)) == false){
			logs.add(currentBlock.getRelative(BlockFace.NORTH));
			checkNearbyBlocks(currentBlock.getRelative(BlockFace.NORTH), logs); // recursive call
		}
		if (isLog(currentBlock.getRelative(BlockFace.NORTH_EAST))
					&& logs.contains(currentBlock.getRelative(BlockFace.NORTH_EAST)) == false){
			logs.add(currentBlock.getRelative(BlockFace.NORTH_EAST));
			checkNearbyBlocks(currentBlock.getRelative(BlockFace.NORTH_EAST), logs); // recursive call
		}
		if (isLog(currentBlock.getRelative(BlockFace.EAST))
					&& logs.contains(currentBlock.getRelative(BlockFace.EAST)) == false){
			logs.add(currentBlock.getRelative(BlockFace.EAST));
			checkNearbyBlocks(currentBlock.getRelative(BlockFace.EAST), logs); // recursive call
		}
		if (isLog(currentBlock.getRelative(BlockFace.SOUTH_EAST))
					&& logs.contains(currentBlock.getRelative(BlockFace.SOUTH_EAST)) == false){
			logs.add(currentBlock.getRelative(BlockFace.SOUTH_EAST));
			checkNearbyBlocks(currentBlock.getRelative(BlockFace.SOUTH_EAST), logs); // recursive call
		}
		if (isLog(currentBlock.getRelative(BlockFace.SOUTH))
					&& logs.contains(currentBlock.getRelative(BlockFace.SOUTH)) == false){
			logs.add(currentBlock.getRelative(BlockFace.SOUTH));
			checkNearbyBlocks(currentBlock.getRelative(BlockFace.SOUTH), logs); // recursive call
		}
		if (isLog(currentBlock.getRelative(BlockFace.SOUTH_WEST))
					&& logs.contains(currentBlock.getRelative(BlockFace.SOUTH_WEST)) == false){
			logs.add(currentBlock.getRelative(BlockFace.SOUTH_WEST));
			checkNearbyBlocks(currentBlock.getRelative(BlockFace.SOUTH_WEST), logs); // recursive call
		}
		if (isLog(currentBlock.getRelative(BlockFace.WEST))
					&& logs.contains(currentBlock.getRelative(BlockFace.WEST)) == false){
			logs.add(currentBlock.getRelative(BlockFace.WEST));
			checkNearbyBlocks(currentBlock.getRelative(BlockFace.WEST), logs); // recursive call
		}
		if (isLog(currentBlock.getRelative(BlockFace.NORTH_WEST))
				&& logs.contains(currentBlock.getRelative(BlockFace.NORTH_WEST)) == false){
			logs.add(currentBlock.getRelative(BlockFace.NORTH_WEST));
			checkNearbyBlocks(currentBlock.getRelative(BlockFace.NORTH_WEST), logs); // recursive call
		}
		if (isLog(currentBlock.getRelative(BlockFace.UP))
					&& logs.contains(currentBlock.getRelative(BlockFace.UP)) == false){
			logs.add(currentBlock.getRelative(BlockFace.UP));
			checkNearbyBlocks(currentBlock.getRelative(BlockFace.UP), logs); // recursive call
		}
	}
	
	public boolean isLog(Block block){
		if (block.getType() == Material.LOG
				|| block.getType() == Material.LOG_2){
			return true;
		}
		return false;
	}
	
	public ArrayList<Block> getJungleLogBlocks(Block startingBlock, int maxBlockDistance){
		World world = startingBlock.getWorld();
		int numLogsFoundOnThisLevel;
		int currentY = startingBlock.getY();
		ArrayList<Block> logs = new ArrayList<>();
		
		while (true){
			numLogsFoundOnThisLevel = 0;
			
			int xFrom = startingBlock.getX()-4;
			int xTo = startingBlock.getX()+4;
			int zFrom = startingBlock.getZ()-4;
			int zTo = startingBlock.getZ()+4;
			Block temp;
			for (int x = xFrom; x <= xTo; x++){
				for (int z = zFrom; z <= zTo; z++){
					temp = world.getBlockAt(x, currentY, z);
					if (temp.equals(startingBlock) == true){
						// ignore this block because it's the one the player used the axe to break
						continue;
					}
					else if(isLog(temp)){
						numLogsFoundOnThisLevel++;
						logs.add(temp);
					}
				}
			}
			
			if (numLogsFoundOnThisLevel != 0){
				currentY++; // increment to the next Y to check for logs
			}
			else if (numLogsFoundOnThisLevel == 0 && currentY == startingBlock.getY()){
				currentY++; // increment to the next Y to check for logs
			}
			else{
				break; // end the loop of checking for logs
			}
		}
		
		return logs;
	}

}
