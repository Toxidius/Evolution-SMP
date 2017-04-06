package Evolution.Relics;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Evolution.Main.Core;

public class JackHammer implements Listener{
	
	private ItemStack relic;
	
	public JackHammer(){
		relic = new ItemStack(Material.IRON_PICKAXE, 1);
		ItemMeta meta = relic.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Jack's Hammer");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.DARK_RED + "Ancient Relic!");
		meta.setLore(lore);
		relic.setItemMeta(meta);
		//relic.addEnchantment(Enchantment.DURABILITY, 3);
		//relic.addEnchantment(Enchantment.DIG_SPEED, 3);
		
		Core.relics.add(relic);
	}
	
	public ItemStack getRelic(){
		return relic.clone();
	}
	
	@EventHandler
	public void onJackHammerBlockBreak(BlockBreakEvent e){
		if (e.getPlayer().getInventory().getItemInMainHand() != null
				&& e.getPlayer().getInventory().getItemInMainHand().hasItemMeta()
				&& e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasDisplayName()
				&& e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.RED + "Jack's Hammer")){
			Block block = e.getBlock();
			Player player = e.getPlayer();
			World world = block.getWorld();
			ItemStack itemInHand = player.getInventory().getItemInMainHand();
			Material typeBroken = block.getType();
			int xFrom = block.getX()-1;
			int xTo = block.getX()+1;
			int yFrom = block.getY()-1;
			int yTo = block.getY()+1;
			int zFrom = block.getZ()-1;
			int zTo = block.getZ()+1;
			
			Block temp;
			for (int y = yFrom; y <= yTo; y++){
				for (int x = xFrom; x <= xTo; x++){
					for (int z = zFrom; z <= zTo; z++){
						if (x == block.getX() && y == block.getY() && z == block.getZ()){
							continue; // ignore this block so the durability code executes
						}
						
						temp = world.getBlockAt(x, y, z);
						if (temp.getType() == typeBroken){
							// break block
							temp.breakNaturally(itemInHand);
							
							// experience drop
							if (temp.getType() == Material.COAL_ORE 
									|| temp.getType() == Material.REDSTONE_ORE 
									|| temp.getType() == Material.GLOWING_REDSTONE_ORE 
									|| temp.getType() == Material.LAPIS_ORE
									|| temp.getType() == Material.EMERALD_ORE
									|| temp.getType() == Material.DIAMOND_ORE
									|| temp.getType() == Material.QUARTZ_ORE){
								ExperienceOrb orb = (ExperienceOrb) world.spawnEntity(temp.getLocation(), EntityType.EXPERIENCE_ORB);
								orb.setExperience(5);
							}
						}
					}
				}
			}
			
			// end if
		}
	}

}
