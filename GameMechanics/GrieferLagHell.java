package Evolution.GameMechanics;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.potion.PotionEffectType;

public class GrieferLagHell implements Listener, CommandExecutor{

	private ArrayList<String> grieferNames;
	private ArrayList<String> grieferUUIDs;
	private ArrayList<String> grieferIPs;
	public ArrayList<Player> currentlyLaggingPlayers;
	
	public GrieferLagHell(){
		grieferNames = new ArrayList<String>();
		grieferNames.add("Diano");
		grieferNames.add("Rubiu5");
		grieferUUIDs = new ArrayList<String>();
		grieferUUIDs.add("9cb7274d-02f9-451b-9ae0-6645a1d31884"); // diano
		grieferUUIDs.add("ab95dc51-6acd-48f2-a88e-e82d44011cf6"); // rubiu5
		grieferIPs = new ArrayList<String>();
		grieferIPs.add("187.188.10.214"); // diano/rubiu5's ip
		currentlyLaggingPlayers = new ArrayList<>();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if (cmd.getName().equalsIgnoreCase("lag")){
			if (!sender.isOp()){
				sender.sendMessage("Must be OP to use this command.");
				return true;
			}
			if (args.length < 1){
				sender.sendMessage("Must specify a player to lag.");
				return true;
			}
			Player player = Bukkit.getPlayer(args[0]);
			@SuppressWarnings("unused")
			GrieferLagHellRunnable runnable = new GrieferLagHellRunnable(player);
			
			sender.sendMessage("Lagging " + player.getName() + " to death");
			return true;
		}
		
		return false;
	}
	
	@EventHandler
	public void onGrieferJoin(PlayerJoinEvent e){
		if (grieferNames.contains(e.getPlayer().getName()) == true
				|| grieferUUIDs.contains(e.getPlayer().getUniqueId().toString())){
			
			// log it
			String logText = "";
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			logText += dateFormat.format(date) + " "; // 2013/10/15 16:16:39
			logText += e.getPlayer().getName() + " (" + e.getPlayer().getUniqueId().toString() + ") logged in";
			
			writeToLog("GrieferLagLog.txt", logText);
			
			// create the runnable that gives the hacker lag hell
			@SuppressWarnings("unused")
			GrieferLagHellRunnable runnable = new GrieferLagHellRunnable(e.getPlayer());
		}
	}
	
	@EventHandler
	public void onGrieferQuit(PlayerQuitEvent e){
		if (grieferNames.contains(e.getPlayer().getName()) == true
				|| grieferUUIDs.contains(e.getPlayer().getUniqueId().toString())){
			
			// log it
			String logText = "";
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			logText += dateFormat.format(date) + " "; // 2013/10/15 16:16:39
			logText += e.getPlayer().getName() + " (" + e.getPlayer().getUniqueId().toString() + ") quit";
			
			writeToLog("GrieferLagLog.txt", logText);
		}
		
		if (currentlyLaggingPlayers.contains(e.getPlayer())){
			e.getPlayer().setGameMode(GameMode.SURVIVAL); // survival for when they log in next
			e.getPlayer().removePotionEffect(PotionEffectType.JUMP);
			e.getPlayer().setWalkSpeed(0.2F); // reset back to normal walking speed
			currentlyLaggingPlayers.remove(e.getPlayer());
		}
	}
	
	@EventHandler
	public void onServerGrieferPingEvent(ServerListPingEvent e){
		String pingIP = e.getAddress().toString();
		pingIP = pingIP.substring(1, pingIP.length());
		
		if (grieferIPs != null
				&& grieferIPs.contains(pingIP)){
			// log it
			String logText = "";
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			logText += dateFormat.format(date) + " "; // 2013/10/15 16:16:39
			logText += pingIP + " pinged the server.";
			
			writeToLog("GrieferLagLog.txt", logText);
		}
	}
	
	public void writeToLog(String logFileName, String logText){
		// Try writing the logText to the file. Must be in try catch block to handle the IOException if one is called.
		try{
			FileWriter fileWriter = new FileWriter(logFileName, true); // open the file name in logFileName for writing in append mode
			BufferedWriter bw = new BufferedWriter(fileWriter); // create new buffered writer using the fileWriter object
			bw.write(logText);
			bw.newLine();
			bw.flush();
			bw.close();
		} catch(IOException ex){
			System.out.println("Error when writing " + logFileName+ ": " + ex.getMessage());
		}
	}

}
