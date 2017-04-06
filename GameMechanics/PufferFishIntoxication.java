package Evolution.GameMechanics;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import Evolution.Main.Core;

public class PufferFishIntoxication implements Listener{

	public PufferFishIntoxication(){
	}
	
	@EventHandler
	public void onPlayerInteractWithPufferfish(PlayerInteractAtEntityEvent e){
		if (e.getRightClicked() instanceof Player
				&& e.getHand() == EquipmentSlot.HAND
				&& e.getPlayer().getInventory().getItemInMainHand() != null
				&& e.getPlayer().getInventory().getItemInMainHand().getType() == Material.RAW_FISH
				&& e.getPlayer().getInventory().getItemInMainHand().getDurability() == (short)3){
			Player player = e.getPlayer();
			Player clickedPlayer = (Player) e.getRightClicked();
			// player right clicked another player while holding a pufferfish
			// give right clicked player intoxication effect
			@SuppressWarnings("unused")
			FOVEffect fovEffect = new FOVEffect(clickedPlayer);
			
			// remove one pufferfish from players inventory
			if (player.getInventory().getItemInMainHand().getAmount() == 1){
				// remove item
				Bukkit.getScheduler().scheduleSyncDelayedTask(Core.thisPlugin, new Runnable(){
					@Override
					public void run() {
						player.getInventory().setItemInMainHand(new ItemStack(Material.AIR, 1)); // remove the item at that slot
					}
				}, 1);
			}
			else{
				// decrement item amount
				Bukkit.getScheduler().scheduleSyncDelayedTask(Core.thisPlugin, new Runnable(){
					@Override
					public void run() {
						player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount()-1);
					}
				}, 1);
			}
		}
	}
}
