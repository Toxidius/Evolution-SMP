package Evolution.GameMechanics;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Evolution.Main.Core;

public class GrieferLagHellRunnable implements Runnable{

	private Player player;
	private World world;
	private Material lastMaterial;
	private int calls;
	private int id;
	
	public GrieferLagHellRunnable(Player player) {
		this.player = player;
		world = Bukkit.getWorlds().get(0);
		lastMaterial = Material.AIR;
		calls = 0;
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 40, 1); // run every tick after 2 seconds initial delay
		Core.grieferLagHell.currentlyLaggingPlayers.add(player);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		if (player == null
				|| player.isOnline() == false){
			Bukkit.getScheduler().cancelTask(id);
			return;
		}
		
		if (calls == 0){
			player.setHealth(20.0);
			player.setFallDistance(0F);
			player.teleport(new Location(world, 0.5, 256, 0.5));
			player.setFallDistance(0F);
			player.setGameMode(GameMode.ADVENTURE);
			player.setWalkSpeed(0.0001F); // can't walk
			player.removePotionEffect(PotionEffectType.JUMP);
			player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 1000000000, -10)); // can't jump
			Location belowPlayer = new Location(world, 0, 255, 0);
			belowPlayer.getBlock().setType(Material.OBSIDIAN);
		}
		else{
			Location temp;
			
			/*
			WorldServer server = ((CraftWorld)player.getLocation().getWorld()).getHandle();
			net.minecraft.server.v1_11_R1.World otherWorld = ((CraftWorld)player.getLocation().getWorld()).getHandle();
			EntityEnderDragon dragon = new EntityEnderDragon((net.minecraft.server.v1_11_R1.World) otherWorld);
			
			temp = new Location(world, player.getLocation().getX()+Math.random()*5-10, 255, player.getLocation().getX()+Math.random()*5-10);
			dragon.setLocation(temp.getX(), temp.getY(), temp.getZ(), 0, 0);
			dragon.setNoGravity(true);
			
			PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(dragon);
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
			*/
			
			if (calls%70 == 0){
				Material material;
				if (lastMaterial == Material.AIR){
					material = Material.OBSIDIAN;
				}
				else{
					material = Material.AIR;
				}
				
				for (int x = -5; x < 20; x++){
					for (int z = -5; z < 20; z++){
						temp = new Location(world, x, 255, z);
						player.sendBlockChange(temp, material, (byte)0);
					}
				}
				
				lastMaterial = material;
				
				// make sure they're still in the lag zone
				if (player.getLocation().getBlockX() != 0
						|| player.getLocation().getBlockZ() != 0){
					player.setFallDistance(0F);
					player.teleport(new Location(world, 0.5, 256, 0.5));
					player.setFallDistance(0F);
				}
			}
		}
		calls++;
	}

}
