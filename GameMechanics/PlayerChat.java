package Evolution.GameMechanics;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import Evolution.Main.ConfigManager;
import Evolution.Main.Core;

public class PlayerChat implements Listener, CommandExecutor{
	
	private int dailyColor;
	private ConfigManager configManager;
	
	public PlayerChat(){
		dailyColor = 0;
		configManager = new ConfigManager("EvolutionSMPConfig");
		
		// load chat color
		int temp = configManager.getInt("dailyColor");
		if (temp == 0){
			dailyColor = 2;
			configManager.set("dailyColor", dailyColor);
			configManager.save();
		}
		else{
			dailyColor = temp;
		}
	}
	
	public String getColor(){
		if (dailyColor == -1){
			return randomColor();
		}
		ChatColor[] colors = ChatColor.values();
		String string = "" + colors[dailyColor];
		return string;
	}
	
	public String randomColor(){
		ChatColor[] colors = ChatColor.values();
		int r = Core.r.nextInt(22); // number 0 to 21
		String string = "" + colors[r];
		return string;
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e){
		e.setCancelled(true);
		Bukkit.getServer().broadcastMessage(getColor() + "" + e.getPlayer().getName() + ChatColor.RESET + "" + ChatColor.WHITE + " " + e.getMessage());
		
		/*
		String message = e.getMessage();
		if (message.contains("toxbot") || message.contains("Toxbot") || message.contains("ToxBot")){
			// toxbot commands here
			if (message.contains("size")){
				chatBot.basicMsg("I have witnessed " + totalWordsSaid + " words from " + totalMessagesSaid + " messages. This means there is an average of " + (Math.round(( (float)totalWordsSaid/totalMessagesSaid ) * 100.0) / 100.0) + " words per message.");
				chatBot.basicMsg("I know all...");
				return;
			}
			else{
				return; // unknown toxbot command; don't allow this to be logged
			}
		}
		*/
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if (cmd.getName().equalsIgnoreCase("setcolor")){
			if (!sender.isOp()){
				sender.sendMessage("Must be OP to use this command.");
				return true;
			}
			if (args.length <= 0){
				sender.sendMessage("Must specify a color from 1 to 21.");
				return true;
			}
			dailyColor = Integer.valueOf(args[0]);
			configManager.set("dailyColor", dailyColor);
			configManager.save();
			sender.sendMessage(getColor() + "Color set.");
			return true;
		}
		else if (cmd.getName().equalsIgnoreCase("listcolors")){
			ChatColor[] colors = ChatColor.values();
			String string;
			for (int i = 0; i < 21; i++){
				string = "" + colors[i] + "Color " + i;
				sender.sendMessage(string);
			}
			return true;
		}
		
		return false;
	}

}
