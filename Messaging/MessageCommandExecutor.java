package Evolution.Messaging;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import Evolution.Main.Core;

public class MessageCommandExecutor implements CommandExecutor{
	
	public MessageCommandExecutor(){
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if (cmd.getName().equalsIgnoreCase("msg")){
			if ( !(sender instanceof Player)){
				sender.sendMessage(ChatColor.RED + "Must be a player to use this command!");
				return true;
			}
			else if (args.length < 2){
				sender.sendMessage(ChatColor.RED + "Invalid Agruments! Ex: /msg Notch Hi!");
				return true;
			}
			
			String message = "";
			Player toPlayer, fromPlayer;
			fromPlayer = (Player) sender;
			toPlayer = Bukkit.getPlayer(args[0]);
			
			if (toPlayer == null){
				sender.sendMessage(ChatColor.RED + args[0] + " doesn't seem to be online!");
				return true;
			}
			else{
				for (int i = 1; i < args.length; i++){
					message += args[i] + " ";
				}
				toPlayer.sendMessage(ChatColor.DARK_PURPLE + "" + fromPlayer.getName() + "" + ChatColor.LIGHT_PURPLE + " -> " + ChatColor.DARK_PURPLE 
						+ toPlayer.getName() + ChatColor.WHITE + ": " + message);
				fromPlayer.sendMessage(ChatColor.DARK_PURPLE + fromPlayer.getName() + "" + ChatColor.LIGHT_PURPLE + " -> " + ChatColor.DARK_PURPLE 
						+ toPlayer.getName() + ChatColor.WHITE + ": " + message);
				
				toPlayer.setMetadata("messageRespondTo", new FixedMetadataValue(Core.thisPlugin, new String(fromPlayer.getName()))); // set the respond to player name
				fromPlayer.setMetadata("messageRespondTo", new FixedMetadataValue(Core.thisPlugin, new String(toPlayer.getName()))); // set the respond to player name
			}
			return true;
		}
		else if (cmd.getName().equalsIgnoreCase("r")){
			if (!(sender instanceof Player)){
				sender.sendMessage(ChatColor.RED + "Must be a player to use this command!");
				return true;
			}
			else if (args.length < 1){
				sender.sendMessage(ChatColor.RED + "Invalid Agruments! Ex: /r Hi!");
				return true;
			}
			
			String message = "";
			Player toPlayer, fromPlayer;
			fromPlayer = (Player) sender;
			if (fromPlayer.hasMetadata("messageRespondTo") == false){
				sender.sendMessage(ChatColor.RED + "You have nobody to respond to!");
				return true;
			}
			toPlayer = Bukkit.getPlayer(fromPlayer.getMetadata("messageRespondTo").get(0).asString());
			
			if (toPlayer == null){
				sender.sendMessage(ChatColor.RED + args[0] + " doesn't seem to be online!");
				return true;
			}
			else{
				for (int i = 0; i < args.length; i++){
					message += args[i] + " ";
				}
				toPlayer.sendMessage(ChatColor.DARK_PURPLE + "" + fromPlayer.getName() + "" + ChatColor.LIGHT_PURPLE + " -> " + ChatColor.DARK_PURPLE 
						+ toPlayer.getName() + ChatColor.WHITE + ": " + message);
				fromPlayer.sendMessage(ChatColor.DARK_PURPLE + fromPlayer.getName() + "" + ChatColor.LIGHT_PURPLE + " -> " + ChatColor.DARK_PURPLE 
						+ toPlayer.getName() + ChatColor.WHITE + ": " + message);
				
				toPlayer.setMetadata("messageRespondTo", new FixedMetadataValue(Core.thisPlugin, new String(fromPlayer.getName()))); // set the respond to player name
			}
			return true;
		}
		return false;
	}
		

}
