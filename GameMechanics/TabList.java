package Evolution.GameMechanics;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import Evolution.Main.Core;
import net.minecraft.server.v1_11_R1.IChatBaseComponent;
import net.minecraft.server.v1_11_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_11_R1.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_11_R1.PlayerConnection;

public class TabList implements Listener, Runnable{

	private long[] previousTimes;
	private int call;
	private double tps;
	
	public TabList(){
		previousTimes = new long[7];
		previousTimes[1] = System.currentTimeMillis();
		call = 2;
		tps = (double)0;
		
		// send all currently online players the header and footer
		for (Player player : Bukkit.getOnlinePlayers()){
			if (player.isOnline()){
				sendPlayerTabList(player);
			}
		}
		
		// start the runnable that executes every 10 seconds sending the latest tps
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 200, 200); // 10 second interval (first call starts 10 seconds from now)
	}
	
	@Override
	public void run() {
		// check if the previousTimes is full of previous times (60 full seconds have passed)
		if (previousTimes[1] == 0
				|| previousTimes[6] == 0){
			
			// not full yet, so just fill in the current slot with the current time
			previousTimes[call] = System.currentTimeMillis();
		}
		else{
			// update tps
			long time = System.currentTimeMillis();
			double difference = (time - previousTimes[call]) / 1000.0;
			tps = (1200.0 / difference); // baseline for 20 tps is 60000ms have passed
			
			if (tps > 20.0){
				tps = (double)20.0;
			}
			
			previousTimes[call] = time;
		}
		
		
		// send all players the new tps in header/footer
		for (Player player : Bukkit.getOnlinePlayers()){
			if (player.isOnline()){
				sendPlayerTabList(player);
			}
		}
		call++; // increment call count
		if (call > 6){
			call = 1;
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		sendPlayerTabList(e.getPlayer());
	}
	
	public void sendPlayerTabList(Player player){
		int ping = getPing(player);
		String pingText = "";
		if (ping < 0){
			pingText = "Loading...";
		}
		else if (ping > 5000){
			pingText = "Loading...";
		}
		else{
			pingText = "" + ping;
		}
		
		String tpsText = "";
		if (tps <= 0.01){
			tpsText = "Loading... ";
		}
		else{
			tpsText = "" + round(tps);
		}
		
		String headerText = ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Evolution SMP";
		String footerText = ChatColor.GREEN + "TPS: " + tpsText + '\n' + "Ping: " + pingText + "ms";
		
		CraftPlayer craftPlayer = (CraftPlayer) player;
		PlayerConnection connection = craftPlayer.getHandle().playerConnection;
		IChatBaseComponent top = ChatSerializer.a("{\"text\": \"" + headerText + "\"}");
		IChatBaseComponent bottom = ChatSerializer.a("{\"text\": \"" + footerText + "\"}");
		PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
		
		try{
			// header
			Field footerField = packet.getClass().getDeclaredField("a"); // get the header field
			footerField.setAccessible(true);
			footerField.set(packet, top);
			footerField.setAccessible(false);
			
			// footer
			footerField = packet.getClass().getDeclaredField("b"); // get the footer field ("a") is the header
			footerField.setAccessible(true);
			footerField.set(packet, bottom);
			footerField.setAccessible(false);
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		connection.sendPacket(packet);
	}
	
	public int getPing(Player player){
		try {
			Class<?> CPClass;
			String serverName = Bukkit.getServer().getClass().getPackage().getName();
			String serverVersion = serverName.substring(serverName.lastIndexOf(".") + 1, serverName.length());
			CPClass = Class.forName("org.bukkit.craftbukkit." + serverVersion + ".entity.CraftPlayer");
			Object CraftPlayer = CPClass.cast(player);

			Method getHandle = CraftPlayer.getClass().getMethod("getHandle", new Class[0]);
			Object EntityPlayer = getHandle.invoke(CraftPlayer, new Object[0]);

			Field ping = EntityPlayer.getClass().getDeclaredField("ping");

			return ping.getInt(EntityPlayer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public double round(double input){
		input = Math.round( (input*100.0) )/100.0; // round to 0.00
		return input;
	}
}