package Evolution.GameMechanics;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventorySnooping implements Listener, CommandExecutor{

	public InventorySnooping(){
	}
	
	@EventHandler
	public void snooperItemTakeEvent(InventoryClickEvent e){
		//Bukkit.getServer().broadcastMessage("" + e.getSlot());
		if (e.isShiftClick()
				&& e.getView().getTopInventory() != null
				&& e.getView().getTopInventory().getName() != null
				&& e.getView().getTopInventory().getName().contains(ChatColor.RED.toString())
				&& (e.getView().getTopInventory().getName().contains("Inventory of ") || e.getView().getTopInventory().getName().contains("Ender Chest of "))){
			e.setCancelled(true); // prevent shift clicking into the snooped inventory
		}
		if (e.getClickedInventory() == null
				|| e.getClickedInventory().getName() == null){
			return;
		}
		if (e.getClickedInventory().getName().contains(ChatColor.RED.toString())
				&& e.getClickedInventory().getName().contains("Inventory of ")){
			
			String targetName = e.getClickedInventory().getName().replace(ChatColor.RED + "Inventory of ", "");
			Player target = Bukkit.getPlayer(targetName);
			
			if (e.getAction() == InventoryAction.PICKUP_ALL){
				ItemStack pickedUpStack = e.getCurrentItem();
				
				if (pickedUpStack == null){
					e.setCancelled(true);
				}
				else if (e.getSlot() >= 36 && e.getSlot() <= 39){
					// armor removed
					int id = e.getSlot();
					if (id == 36){
						target.getInventory().clear(39);
					}
					else if (id == 37){
						target.getInventory().clear(38);
					}
					else if (id == 38){
						target.getInventory().clear(37);
					}
					else if (id == 39){
						target.getInventory().clear(36);
					}
				}
				else{
					int id = e.getSlot();
					if (id != -1){
						target.getInventory().clear(id);
						//Bukkit.getServer().broadcastMessage("removed");
					}
				}
				
			}
			else if (e.getAction() == InventoryAction.PLACE_ALL){
				int id = e.getSlot();
				if (id > 39){
					// empty slots
					e.setCancelled(true);
				}
				else if (id >= 36 && id <= 39){
					// armor slots
					if (id == 36 && target.getInventory().getItem(39) == null){
						target.getInventory().setItem(39, e.getCursor());
					}
					else if (id == 37 && target.getInventory().getItem(38) == null){
						target.getInventory().setItem(38, e.getCursor());
					}
					else if (id == 38 && target.getInventory().getItem(37) == null){
						target.getInventory().setItem(37, e.getCursor());
					}
					else if (id == 39 && target.getInventory().getItem(36) == null){
						target.getInventory().setItem(36, e.getCursor());
					}
				}
				else if (target.getInventory().getItem(id) == null){
					// basic inventory slots
					target.getInventory().setItem(id, e.getCursor());
					//Bukkit.getServer().broadcastMessage("added");
				}
				else{
					e.setCancelled(true); // trying to place an item into a space that's already occupied
				}
				
			}
			else{
				e.setCancelled(true); // prevent weird actions (shift clicking, right clicking etc)
			}
		}
		else if (e.getClickedInventory().getName().contains(ChatColor.RED.toString())
				&& e.getClickedInventory().getName().contains("Ender Chest of ")){
			
			String targetName = e.getClickedInventory().getName().replace(ChatColor.RED + "Ender Chest of ", "");
			Player target = Bukkit.getPlayer(targetName);
			
			if (e.getAction() == InventoryAction.PICKUP_ALL){
				ItemStack pickedUpStack = e.getCurrentItem();
				
				if (pickedUpStack == null){
					e.setCancelled(true);
				}
				else{
					int id = e.getSlot();
					if (id != -1){
						target.getEnderChest().clear(id);
						//Bukkit.getServer().broadcastMessage("removed");
					}
				}
			}
			else if (e.getAction() == InventoryAction.PLACE_ALL){
				int id = e.getSlot();
				if (target.getEnderChest().getItem(id) == null){
					// basic enderchest slots
					target.getEnderChest().setItem(id, e.getCursor());
					//Bukkit.getServer().broadcastMessage("added " + e.getCursor().getType() + " to " + id);
				}
				else{
					e.setCancelled(true); // trying to place an item into a space that's already occupied
				}
				
			}
			else{
				e.setCancelled(true); // prevent weird actions (shift clicking, right clicking etc)
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("isee")){
			if (!(sender instanceof Player)){
				sender.sendMessage("Must be player to use this command.");
				return true;
			}
			if (!sender.isOp()){
				sender.sendMessage("Must be OP to use this command.");
				return true;
			}
			if (args.length <= 0){
				sender.sendMessage("Must specify player");
				return true;
			}
			// check if the player exists
			if ( Bukkit.getServer().getPlayer(args[0]) == null){
				sender.sendMessage("The player \"" + args[0] + "\" wasn't found.");
				return true;
			}
			
			Player player = (Player) sender;
			Player target = Bukkit.getServer().getPlayer(args[0]);
			
			// setup inventory window
			Inventory inv = Bukkit.createInventory(null, 45, ChatColor.RED + "Inventory of " + target.getName());
			inv.setItem(0, new ItemStack(Material.AIR));
			
			// loop through the players inventory getting the item stacks
			int count = 0;
			for(ItemStack is : target.getInventory()){
				if (is == null){ // check if air/nothing
					count++;
					continue;
				}
				inv.setItem(count, is); // add this item to the fake inventory
				//player.sendMessage(count + " " + is.getType().name());
				count++;
			}
			inv.setItem(36, target.getInventory().getHelmet());
			inv.setItem(37, target.getInventory().getChestplate());
			inv.setItem(38, target.getInventory().getLeggings());
			inv.setItem(39, target.getInventory().getBoots());
			player.openInventory(inv); // open the newly created inventory for the user
			return true;
		}
		else if (cmd.getName().equalsIgnoreCase("esee")){
			if (!(sender instanceof Player)){
				sender.sendMessage("Must be player to use this command.");
				return true;
			}
			if (!sender.isOp()){
				sender.sendMessage("Must be OP to use this command.");
				return true;
			}
			if (args.length <= 0){
				sender.sendMessage("Must specify player");
				return true;
			}
			// check if the player exists
			if ( Bukkit.getServer().getPlayer(args[0]) == null){
				sender.sendMessage("The player \"" + args[0] + "\" doesn't exist.");
				return true;
			}
			Player player = (Player) sender;
			Player target = Bukkit.getServer().getPlayer(args[0]);
			
			Inventory inv = Bukkit.createInventory(null, 27, ChatColor.RED + "Ender Chest of " + target.getName());
			inv.setItem(0, new ItemStack(Material.AIR));
			
			// loop through the players inventory getting the item stacks
			int count = 0;
			for(ItemStack is : target.getEnderChest()){
				if (is == null){ // check if air/nothing
					count++;
					continue;
				}
				inv.setItem(count, is); // add this item to the fake inventory
				count++;
			}
			player.openInventory(inv); // open the newly created inventory for the user
			return true;
		}
		return false;
	}
	
}
