package Evolution.GameMechanics;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class AprilFoolsCreeperTroll implements Listener{

	@SuppressWarnings("unused")
	private HashMap<Player, AprilFoolsPlayerData> playerDataMap;
	
	public AprilFoolsCreeperTroll() {
		playerDataMap = new HashMap<>();
	}
	
	/*

	// REMOVED for now
	
	@EventHandler
	public void onPlayerOpenChest(PlayerInteractEvent e){
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK
				&& e.getHand() == EquipmentSlot.HAND
				&& (e.getClickedBlock().getType() == Material.CHEST || e.getClickedBlock().getType() == Material.TRAPPED_CHEST) ){
			Location playerLocation = e.getPlayer().getLocation();
			Player player = e.getPlayer(); 
			
			// add this location to the player data
			if (playerDataMap.containsKey(player) == true){
				playerDataMap.get(player).addLocation(playerLocation);
			}
			else{
				// create a whole new playerData for this player
				AprilFoolsPlayerData playerData = new AprilFoolsPlayerData(player);
				playerData.addLocation(playerLocation);
				playerDataMap.put(player, playerData);
			}
			
			// check if the player has adequate number of chest opens to find a location for the creeper to spawn
			int numLocations = playerDataMap.get(player).getNumberOfLocations();
			if (numLocations >= 10
					&& Math.random() <= 0.20){
				long timeSinceLastTrollEvent = System.currentTimeMillis() - playerDataMap.get(player).getLastTrollEvent();
				if (timeSinceLastTrollEvent >= 300000){
					// atleast 5 mins have passed since last troll event
					// start a new troll event
					//Bukkit.getServer().broadcastMessage("trigger");
					playerDataMap.get(player).setLastTrollEvent(System.currentTimeMillis());
					
					@SuppressWarnings("unused")
					AprilFoolsCreeperTrollEffect effect = new AprilFoolsCreeperTrollEffect(player);
				}
			}
		}
	}
	*/
	
	@EventHandler
	public void onTrollCreeperDamage(EntityDamageEvent e){
		if (e.getEntity() instanceof Creeper
				&& e.getEntity().getCustomName() != null
				&& e.getEntity().getCustomName().equals("AprilFoolsTrollCreeper")){
			e.setCancelled(true); // no dmg for troll creepers
		}
	}
	
	@EventHandler
	public void onTrollCreeperExplodePrime(ExplosionPrimeEvent e){
		if (e.getEntity() instanceof Creeper
				&& e.getEntity().getCustomName() != null
				&& e.getEntity().getCustomName().equals("AprilFoolsTrollCreeper")){
			e.setCancelled(true); // no dmg for troll creepers
			
			e.getEntity().remove(); // remove creeper so he does no real damage to the player
			e.setCancelled(true); // cancel the blowing up of the creeper. so he doesn't damage the player
			
			explodeAllNearbyChests(e.getEntity().getLocation());
			
			//e.blockList().clear(); // clear block list so no blocks are destroyed
			e.getEntity().getWorld().spawnParticle(Particle.EXPLOSION_HUGE, e.getEntity().getLocation(), 1); // fake explosion sound
			e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0F, 1.0F);
			
			// schedule item and chest cleanup
			@SuppressWarnings("unused")
			AprilFoolsCreeperCleanup cleanup = new AprilFoolsCreeperCleanup(e.getEntity().getLocation());
		}
	}
	
	@EventHandler
	public void onPlayerPickupTrollItem(PlayerPickupItemEvent e){
		if (e.getItem().getCustomName() != null
				&& e.getItem().getCustomName().equals("AprilFoolsTrollItem")){
			e.setCancelled(true); // no picking up troll items
		}
	}
	
	@EventHandler
	public void onHopperPickupItem(InventoryPickupItemEvent e){
		if (e.getItem().getCustomName() != null
				&& e.getItem().getCustomName().equals("AprilFoolsTrollItem")){
			e.setCancelled(true); // no picking up troll items
		}
	}
	
	public void explodeAllNearbyChests(Location center){
		Location temp;
		Block block;
		Chest chest;
		
		int radius = 5;
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
					if (temp.getBlock().getType() == Material.CHEST
							|| temp.getBlock().getType() == Material.TRAPPED_CHEST){
						
						block = temp.getBlock();
						sendAllPlayersBlockDestory(block);
						
						// get all items that were within this chest and "fling them out"
						chest = (Chest) block.getState();
						ItemStack clone;
						Item item;
						for (ItemStack stack : chest.getBlockInventory().getContents()){
							if (stack == null
									|| stack.getType() == Material.AIR){
								continue; // skip
							}
							clone = stack.clone();
							item = block.getWorld().dropItem(block.getLocation(), clone);
							item.setCustomName("AprilFoolsTrollItem");
							item.setVelocity(new Vector(Math.random()-0.5, Math.random()-0.5, Math.random()-0.5));
						}
					}
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void sendAllPlayersBlockDestory(Block block){
		block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getTypeId(), 10); // block break effect
		
		// tell clients that the block is actually air now
		for (Player player : Bukkit.getOnlinePlayers()){
			player.sendBlockChange(block.getLocation(), Material.AIR, (byte)0);
		}
	}
}
