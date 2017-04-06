package Evolution.Relics;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import Evolution.Main.Core;

public class ExpExtractor implements Listener{

	private ItemStack relic;
	
	public ExpExtractor(){
		relic = new ItemStack(Material.END_CRYSTAL, 1);
		ItemMeta meta = relic.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Exp Extractor");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.DARK_RED + "Extracts one level of experience when right clicked");
		meta.setLore(lore);
		relic.setItemMeta(meta);
		
		Core.relics.add(relic);
	}
	
	public ItemStack getRelic(){
		return relic.clone();
	}
	
	@EventHandler
	public void onExpExtractorRightClick(PlayerInteractAtEntityEvent e){
		if (e.getHand() == EquipmentSlot.HAND
				&& e.getRightClicked() instanceof EnderCrystal
				&& e.getRightClicked().getCustomName() != null
				&& e.getRightClicked().getCustomName().equals(ChatColor.RED + "Exp Extractor")
				&& e.getPlayer().getLevel() > 0){
			
			int currentLevel = e.getPlayer().getLevel();
			int amountToSpawn = 0;
			if (currentLevel < 5){
				amountToSpawn = 1;
			}
			else if (currentLevel >= 5
					&& currentLevel < 10){
				amountToSpawn = 2;
			}
			else if (currentLevel >= 10
					&& currentLevel < 20){
				amountToSpawn = 5;
			}
			else if (currentLevel >= 20
					&& currentLevel < 30){
				amountToSpawn = 12;
			}
			else if (currentLevel >= 30){
				amountToSpawn = 20;
			}
			
			Location spawnLocation = e.getRightClicked().getLocation().add(0.0, 1.0, 0.0);
			Item dropped = e.getRightClicked().getWorld().dropItem(spawnLocation, new ItemStack(Material.EXP_BOTTLE, amountToSpawn));
			dropped.setVelocity(new Vector(0.0, 0.6, 0.0));
			
			e.getPlayer().setLevel(currentLevel-1);
			
			// particle effect
			Location effectLocation;
			for (int i = 0; i < 20; i++){
				effectLocation = dropped.getLocation().add(Math.random()*2-1, Math.random()*2-1, Math.random()*2-1);
				effectLocation.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, effectLocation, 1);
			}
			dropped.getWorld().playSound(dropped.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 2.0F);
			dropped.getWorld().playSound(dropped.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.5F);
		}
	}
	
	@EventHandler
	public void onExpExtractorPlace(PlayerInteractEvent e){
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK
				&& e.getHand() == EquipmentSlot.HAND
				&& e.getPlayer().getInventory().getItemInMainHand().getType() == Material.END_CRYSTAL
				&& e.getPlayer().getInventory().getItemInMainHand().hasItemMeta()
				&& e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.RED + "Exp Extractor")){
			e.setCancelled(true); // prevent regular minecraft code for ender crystal placement
			int amount = e.getPlayer().getInventory().getItemInMainHand().getAmount();
			if (amount == 1){
				// remove it from hand
				e.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.AIR, 1));
			}
			else{
				// more than 1 in hand
				// remove 1 from hand
				e.getPlayer().getInventory().getItemInMainHand().setAmount(amount--);
			}
			
			Location spawnLocation = e.getClickedBlock().getLocation().add(0.5, 1.0, 0.5);
			EnderCrystal crystal = (EnderCrystal) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.ENDER_CRYSTAL);
			crystal.setCustomName(ChatColor.RED + "Exp Extractor");
			crystal.setCustomNameVisible(true);
		}
	}
	
	@EventHandler
	public void onExpExtractorDamage(EntityDamageEvent e){
		if (e.getEntity() instanceof EnderCrystal
				&& e.getEntity().getCustomName() != null
				&& e.getEntity().getCustomName().equals(ChatColor.RED + "Exp Extractor")){
			e.setCancelled(true); // prevent explosion
			
			if (e.getCause() == DamageCause.PROJECTILE){
				EntityDamageByEntityEvent e2 = (EntityDamageByEntityEvent) e;
				e2.getDamager().remove(); // remove the arrow that did the damage
				e.getEntity().setFireTicks(1);
			}
			
			if (e.getCause() == DamageCause.ENTITY_ATTACK){
				EntityDamageByEntityEvent e2 = (EntityDamageByEntityEvent) e;
				if (e2.getDamager() instanceof Player){
					e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation().add(0.0, 1.0, 0.0), getRelic());
					e.getEntity().remove();
				}
			}
		}
	}
	
	@EventHandler
	public void onExpExtractorCombust(ExplosionPrimeEvent e){
		if (e.getEntity() instanceof EnderCrystal
				&& e.getEntity().getCustomName() != null
				&& e.getEntity().getCustomName().equals(ChatColor.RED + "Exp Extractor")){
			e.setCancelled(true); // prevent explosion
		}
	}
}
