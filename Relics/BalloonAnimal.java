package Evolution.Relics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.PolarBear;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Evolution.Main.Core;

public class BalloonAnimal implements Listener{

	private ItemStack relic;
	public ArrayList<Player> activePlayers;
	public HashMap<Player, ArrayList<BalloonAnimalWatcher>> playerBalloonWatchers;
	
	public BalloonAnimal() {
		relic = new ItemStack(Material.LEASH, 1);
		ItemMeta meta = relic.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Balloon Animal!");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.DARK_RED + "Ancient Relic!");
		lore.add(ChatColor.DARK_RED + "Your own cute little balloon animal!");
		meta.setLore(lore);
		relic.setItemMeta(meta);
		relic.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		Core.relics.add(relic);
		
		activePlayers = new ArrayList<>();
		playerBalloonWatchers = new HashMap<>();
	}
	
	public ItemStack getRelic(){
		return relic.clone();
	}
	
	@EventHandler
	public void onRightClickBalloonAnimal(PlayerInteractEvent e){
		if (e.getAction() == Action.RIGHT_CLICK_AIR
				|| e.getAction() == Action.RIGHT_CLICK_BLOCK){
			if (e.getHand() == EquipmentSlot.HAND
					&& e.getPlayer().getInventory().getItemInMainHand() != null
					&& e.getPlayer().getInventory().getItemInMainHand().getType() == Material.LEASH
					&& e.getPlayer().getInventory().getItemInMainHand().hasItemMeta()
					&& e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasDisplayName()
					&& e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.RED + "Balloon Animal!")){
				// check if player already has balloon animals active
				if (activePlayers.contains(e.getPlayer()) == true
						&& playerBalloonWatchers.containsKey(e.getPlayer()) == true){
					// already has balloons active, remove them
					ArrayList<BalloonAnimalWatcher> listOfWatchers = playerBalloonWatchers.get(e.getPlayer());
					for (BalloonAnimalWatcher watcher : listOfWatchers){
						watcher.end();
					}
					
					// cleanup
					playerBalloonWatchers.remove(e.getPlayer());
					activePlayers.remove(e.getPlayer());
				}
				else{
					// does not have balloons active, makes new ones
					EntityType type = EntityType.PIG;
					int random = Core.r.nextInt(10);
					if (random == 0){
						type = EntityType.COW;
					}
					else if (random == 1){
						type = EntityType.CHICKEN;
					}
					else if (random == 2){
						type = EntityType.SHEEP;
					}
					else if (random == 3){
						type = EntityType.PIG;
					}
					else if (random == 4){
						type = EntityType.RABBIT;
					}
					else if (random == 5){
						type = EntityType.POLAR_BEAR;
					}
					else if (random == 6){
						type = EntityType.SQUID;
					}
					else if (random == 7){
						type = EntityType.SLIME;
					}
					else if (random == 8){
						type = EntityType.BAT;
					}
					else if (random == 9){
						type = EntityType.VEX;
					}
					
					Player player = e.getPlayer();
					ArrayList<BalloonAnimalWatcher> listOfWatchers = new ArrayList<>();
					int numToSpawn;
					if (type != EntityType.BAT){
						numToSpawn = 4 + Core.r.nextInt(3); // 4 to 6
					}
					else{
						numToSpawn = 4 + Core.r.nextInt(22); // 4 to 25
					}
					
					for (int i = 0; i < numToSpawn; i++){
						double verticalRandom = Math.random();		
						LivingEntity living = (LivingEntity) player.getWorld().spawnEntity(player.getLocation().add(0.0, (3.0+verticalRandom), 0.0), type);
						if (living instanceof Cow){
							((Cow)living).setBaby();
						}
						else if (living instanceof Chicken){
							((Chicken)living).setBaby();
						}
						else if (living instanceof Sheep){
							((Sheep)living).setBaby();
						}
						else if (living instanceof Pig){
							((Pig)living).setBaby();
						}
						else if (living instanceof Rabbit){
							((Rabbit)living).setBaby();
						}
						else if (living instanceof PolarBear){
							((PolarBear)living).setBaby();
						}
						else if (living instanceof Llama){
							((Llama)living).setBaby();
						}
						else if (living instanceof Slime){
							int sizeRandom = Core.r.nextInt(3);
							if (sizeRandom == 0){
								((Slime)living).setSize(1);
							}
							else if (sizeRandom == 1){
								((Slime)living).setSize(2);
							}
							else if (sizeRandom == 2){
								((Slime)living).setSize(4);
							}
						}
						
						living.setGravity(false);
						living.setLeashHolder(player);
						living.setCustomName("BalloonAnimal");
						living.setCustomNameVisible(false);
						BalloonAnimalWatcher watcher = new BalloonAnimalWatcher(living, player);
						listOfWatchers.add(watcher);
					}
					playerBalloonWatchers.put(player, listOfWatchers);
					activePlayers.add(player);
				}
			}
		}
	}
	
	@EventHandler
	public void onRightClickAnimalWithBalloonAnimalRelic(PlayerInteractAtEntityEvent e){
		if (e.getHand() == EquipmentSlot.HAND
				&& e.getPlayer().getInventory().getItemInMainHand() != null
				&& e.getPlayer().getInventory().getItemInMainHand().getType() == Material.LEASH
				&& e.getPlayer().getInventory().getItemInMainHand().hasItemMeta()
				&& e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasDisplayName()
				&& e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.RED + "Balloon Animal!")){
			//Bukkit.getServer().broadcastMessage("canceled");
			e.setCancelled(true); // prevent leashing with the balloon animal relic
		}
	}
	
	@EventHandler
	public void onServerReload(PluginDisableEvent e){
		// remove all the active balloons by looping through all the watchers and ending them
		ArrayList<BalloonAnimalWatcher> listOfWatchers;
		for (Player player : playerBalloonWatchers.keySet()){
			listOfWatchers = playerBalloonWatchers.get(player);
			for (BalloonAnimalWatcher watcher : listOfWatchers){
				watcher.end();
			}
		}
	}
	
	@EventHandler
	public void onBalloonAnimalHurt(EntityDamageEvent e){
		if (e.getEntity() instanceof LivingEntity
				&& e.getEntity().getCustomName() != null
				&& e.getEntity().getCustomName().equals("BalloonAnimal")
				&& ((LivingEntity)e.getEntity()).getLeashHolder() instanceof Player){
			e.setCancelled(true); // no dmg for balloon animals being leashed
		}
	}
	
	@EventHandler
	public void onBalloonAnimalUnleash(PlayerUnleashEntityEvent e){
		if (e.getEntity() instanceof LivingEntity
				&& e.getEntity().getCustomName() != null
				&& e.getEntity().getCustomName().equals("BalloonAnimal")){
			e.setCancelled(true); // no unleashing
		}
	}
}