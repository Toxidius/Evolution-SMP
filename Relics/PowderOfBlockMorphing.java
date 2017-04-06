package Evolution.Relics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Evolution.Main.Core;

public class PowderOfBlockMorphing implements Listener{
	
	private HashMap<String, PowderOfBlockMorphingWatcher> playerWatchers;
	private ItemStack relic;
	
	public PowderOfBlockMorphing(){
		relic = new ItemStack(Material.BLAZE_POWDER, 1);
		ItemMeta meta = relic.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Powder of Block Morphing");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.DARK_RED + "Ancient Relic!");
		lore.add(ChatColor.DARK_RED + "Turns the user into a block of their choice!");
		lore.add(ChatColor.DARK_RED + "Right click a block to morph into that block");
		lore.add(ChatColor.DARK_RED + "Right click again to morph back.");
		meta.setLore(lore);
		relic.setItemMeta(meta);
		relic.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
		Core.relics.add(relic);
		
		playerWatchers = new HashMap<>();
	}
	
	public ItemStack getRelic(){
		return relic.clone();
	}
	
	public void showPlayer(Player player){
		for (Player otherPlayer : Bukkit.getOnlinePlayers()){
			if (otherPlayer.isOnline()){
				otherPlayer.showPlayer(player);
			}
		}
	}
	
	public void hidePlayer(Player player){
		for (Player otherPlayer : Bukkit.getOnlinePlayers()){
			if (otherPlayer.isOnline()){
				otherPlayer.hidePlayer(player);
			}
		}
	}
	
	public void stopAll(){
		for (String name : playerWatchers.keySet()){
			playerWatchers.get(name).stop();
		}
	}
	
	// drop head code and the drop axe code
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerRightClickMorphingPowder(PlayerInteractEvent e){
		if (e.isCancelled())
			return;
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK
				&& e.getHand() == EquipmentSlot.HAND
				&& e.getPlayer().getInventory().getItemInMainHand().getType() == Material.BLAZE_POWDER
				&& e.getPlayer().getInventory().getItemInMainHand().hasItemMeta()
				&& e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasDisplayName()
				&& e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(ChatColor.RED + "Powder of Block Morphing")){
			String name = e.getPlayer().getName();
			Player player = e.getPlayer();
			if (playerWatchers.containsKey(name)){
				// unmorph this player
				player.sendMessage(ChatColor.GRAY + "You have un-morphed.");
				playerWatchers.get(name).stop();
				playerWatchers.remove(name);
				//showPlayer(player);
				return;
			}
			player.sendMessage(ChatColor.GRAY + "You have been morphed into a block.");
			PowderOfBlockMorphingWatcher watcher = new PowderOfBlockMorphingWatcher(player, e.getClickedBlock().getType(), e.getClickedBlock().getData());
			playerWatchers.put(name, watcher);
			player.removePotionEffect(PotionEffectType.INVISIBILITY);
			player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 80, 0, false, false)); // invisiblity for 4 seconds with no particles
			//hidePlayer(player);
		}
	}
	
	@EventHandler
	public void onServerReload(PluginDisableEvent e){
		// end all watchers and remove falling blocks
		for (String playerName : playerWatchers.keySet()){
			playerWatchers.get(playerName).stop();
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		// old code -- no longer used
		/*
		Player player = e.getPlayer();
		Player otherPlayer;
		for (String name : playerWatchers.keySet()){
			otherPlayer = Bukkit.getPlayer(name);
			if (otherPlayer.isOnline()){
				//player.hidePlayer(otherPlayer);
			}
		}
		*/
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e){
		if (playerWatchers.containsKey(e.getPlayer().getName())){
			playerWatchers.get(e.getPlayer().getName()).stop();
			playerWatchers.remove(e.getPlayer().getName());
		}
	}
	
	@EventHandler
	public void onEntityChangeBlock(EntityChangeBlockEvent e){
		if (e.getEntity().getCustomName() != null
				&& e.getEntity().getCustomName().equals("noDrop")){
			e.setCancelled(true);
		}
	}
}
