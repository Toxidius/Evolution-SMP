package Evolution.GameMechanics;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import Evolution.Main.Core;

public class SantaPresents implements Listener, Runnable{
	
	private int time;
	@SuppressWarnings("unused")
	private int id;
	private Player lastRandomPlayer;
	
	public SantaPresents(){
		time = 0;
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 20, 20); // executes every second
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerRightClickPresent(PlayerInteractEvent e){
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK
				&& e.getHand() == EquipmentSlot.HAND
				&& e.getClickedBlock().getType() == Material.SKULL
				&& ((Skull)e.getClickedBlock().getState()).hasOwner()
				&& ((Skull)e.getClickedBlock().getState()).getOwner().equals("MHF_Present2")
				&& e.getClickedBlock().getRelative(BlockFace.DOWN).getType() == Material.SNOW_BLOCK){
			// player right clicked a present
			e.getClickedBlock().setType(Material.AIR);
			Location dropLocation = e.getClickedBlock().getLocation().add(0.5, 0.0, 0.5);
			
			ItemStack present;
			switch (Core.r.nextInt(10)){
				case 0:
					present = Core.snowballGun.getRelic();
					break;
				case 1:
					present = Core.christmasParty.getRelic();
					break;
				case 2:
					present = Core.mistleToe.getRelic();
					break;
				case 3:
					present = Core.snowTime.getRelic();
					break;
				case 4:
					present = Core.enchantedPartcles.getRelic();
					break;
				case 5:
					present = new ItemStack(Material.COAL, Core.r.nextInt(10)+10);
					break;
				case 6:
					present = new ItemStack(Material.EMERALD, Core.r.nextInt(6)+10);
					break;
				case 7:
					present = new ItemStack(Material.EMERALD_BLOCK, Core.r.nextInt(2)+1);
					break;
				case 8:
					present = new ItemStack(Material.COAL_BLOCK, Core.r.nextInt(2)+2);
					break;
				case 9:
					present = new ItemStack(Material.SLIME_BALL, Core.r.nextInt(15)+10);
					break;
				default:
					present = Core.snowballGun.getRelic();
					break;
			}
			dropLocation.getWorld().dropItem(dropLocation, present);
			
			Firework firework;
			FireworkMeta meta;
			FireworkEffect effect;
			firework = (Firework) e.getClickedBlock().getWorld().spawnEntity(e.getClickedBlock().getLocation().clone().add(0.5, 0.0, 0.5), EntityType.FIREWORK);
			meta = firework.getFireworkMeta();
			meta.clearEffects();
			meta.setPower(0);
			effect = FireworkEffect.builder().trail(true).with(Type.BALL_LARGE).withColor(Color.RED).build();
			meta.addEffect(effect);
			firework.setFireworkMeta(meta);
			
			// remove the nearby snow too
			Location temp;
			Location center = e.getClickedBlock().getLocation();
			int radius = 3;
			int startX = center.getBlockX()-radius;
			int startY = center.getBlockY()-1;
			int startZ = center.getBlockZ()-radius;
			int endX = center.getBlockX()+radius;
			int endY = center.getBlockY()+1;
			int endZ = center.getBlockZ()+radius;
			for (int y = startY; y <= endY; y++){
				for (int x = startX; x <= endX; x++){
					for (int z = startZ; z <= endZ; z++){
						temp = new Location(center.getWorld(), x, y, z);
						if (temp.getBlock().getType() == Material.SNOW
								|| temp.getBlock().getType() == Material.SNOW_BLOCK){
							temp.getBlock().setType(Material.AIR);
						}
					}
				}
			}
			
			Bukkit.getServer().broadcastMessage(ChatColor.GOLD + e.getPlayer().getName() + " found a christmas present!");
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerBreakPresentBlock(BlockBreakEvent e){
		if (e.getBlock().getType() == Material.SKULL
				&& ((Skull)e.getBlock().getState()).hasOwner()
				&& ((Skull)e.getBlock().getState()).getOwner().equals("MHF_Present2")
				&& e.getBlock().getRelative(BlockFace.DOWN).getType() == Material.SNOW_BLOCK){
			// remove the snow below the present block
			Location temp;
			Location center = e.getBlock().getLocation();
			int radius = 3;
			int startX = center.getBlockX()-radius;
			int startY = center.getBlockY()-1;
			int startZ = center.getBlockZ()-radius;
			int endX = center.getBlockX()+radius;
			int endY = center.getBlockY()+1;
			int endZ = center.getBlockZ()+radius;
			for (int y = startY; y <= endY; y++){
				for (int x = startX; x <= endX; x++){
					for (int z = startZ; z <= endZ; z++){
						temp = new Location(center.getWorld(), x, y, z);
						if (temp.getBlock().getType() == Material.SNOW
								|| temp.getBlock().getType() == Material.SNOW_BLOCK){
							temp.getBlock().setType(Material.AIR);
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		time++;
		int dropTime = (int) Math.round((30.0*60.0) / Bukkit.getOnlinePlayers().size()); // every 30 minutes a present spawns
		if (time >= dropTime
				&& Bukkit.getOnlinePlayers().size() > 0){
			// spawn a present somewhere
			Location spawnLocation = getRandomSpawnLocation();
			if (spawnLocation == null){
				time = 0; // no valid location found to spawn the present
				return;
			}
			spawnLocation.getBlock().setType(Material.SNOW_BLOCK);
			snowLayers(spawnLocation);
			
			Block skullBlock = spawnLocation.getBlock().getRelative(BlockFace.UP);
			skullBlock.setType(Material.SKULL);
			skullBlock.setData((byte)1);
			Skull skull = (Skull) skullBlock.getState();
			skull.setSkullType(SkullType.PLAYER);
			skull.setOwner("MHF_Present2");
			skull.update();
			
			Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "Santa has dropped off a present near " + lastRandomPlayer.getName() + "!");
			
			// spawn effect
			@SuppressWarnings("unused")
			SantaPresentsEffect effect = new SantaPresentsEffect(skullBlock.getLocation().add(0.0, 1.0, 0.0));
			
			// remove 5 mins later if nobody got it
			Bukkit.getScheduler().scheduleSyncDelayedTask(Core.thisPlugin, new Runnable(){
				@Override
				public void run() {
					if (skullBlock.getType() == Material.SKULL){
						skullBlock.setType(Material.AIR); // remove the skull after 5 mins if nobody gets it
						
						// remove the nearby snow too
						Location temp;
						Location center = skullBlock.getLocation();
						int radius = 3;
						int startX = center.getBlockX()-radius;
						int startY = center.getBlockY()-1;
						int startZ = center.getBlockZ()-radius;
						int endX = center.getBlockX()+radius;
						int endY = center.getBlockY()+1;
						int endZ = center.getBlockZ()+radius;
						for (int y = startY; y <= endY; y++){
							for (int x = startX; x <= endX; x++){
								for (int z = startZ; z <= endZ; z++){
									temp = new Location(center.getWorld(), x, y, z);
									if (temp.getBlock().getType() == Material.SNOW
											|| temp.getBlock().getType() == Material.SNOW_BLOCK){
										temp.getBlock().setType(Material.AIR);
									}
								}
							}
						}
					}
				}
			}, 5*60*20); // 5 mins later
			time = 0;
		}
		else if (time >= dropTime
				&& Bukkit.getOnlinePlayers().size() == 0){
			time = 0;
		}
	}
	
	@SuppressWarnings("deprecation")
	public void snowLayers(Location center){
		Location temp;
		int radius = 3;
		int startX = center.getBlockX()-radius;
		int startY = center.getBlockY();
		int startZ = center.getBlockZ()-radius;
		int endX = center.getBlockX()+radius;
		int endY = center.getBlockY();
		int endZ = center.getBlockZ()+radius;
		for (int y = startY; y <= endY; y++){
			for (int x = startX; x <= endX; x++){
				for (int z = startZ; z <= endZ; z++){
					temp = new Location(center.getWorld(), x, y, z);
					if ( (temp.getBlock().getType() == Material.AIR || temp.getBlock().getType() == Material.LONG_GRASS)
							&& center.distance(temp) <= radius){
						temp.getBlock().setType(Material.SNOW, false); // no block update
						int height; // 0 to 7
						height = (int) Math.round((7.0 * radius/center.distance(temp)) / radius);
						
						// random increase or decrease
						if (height <= 6 && height >= 1){
							height += Core.r.nextInt(3) - 1; // -1 to 1
						}
						
						temp.getBlock().setData((byte)height, false); // no block update
					}
				}
			}
		}
	}
	
	public Location getRandomSpawnLocation(){
		// get random player in overworld to spawn it near
		Player randomPlayer = getRandomPlayer();
		if (randomPlayer == null){
			return null; // no valid player found online
		}
		lastRandomPlayer = randomPlayer;
		
		int xOff = Core.r.nextInt(256)-128; // roughly up to 8 chunks away
		int zOff = Core.r.nextInt(256)-128; // roughly up to 8 chunks away
		
		Location spawnLocation = randomPlayer.getLocation().add(xOff, 0.0, zOff);
		spawnLocation = spawnLocation.getWorld().getHighestBlockAt(spawnLocation).getLocation();
		if (spawnLocation.getBlock().getRelative(BlockFace.DOWN).getType().isSolid() == false){
			return spawnLocation.getBlock().getRelative(BlockFace.DOWN).getLocation();
		}
		return spawnLocation;
	}
	
	public Player getRandomPlayer(){
		if (Bukkit.getWorlds().get(0).getPlayers().size() == 0){
			return null; // nobody online
		}
		
		ArrayList<Player> playersToChoose = new ArrayList<>();
		for (Player player : Bukkit.getWorlds().get(0).getPlayers()){
			if (player.getPlayerListName().contains("(AFK)") == false){
				playersToChoose.add(player);
			}
		}
		
		if (playersToChoose.size() == 0){
			return null;
		}
		
		Player choosenPlayer = null;
		Player tempPlayer = null;
		for (int i = 0; i < 10; i++){
			// try 10 times to find a good player that wasn't previously choosen
			tempPlayer = playersToChoose.get( Core.r.nextInt(playersToChoose.size()) );
			if (tempPlayer.equals(lastRandomPlayer) == false){
				choosenPlayer = tempPlayer;
				break;
			}
			choosenPlayer = tempPlayer; // this player was previously choosen, but no other applicable players are onlines
		}
		
		if (choosenPlayer != null){
			return choosenPlayer;
		}
		else{
			return null;
		}
	}

}
