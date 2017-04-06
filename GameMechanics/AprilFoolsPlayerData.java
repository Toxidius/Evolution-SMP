package Evolution.GameMechanics;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class AprilFoolsPlayerData {
	
	private Player player;
	private ArrayList<Location> locationsOfPlayerWhenOpenChest;
	private long lastTrollEvent;
	
	public AprilFoolsPlayerData(Player player){
		this.player = player;
		locationsOfPlayerWhenOpenChest = new ArrayList<>();
		lastTrollEvent = 0;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public ArrayList<Location> getLocations(){
		return locationsOfPlayerWhenOpenChest;
	}
	
	public int getNumberOfLocations(){
		return locationsOfPlayerWhenOpenChest.size();
	}
	
	public Location getFrequentlyUsedLocation(){
		HashMap<Location, Integer> occurances = new HashMap<>();
		
		for (Location loc1 : locationsOfPlayerWhenOpenChest){
			occurances.put(loc1, new Integer(0));
			
			for (Location loc2 : locationsOfPlayerWhenOpenChest){
				if (loc1.distance(loc2) <= 5){
					occurances.replace(loc1, (occurances.get(loc1).intValue()+1) );
				}
			}
		}
		
		
		int highestOccurances = 0;
		Location highestOccurancesLocation = null;
		for (Location loc1 : occurances.keySet()){
			if (occurances.get(loc1).intValue() > highestOccurances){
				highestOccurances = occurances.get(loc1).intValue();
				highestOccurancesLocation = loc1;
			}
		}
		
		return highestOccurancesLocation;
	}
	
	public long getLastTrollEvent(){
		return lastTrollEvent;
	}
	
	public void setLastTrollEvent(long lastTrollEvent){
		this.lastTrollEvent = lastTrollEvent;
	}
	
	public void addLocation(Location location){
		locationsOfPlayerWhenOpenChest.add(location);
	}
	
	public void clearLocations(){
		locationsOfPlayerWhenOpenChest = new ArrayList<Location>();
	}

}
