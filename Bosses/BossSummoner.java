package Evolution.Bosses;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import Evolution.Main.Core;

public class BossSummoner implements Listener{
	
	public BossSummoner(){
	}
	
	public enum BossType{
		Zombie, Pumpkin, Spider, PolarBear, Creeper;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onHeadPlaceOnDiamond(BlockPlaceEvent e){
		if (e.isCancelled())
			return;
		if (e.getBlock().getType() == Material.SKULL){
			if (e.getBlock().getRelative(BlockFace.DOWN).getType() == Material.EMERALD_BLOCK){
				// check for other emerald blocks
				Block headBlock = e.getBlock();
				int numEligibleSummonBlocks = 0;
				if (headBlock.getLocation().add(0.0, -1.0, 0.0).getBlock().getType() == Material.EMERALD_BLOCK){
					numEligibleSummonBlocks++;
				}
				if (headBlock.getLocation().add(0.0, -2.0, 0.0).getBlock().getType() == Material.EMERALD_BLOCK){
					numEligibleSummonBlocks++;
				}
				if (headBlock.getLocation().add(-1.0, -1.0, 0.0).getBlock().getType() == Material.EMERALD_BLOCK){
					numEligibleSummonBlocks++;
				}
				if (headBlock.getLocation().add(1.0, -1.0, 0.0).getBlock().getType() == Material.EMERALD_BLOCK){
					numEligibleSummonBlocks++;
				}
				if (headBlock.getLocation().add(0.0, -1.0, -1.0).getBlock().getType() == Material.EMERALD_BLOCK){
					numEligibleSummonBlocks++;
				}		
				if (headBlock.getLocation().add(0.0, -1.0, 1.0).getBlock().getType() == Material.EMERALD_BLOCK){
					numEligibleSummonBlocks++;
				}
				
				// test for requirements of emerald blocks
				if (numEligibleSummonBlocks < 4){
					return; // not enough emerald blocks to summon the boss
				}
				
				// boss summon squence
				Skull skull = (Skull) e.getBlock().getState();
				SkullType skullType = skull.getSkullType();
				Location spawnLocation = e.getBlock().getLocation().add(0.5, -2.0, 0.5);
				
				if (skullType == SkullType.ZOMBIE){
					removeBlocks(e.getBlock()); // remove summon blocks
					@SuppressWarnings("unused")
					SummonEffect effect = new SummonEffect(spawnLocation, BossType.Zombie); // start the summon effect
				}
				else if (skullType == SkullType.SKELETON){
					removeBlocks(e.getBlock()); // remove summon blocks
					@SuppressWarnings("unused")
					SummonEffect effect = new SummonEffect(spawnLocation, BossType.Pumpkin); // start the summon effect
				}
				else if (skullType == SkullType.CREEPER){
					removeBlocks(e.getBlock()); // remove summon blocks
					@SuppressWarnings("unused")
					SummonEffect effect = new SummonEffect(spawnLocation, BossType.Creeper); // start the summon effect
				}
				else if (skullType == SkullType.PLAYER){
					String playerName = skull.getOwner();
					if (playerName.equals("polarbear9")){
						removeBlocks(e.getBlock()); // remove summon blocks
						@SuppressWarnings("unused")
						SummonEffect effect = new SummonEffect(spawnLocation, BossType.PolarBear); // start the summon effect
					}
					else if (playerName.equals("MHF_Spider")){
						removeBlocks(e.getBlock()); // remove summon blocks
						@SuppressWarnings("unused")
						SummonEffect effect = new SummonEffect(spawnLocation, BossType.Spider); // start the summon effect
					}
				}
			}
		}
	}
	
	public void spawnBoss(BossType bossType, Location spawnLocation){
		if (bossType == BossType.Zombie){
			Core.zombieBoss.createBoss(spawnLocation); // summon zombie boss
			spawnLocation.getWorld().playSound(spawnLocation, Sound.ENTITY_ZOMBIE_AMBIENT, 1000F, 0F);
		}
		else if (bossType == BossType.Pumpkin){
			Core.pumpkinBoss.createBoss(spawnLocation); // summon pumpkin boss
			spawnLocation.getWorld().playSound(spawnLocation, Sound.ENTITY_SKELETON_HORSE_AMBIENT, 1000F, 0F);
		}
		else if (bossType == BossType.Spider){
			Core.spiderBoss.createBoss(spawnLocation); // summon spider boss
			spawnLocation.getWorld().playSound(spawnLocation, Sound.ENTITY_SPIDER_AMBIENT, 1000F, 0F);
		}
		else if (bossType == BossType.PolarBear){
			Core.polarBearBoss.createBoss(spawnLocation); // summon polar bear boss
			spawnLocation.getWorld().playSound(spawnLocation, Sound.ENTITY_POLAR_BEAR_WARNING, 1000F, 0F);
		}
		else if (bossType == BossType.Creeper){
			Core.creeperBoss.createBoss(spawnLocation); // summon creeper boss
			spawnLocation.getWorld().playSound(spawnLocation, Sound.ENTITY_CREEPER_PRIMED, 1000F, 0F);
		}
	}
	
	public void removeBlocks(Block skullBlock){
		Location center = skullBlock.getLocation();
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Core.thisPlugin, new Runnable(){
			@Override
			public void run() {
				Location temp;
				int startX = center.getBlockX()-1;
				int startY = center.getBlockY()-2;
				int startZ = center.getBlockZ()-1;
				int endX = center.getBlockX()+1;
				int endY = center.getBlockY()+0;
				int endZ = center.getBlockZ()+1;
				for (int y = startY; y <= endY; y++){
					for (int x = startX; x <= endX; x++){
						for (int z = startZ; z <= endZ; z++){
							temp = new Location(center.getWorld(), x, y, z);
							if (temp.getBlock().getType() != Material.AIR
									&& temp.getBlock().getType() != Material.BEDROCK
									&& temp.getBlock().getType() != Material.ENDER_PORTAL_FRAME
									&& temp.getBlock().getType() != Material.ENDER_PORTAL){
								temp.getBlock().setType(Material.AIR);
							}
						}
					}
				}
			}
		}, 1L);
		
	}

}
