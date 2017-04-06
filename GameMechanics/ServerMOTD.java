package Evolution.GameMechanics;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import Evolution.Main.ConfigManager;

public class ServerMOTD implements Listener, CommandExecutor{
	
	private ConfigManager configManager;
	private String MOTD;
	
	public ServerMOTD(){
		configManager = new ConfigManager("EvolutionSMPConfig");
		
		// load config message
		MOTD = configManager.getString("MOTD");
		if (MOTD == null){
			configManager.set("MOTD", "Default");
			configManager.save();
		}
	}
	
	@EventHandler
	public void onServerPingEvent(ServerListPingEvent e){
		int numOnline = Bukkit.getOnlinePlayers().size();
		if (numOnline == 0){
			e.setMotd(ChatColor.DARK_PURPLE + "Evolution SMP" + ChatColor.WHITE + " - " + ChatColor.WHITE + MOTD);
		}
		else{
			if (MOTD.length() > 35){
				e.setMotd(ChatColor.DARK_PURPLE + "Evolution SMP" + ChatColor.WHITE + " - " + ChatColor.WHITE + MOTD + "   " 
						+ ChatColor.AQUA + ChatColor.UNDERLINE + numOnline + ChatColor.RESET + ChatColor.AQUA + " Currently Online!");
			}
			else{
				e.setMotd(ChatColor.DARK_PURPLE + "Evolution SMP" + ChatColor.WHITE + " - " + ChatColor.WHITE + MOTD + '\n' 
					+ ChatColor.AQUA + ChatColor.UNDERLINE + numOnline + ChatColor.RESET + ChatColor.AQUA + " Currently Online!");
			}
			
		}
		String pingIP = e.getAddress().toString();
		pingIP = pingIP.substring(1, pingIP.length());
		System.out.println("Ping from " + pingIP);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if (cmd.getName().equalsIgnoreCase("setmotd")){
			if (!sender.isOp()){
				sender.sendMessage("Must be OP to use this command.");
				return true;
			}
			if (args.length < 1){
				sender.sendMessage("Must specify a message of the day.");
				return true;
			}
			MOTD = "";
			for (int i = 0; i < args.length; i++){
				MOTD += args[i] + " ";
			}
			configManager.set("MOTD", MOTD);
			configManager.save();
			sender.sendMessage("MOTD set to \"" + MOTD + "\".");
			return true;
		}
		
		return false;
	}

}
