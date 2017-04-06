package Evolution.HolidayRelics;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import Evolution.Main.Core;

public class SnowballGun implements Listener{
	
	private ItemStack relic;
	
	public SnowballGun(){
		relic = new ItemStack(Material.IRON_BARDING, 1);
		ItemMeta meta = relic.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Snowball Gun");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.DARK_RED + "Ancient Relic!");
		meta.setLore(lore);
		relic.setItemMeta(meta);
		relic.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
	}
	
	public ItemStack getRelic(){
		return relic.clone();
	}
	
	@EventHandler
	public void onRightClickSnowballGun(PlayerInteractEvent e){
		if ( (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)
				&& e.getHand() == EquipmentSlot.HAND
				&& e.getPlayer().getInventory().getItemInMainHand().getType() == Material.IRON_BARDING
				&& e.getPlayer().getInventory().getItemInMainHand().hasItemMeta()
				&& e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasDisplayName()
				&& e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains(ChatColor.RED + "Snowball Gun")){
			
			// spawns custom snowball
			Vector velocity = e.getPlayer().getEyeLocation().getDirection().multiply(1.5);
			Player shooter = e.getPlayer();
			shooter.getWorld().playSound(shooter.getLocation(), Sound.BLOCK_LAVA_POP, 1F, 2F);
			Location spawnLocation = shooter.getEyeLocation();
			spawnLocation.add(shooter.getEyeLocation().getDirection().multiply(1.0));
			
			Snowball ball = (Snowball) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.SNOWBALL);
			ball.setShooter(shooter);
			ball.setVelocity(velocity);
			ball.setCustomName("ChristmasGunSnowball");
			e.setCancelled(true);
		}
		
		// end if
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onSnowballGunLand(ProjectileHitEvent e){
		if (e.getEntity() instanceof Snowball
				&& e.getEntity().getCustomName() != null
				&& e.getEntity().getCustomName().equals("ChristmasGunSnowball")){
			// special snow effect
			Location center = e.getEntity().getLocation();
			Location temp;
			
			int radius = 3;
			int startX = center.getBlockX()-radius;
			int startY = center.getBlockY()-radius;
			int startZ = center.getBlockZ()-radius;
			int endX = center.getBlockX()+radius;
			int endY = center.getBlockY()+radius;
			int endZ = center.getBlockZ()+radius;
			for (int y = startY; y <= endY; y++){
				for (int x = startX; x <= endX; x++){
					for (int z = startZ; z <= endZ; z++){
						temp = new Location(center.getWorld(), x, y, z);
						if (temp.distance(center) <= radius 
								&& (temp.getBlock().getType().isSolid()
										&& (temp.getBlock().getRelative(BlockFace.UP).getType() == Material.AIR || temp.getBlock().getRelative(BlockFace.UP).getType() == Material.LONG_GRASS))){
							Block blockToChange = temp.getBlock().getRelative(BlockFace.UP);
							Material oldMaterial = blockToChange.getType();
							byte oldData = blockToChange.getData();
							blockToChange.setType(Material.SNOW);

							Bukkit.getScheduler().scheduleSyncDelayedTask(Core.thisPlugin, new Runnable(){
								@Override
								public void run() {
									if (blockToChange.getType() == Material.SNOW){
										blockToChange.setType(oldMaterial);
										blockToChange.setData(oldData);
									}
								}
							}, Core.r.nextInt(100)+40); // 1 to 6 seconds remove the snow
						}
					}
				}
			}
		}
	}
}
