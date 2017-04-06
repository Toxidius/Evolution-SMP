package Evolution.Relics;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Evolution.Main.Core;

public class SwordOfFreezing implements Listener{
	
	private ItemStack relic;
	
	public SwordOfFreezing(){
		relic = new ItemStack(Material.IRON_SWORD, 1);
		ItemMeta meta = relic.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Sword of Freezing");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.DARK_RED + "Ancient Relic!");
		lore.add(ChatColor.DARK_RED + "Originating in the tundra's of northern");
		lore.add(ChatColor.DARK_RED + "Minecraftia, this sword freezes your");
		lore.add(ChatColor.DARK_RED + "enemies in times of danger.");
		meta.setLore(lore);
		relic.setItemMeta(meta);
		
		Core.relics.add(relic);
	}
	
	public ItemStack getRelic(){
		return relic.clone();
	}
	
	@EventHandler
	public void onMobKillWithFreezingSword(EntityDamageByEntityEvent e){
		if (e.isCancelled())
			return;
		if (e.getEntity() instanceof LivingEntity
				&& e.getDamager() instanceof Player
				&& ((LivingEntity)e.getEntity()).getHealth()-e.getFinalDamage() > 0.0
				&& ((LivingEntity)e.getEntity()).isDead() == false
				&& ((Player)e.getDamager()).getHealth() <= 18.0){
			// player attacked an entity with <= 9 hearts
			// check if they were killed using a freezing sword
			Player player = (Player) e.getDamager();
			ItemStack itemInHand = player.getInventory().getItemInMainHand();
			if (itemInHand != null
					&& itemInHand.hasItemMeta()
					&& itemInHand.getItemMeta().hasDisplayName()
					&& itemInHand.getItemMeta().getDisplayName().equals(ChatColor.RED + "Sword of Freezing")){
				// this entity was attacked using the sword -- trigger freeze event
				ArrayList<Block> frozenBlocks = new ArrayList<>();
				Location center = e.getEntity().getLocation();
				Location temp;
				int startX = center.getBlockX()-1;
				int startY = center.getBlockY();
				int startZ = center.getBlockZ()-1;
				int endX = center.getBlockX()+1;
				int endY = center.getBlockY()+2;
				int endZ = center.getBlockZ()+1;
				for (int y = startY; y <= endY; y++){
					for (int x = startX; x <= endX; x++){
						for (int z = startZ; z <= endZ; z++){
							if (x == center.getBlockX() && z == center.getBlockZ() && y < center.getBlockY()+2){
								continue; // skip these blocks as the player is in them
							}
							temp = new Location(center.getWorld(), x, y, z);
							if (temp.getBlock().getType() == Material.AIR){
								temp.getBlock().setType(Material.ICE);
								frozenBlocks.add(temp.getBlock());
							}
						}
					}
				}
				e.getEntity().teleport(new Location(center.getWorld(), center.getBlockX()+0.5, center.getBlockY(), center.getBlockZ()+0.5, e.getEntity().getLocation().getYaw(), e.getEntity().getLocation().getPitch())); // round off player to the block
				
				// start the unfreeze runnable
				Bukkit.getScheduler().scheduleSyncDelayedTask(Core.thisPlugin, new Runnable(){
					@Override
					public void run() {
						for (Block block : frozenBlocks){
							block.setType(Material.AIR);
						}
					}
				}, 60L);
			}
			
		}
		// end if
	}

}
