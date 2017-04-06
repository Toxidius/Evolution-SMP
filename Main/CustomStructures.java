package Evolution.Main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import Evolution.Bosses.BossSummoner.BossType;
import net.md_5.bungee.api.ChatColor;

@SuppressWarnings("deprecation")
public class CustomStructures extends BlockPopulator{

	private Random r;
	private Enchanter enchanter;
	public int numChunksGenerated = 0;
	public int numStructuresGenerated = 0;
	
	public CustomStructures(){
		r = new Random();
		enchanter = new Enchanter();
	}
	
	@Override
	public void populate(World world, Random random, Chunk chunk) {
		int blockX = chunk.getX() * 16;
		int blockZ = chunk.getZ() * 16;
		numChunksGenerated++;
		
		Block testBlock = world.getBlockAt(blockX, 0, blockZ);
		if (testBlock.getBiome() == Biome.DEEP_OCEAN || testBlock.getBiome() == Biome.OCEAN){
			return; // can't generate shit in an ocean lol
		}
		
		Block corner1 = getHighestBlock(world, blockX, blockZ); // neg neg
		if (corner1 == null){
			return;
		}
		Block corner2 = getHighestBlock(world, blockX+16, blockZ); // pos neg
		if (corner2 == null){
			return;
		}
		Block corner3 = getHighestBlock(world, blockX, blockZ+16); // neg pos
		if (corner3 == null){
			return;
		}
		Block corner4 = getHighestBlock(world, blockX+16, blockZ+16); // pos pos
		if (corner4 == null){
			return;
		}
		
		//Location corner1Loc = corner1.getLocation();
		int deviation1 = Math.abs( corner1.getY() - corner4.getY() ); // two opposite side corners
		int deviation2 = Math.abs( corner2.getY() - corner3.getY() ); // other two opposite side corners
		int averageDeviation = (int) Math.round( (deviation1+deviation2)/2.0 );
		int frequency = 125; // so-so when applicable
		
		if (averageDeviation <= 2){
			// this chunk fills the requirements of being generated on for surface structures
			
			String biomeName = testBlock.getBiome().name().toLowerCase();
			if (biomeName.contains("hills")){
				frequency = 90; // very often when applicable
			}
			else if (biomeName.contains("savanna")
					|| biomeName.contains("flats")){
				frequency = 225; // rare when applicable
			}
			else if (biomeName.contains("plains")
					|| biomeName.contains("desert")){
				frequency = 450;
			}
			
			if (r.nextInt(frequency) == 5){ // r.nextInt(200)
				// 0.5% chance of a structure spawning in this chunk
				
				// check if there are structures to generate
				if (Core.numStructures <= 0){
					return; // no structures in folder!
				}
				
				int startX = corner1.getX();
				int startY = (int) Math.round( (corner1.getY()+corner2.getY()+corner3.getY()+corner4.getY())/4.0 );
				int startZ = corner1.getZ();
				
				File file = Core.structures[ r.nextInt(Core.structures.length) ];
				buildStructure(file, world, random, chunk, startX, startY, startZ);
				numStructuresGenerated++;
				String logText = "Generated the structure " + file.getName() + " on X: " + startX + " Z: " + startZ + " in " + biomeName;
				writeToLog("StructureLog.txt", logText);
			}
		}
		
		// try for an underground structure
		if (averageDeviation <= 10){
			if (r.nextInt(63) == 5){
				// 1.58% chance of a structure spawning in this chunk
				
				// check if there are structures to generate
				if (Core.numUndergroundStructures <= 0){
					return; // no structures in folder!
				}
				
				int startX = corner1.getX();
				int startY = (int) Math.round( (corner1.getY()+corner2.getY()+corner3.getY()+corner4.getY())/4.0 );
				int startZ = corner1.getZ();
				
				// check height requirement
				if (startY <= 50){
					return; // too low! some of the structures wouldn't fully generate!
				}
				
				File file = Core.undergroundStructures[ r.nextInt(Core.undergroundStructures.length) ];
				buildStructure(file, world, random, chunk, startX, startY, startZ);
			}
		}
		
		// end populate
	}
	
	public void buildStructure(File file, World world, Random random, Chunk chunk, int startX, int startY, int startZ){
		// check if structure is being placed in a desert -- in which case use sand instead of dirt and grass
		Material grassMaterial = Material.GRASS;
		byte grassData = 0;
		Material dirtMaterial = Material.DIRT;
		byte dirtData = 0;
		
		Block corner1 = new Location(world, startX, startY, startZ).getBlock();
		if (corner1.getBiome() == Biome.DESERT || corner1.getBiome() == Biome.DESERT_HILLS || corner1.getBiome() == Biome.MUTATED_DESERT){
			grassMaterial = Material.SAND;
			dirtMaterial = Material.SAND;
		}
		else if (corner1.getBiome() == Biome.ICE_FLATS || corner1.getBiome() == Biome.ICE_MOUNTAINS || corner1.getBiome() == Biome.MUTATED_ICE_FLATS || corner1.getBiome() == Biome.TAIGA_COLD || corner1.getBiome() == Biome.TAIGA_COLD_HILLS || corner1.getBiome() == Biome.MUTATED_TAIGA_COLD || corner1.getBiome() == Biome.COLD_BEACH || corner1.getBiome() == Biome.FROZEN_RIVER || corner1.getBiome() == Biome.FROZEN_OCEAN){
			grassMaterial = Material.SNOW_BLOCK;
			dirtMaterial = Material.SNOW_BLOCK;
		}
		else if (corner1.getBiome() == Biome.MESA || corner1.getBiome() == Biome.MESA_CLEAR_ROCK || corner1.getBiome() == Biome.MESA_ROCK || corner1.getBiome() == Biome.MUTATED_MESA || corner1.getBiome() == Biome.MUTATED_MESA_CLEAR_ROCK || corner1.getBiome() == Biome.MUTATED_MESA_ROCK){
			grassMaterial = Material.SAND;
			grassData = 1;
			dirtMaterial = Material.SAND;
			dirtData = 1;
		}
		
		//long startMS = System.currentTimeMillis();
		//long stopMS;
		int x, y, z;
		Material blockMaterial;
		int blockID;
		int lowestY = 1000; // some random number
		int i = 0;
		Byte blockData;
		ArrayList<String> theAttachedBlocks = new ArrayList<String>();
		Location location;
		Block block;
		
		try{
			FileReader fileReader = new FileReader(file);
			BufferedReader br = new BufferedReader(fileReader);
			String line;
			String[] part;
			while ((line = br.readLine()) != null) {
				part = line.split(":");
				if (i == 0){
					// first iteration, so get the lowest y value
					lowestY = Integer.valueOf(part[1]);
				}
				if (part.length < 5){
					// this line is missing some data, so just skip it
					break;
				}
				else if (part.length == 5){
					// regular blocks
					x = startX + Integer.valueOf(part[0]);
					y = startY + Integer.valueOf(part[1]);
					z = startZ + Integer.valueOf(part[2]);
					if (y <= 0){
						continue; // skip this block -- was going to be placed below bedrock
					}
					blockID = Integer.valueOf(part[3]);
					blockData = Byte.valueOf(part[4]);
					blockMaterial = Material.getMaterial(blockID);
					if (Core.attachedBlocks.contains(blockMaterial)){
						theAttachedBlocks.add(line);
						continue; // skip this block for now because it's attached to something else
					}
					else if (blockMaterial == Material.GRASS){
						blockID = grassMaterial.getId();
						blockData = grassData;
					}
					else if (blockMaterial == Material.DIRT){
						blockID = dirtMaterial.getId();
						blockData = dirtData;
					}
					location = new Location(world, x, y, z);
					block = location.getBlock();
					block.setTypeId(blockID, false);
					block.setData(blockData, false);
					//block.setTypeIdAndData(blockID, blockData, false); // set the block id and data, and ignore physics
					
					// for block specific generation stuffs -- like giving a mob spawner a type
					if (blockMaterial == Material.MOB_SPAWNER){
						//CreatureSpawner spawner = (CreatureSpawner) block.getState();
						//spawner.setSpawnedType( getRandomEntityType() );
						block.setType(Material.AIR, false);
						block.setData((byte)0, false);
						spawnStructureGuardians(block.getLocation());
					}
					else if (blockMaterial == Material.CHEST){
						// loot chest code
						Block blockBelow = block.getRelative(BlockFace.DOWN);
						if (blockBelow == null){
							// do nothing as it's not a valid block (below y0)
						}
						else if (blockBelow.getType() == Material.COAL_BLOCK){
							// rare chest loot generation (mabye if I see it needed -- unlikely atm) TODO
						}
						else if (blockBelow.getType() == Material.GOLD_BLOCK){
							// legendary chest loot generation
							Chest chest =  (Chest) block.getState();
							setLegendaryChestLoot(chest.getBlockInventory());
							
						}
						else if (blockBelow != null){
							// basic chest loot generation
							Chest chest =  (Chest) block.getState();
							setBasicChestLoot(chest.getBlockInventory());
						}

					}
					else if (blockMaterial == Material.ENDER_PORTAL_FRAME){
						block.setType(Material.AIR, false);
						block.setData((byte)0, false);
						// spawned boss code
						BossType bossType = BossType.values()[ r.nextInt(BossType.values().length) ];
						location.add(0.5, 0.0, 0.5); // middle of block
						Core.bossSummoner.spawnBoss(bossType, location);
					}
					
					// fill in any air blocks below the structure to make it fit in with the terrain better
					// this modifys the block so it should be the last operation on this block
					if (Integer.valueOf(part[1]) == lowestY){ // block is at lowest offset level
						while (true){ // infinite loop to check blocks below this if they're air until it reaches non-air blocks
							if (block.getRelative(BlockFace.DOWN).getType().isSolid() == false){
								block = block.getRelative(BlockFace.DOWN); // update current block
								if (blockID == Material.GRASS.getId()){
									blockID = Material.DIRT.getId(); // grass to dirt so it looks better
								}
								else if (blockID == Material.MOB_SPAWNER.getId()){
									blockID = Material.AIR.getId();
								}
								block.setTypeId(blockID, false);
								block.setData(blockData, false);
							}
							else{
								break; // end the loop
							}
						}
					}
					
					// end part.length == 5
				}
				else if (part.length == 7 && (Integer.valueOf(part[3]) == 144)){
					// skull blocks
					x = startX + Integer.valueOf(part[0]);
					y = startY + Integer.valueOf(part[1]);
					z = startZ + Integer.valueOf(part[2]);
					if (y <= 0){
						continue; // skip this block -- was going to be placed below bedrock
					}
					blockID = Integer.valueOf(part[3]);
					blockData = Byte.valueOf(part[4]);
					location = new Location(world, x, y, z);
					block = location.getBlock();
					block.setTypeIdAndData(blockID, blockData, false); // set the block id and data, and ignore physics
					Skull skull = (Skull) block.getState();
					skull.setSkullType(SkullType.valueOf(part[5]));
					skull.setRotation(BlockFace.valueOf(part[6]));
					skull.update();
				}
				else if (part.length == 9 && (Integer.valueOf(part[3]) == 68 || Integer.valueOf(part[3]) == 63)){
					// sign block
					// signs are attached blocks, so just add it to attached blocks right away
					theAttachedBlocks.add(line);
				}
				i++;
			}
			br.close(); // close the file
		} catch(Exception ex){
			System.out.println("--------------------------------------------");
			System.out.println("Exception while reading file: " + ex.getMessage());
			System.out.println("--------------------------------------------");
			ex.printStackTrace();
		}
		
		// finish up the attached blocks
		String[] part;
		for (String line : theAttachedBlocks){
			part = line.split(":");
			if (part.length < 5){
				// this line is missing some data, so just skip it
				break;
			}
			else if (part.length == 5){
				// regular blocks
				x = startX + Integer.valueOf(part[0]);
				y = startY + Integer.valueOf(part[1]);
				z = startZ + Integer.valueOf(part[2]);
				blockID = Integer.valueOf(part[3]);
				blockData = Byte.valueOf(part[4]);
				location = new Location(world, x, y, z);
				block = location.getBlock();
				block.setTypeIdAndData(blockID, blockData, false); // set the block id and data, and ignore physics
			}
			else if (part.length == 7 && (Integer.valueOf(part[3]) == 144)){
				// skull blocks
				x = startX + Integer.valueOf(part[0]);
				y = startY + Integer.valueOf(part[1]);
				z = startZ + Integer.valueOf(part[2]);
				blockID = Integer.valueOf(part[3]);
				blockData = Byte.valueOf(part[4]);
				location = new Location(world, x, y, z);
				block = location.getBlock();
				block.setTypeIdAndData(blockID, blockData, false); // set the block id and data, and ignore physics
				Skull skull = (Skull) block.getState();
				skull.setSkullType(SkullType.valueOf(part[5]));
				skull.setRotation(BlockFace.valueOf(part[6]));
			}
			else if (part.length == 9 && (Integer.valueOf(part[3]) == 68 || Integer.valueOf(part[3]) == 63)){
				// sign blocks
				x = startX + Integer.valueOf(part[0]);
				y = startY + Integer.valueOf(part[1]);
				z = startZ + Integer.valueOf(part[2]);
				blockID = Integer.valueOf(part[3]);
				blockData = Byte.valueOf(part[4]);
				location = new Location(world, x, y, z);
				block = location.getBlock();
				block.setTypeIdAndData(blockID, blockData, false); // set the block id and data, and ignore physics
				Sign sign = (Sign) block.getState();
				sign.setLine(0, part[5].replace("\"", ""));
				sign.setLine(1, part[6].replace("\"", ""));
				sign.setLine(2, part[7].replace("\"", ""));
				sign.setLine(3, part[8].replace("\"", ""));
				sign.update();
			}
		}
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Core.thisPlugin, new Runnable(){
			@Override
			public void run() {
				world.refreshChunk(chunk.getX(), chunk.getZ()); // attempt light update TODO: Remove?
			}
		}, 120L); // 6 seconds later refresh it
		
		//stopMS = System.currentTimeMillis();
		//System.out.println("Generated structure on " + chunk.getX()*16 + " " + chunk.getZ()*16 + " -- Generated in: " + (stopMS-startMS) + "MS");
	}
	
	public Block getHighestBlock(World world, int x, int z){
		Material material;
		
		// for most cases, grass forms y 50 to y 90, so start checking there for efficiency
		for (int y = 50; y <= 130; y++){
			material = world.getBlockAt(x, y, z).getType();
			if (material == Material.GRASS || material == Material.MYCEL){
				return world.getBlockAt(x, y, z);
			}
			else if (material == Material.SAND && world.getBlockAt(x, y+1, z).getType() == Material.AIR){
				return world.getBlockAt(x, y, z); // desert
			}
		}
		
		// check y0 to y 10 (for flatland worlds)
		for (int y = 0; y < 10; y++){
			if (world.getBlockAt(x, y, z).getType() == Material.GRASS){
				return world.getBlockAt(x, y, z);
			}
			else if (world.getBlockAt(x, y, z).getType() == Material.SAND && world.getBlockAt(x, y+1, z).getType() == Material.AIR){
				return world.getBlockAt(x, y, z); // desert
			}
		}
		
		// no block found
		return null;
	}
	
	public int getOpenChestSpot(Inventory chestInventory){
		int locationToAdd;
		for (int i = 0; i < 10; i++){
			// try 10 times to get an open spot
			locationToAdd = r.nextInt(chestInventory.getSize()); // get random location inside this chest
			if (chestInventory.getItem(locationToAdd) == null){
				return locationToAdd;
			}
		}
		return 0; // default
	}
	
	public void setBasicChestLoot(Inventory chestInventory){
		int numItemsToAdd = r.nextInt(4)+4; // 5 to 10 random items (old) 4 to 7 new
		int locationToAdd;
		ItemStack stack;
		
		for (int i = 0; i < numItemsToAdd; i++){
			locationToAdd = getOpenChestSpot(chestInventory); // get random location inside this chest
			stack = getBasicStack();
			if (stack.getAmount() != 0){
				chestInventory.setItem(locationToAdd, stack); // replace contents
			}
		}
		
		// every basic chest gets some iron and gold
		stack = new ItemStack(Material.IRON_INGOT, r.nextInt(8)); // 0 to 7 iron
		if (stack.getAmount() < 1){
			stack.setAmount(1);
		}
		locationToAdd = getOpenChestSpot(chestInventory); // get random location inside this chest
		chestInventory.setItem(locationToAdd, stack); // replace contents
		
		stack = new ItemStack(Material.GOLD_INGOT, r.nextInt(10)); // 0 to 9 gold
		if (stack.getAmount() < 1){
			stack.setAmount(1);
		}
		locationToAdd = getOpenChestSpot(chestInventory); // get random location inside this chest
		chestInventory.setItem(locationToAdd, stack); // replace contents
		
	}
		
	
	public void setLegendaryChestLoot(Inventory chestInventory){
		int numItemsToAdd = r.nextInt(4)+4; // 5 to 8 (old) 4 to 7 new
		int locationToAdd;
		ItemStack stack;
		
		for (int i = 0; i < numItemsToAdd; i++){
			locationToAdd = r.nextInt(chestInventory.getSize()); // get random location inside this chest
			stack = getLegendaryStack();
			if (stack.getAmount() != 0){
				chestInventory.setItem(locationToAdd, stack); // replace contents
			}
		}
		
		// 1 in 10 chance of getting a beheading axe
		if (Math.random() <= 0.1){
			// add axe
			locationToAdd = r.nextInt(chestInventory.getSize()); // get random location inside this chest
			stack = Core.axeOfBeheading.getRelic().clone();
			chestInventory.setItem(locationToAdd, stack); // replace contents
			
			// add "quest" book
			locationToAdd = r.nextInt(chestInventory.getSize()); // get random location inside this chest
			stack = Core.axeOfBeheading.getBook().clone();
			chestInventory.setItem(locationToAdd, stack); // replace contents
		}
	}
	
	public ItemStack getBasicStack(){
		int stackAmount;
		int random = r.nextInt(40); // 0 to 39
		ItemStack stack;
		
		if (random == 0){
			stackAmount = 10;
			stack = new ItemStack(Material.GOLD_INGOT, r.nextInt(stackAmount));
		}
		else if (random == 1){
			stackAmount = 32;
			stack = new ItemStack(Material.BONE, r.nextInt(stackAmount));
		}
		else if (random == 2){
			stackAmount = 16;
			stack = new ItemStack(Material.COAL, r.nextInt(stackAmount));
		}
		else if (random == 3){
			stackAmount = 1;
			stack = new ItemStack(Material.SADDLE, stackAmount);
		}
		else if (random == 4){
			stackAmount = 8;
			stack = new ItemStack(Material.BREAD, r.nextInt(stackAmount));
		}
		else if (random == 5){
			stackAmount = 8;
			stack = new ItemStack(Material.BAKED_POTATO, r.nextInt(stackAmount));
		}
		else if (random == 6){
			stackAmount = 1;
			stack = new ItemStack(Material.IRON_BARDING, stackAmount);
		}
		else if (random == 7){
			stackAmount = 1;
			stack = new ItemStack(Material.GOLD_BARDING, stackAmount);
		}
		else if (random == 8){
			stackAmount = 8;
			stack = new ItemStack(Material.IRON_INGOT, r.nextInt(stackAmount));
		}
		else if (random == 9){
			stackAmount = 2;
			stack = new ItemStack(Material.GOLDEN_APPLE, r.nextInt(stackAmount));
		}
		else if (random == 10){
			stackAmount = 4;
			stack = new ItemStack(Material.MELON_SEEDS, r.nextInt(stackAmount));
		}
		else if (random == 11){
			stackAmount = 3;
			stack = new ItemStack(Material.NAME_TAG, r.nextInt(stackAmount));
		}
		else if (random == 12){
			stackAmount = 12;
			stack = new ItemStack(Material.STRING, r.nextInt(stackAmount));
		}
		else if (random == 12){
			stackAmount = 8;
			stack = new ItemStack(Material.SULPHUR, r.nextInt(stackAmount));
		}
		else if (random == 13){
			stackAmount = 1;
			stack = new ItemStack(Material.BUCKET, stackAmount);
		}
		else if (random == 14){
			stackAmount = 8;
			stack = new ItemStack(Material.ROTTEN_FLESH, r.nextInt(stackAmount));
		}
		else if (random == 15){
			stackAmount = 1;
			stack = new ItemStack(Material.GOLD_RECORD, stackAmount);
		}
		else if (random == 16){
			stackAmount = 1;
			stack = new ItemStack(Material.GREEN_RECORD, stackAmount);
		}
		else if (random == 17){
			stackAmount = 1;
			stack = new ItemStack(Material.ANVIL, stackAmount);
		}
		else if (random == 18){
			stackAmount = 5;
			stack = new ItemStack(Material.DIAMOND, r.nextInt(stackAmount));
		}
		else if (random == 19){
			stackAmount = 1;
			stack = new ItemStack(Material.DIAMOND_AXE, r.nextInt(stackAmount));
		}
		else if (random == 20){
			stackAmount = 16;
			stack = new ItemStack(Material.SLIME_BALL, r.nextInt(stackAmount));
		}
		else if (random == 21){
			stackAmount = 24;
			stack = new ItemStack(Material.ARROW, r.nextInt(stackAmount));
		}
		else if (random == 22){
			stackAmount = 1;
			stack = new ItemStack(Material.BOW, stackAmount);
			if (Math.random() <= 0.75){
				stack = enchanter.enchant(stack, r.nextInt(2)+1, false); // add 1 to 2 basic enchants
			}
		}
		else if (random == 23){
			stackAmount = 1;
			stack = new ItemStack(Material.IRON_CHESTPLATE, stackAmount);
			if (Math.random() <= 0.75){
				stack = enchanter.enchant(stack, r.nextInt(2)+1, false); // add 1 to 2 basic enchants
			}
		}
		else if (random == 24){
			stackAmount = 1;
			stack = new ItemStack(Material.IRON_LEGGINGS, stackAmount);
			if (Math.random() <= 0.75){
				stack = enchanter.enchant(stack, r.nextInt(2)+1, false); // add 1 to 2 basic enchants
			}
		}
		else if (random == 25){
			stackAmount = 1;
			stack = new ItemStack(Material.IRON_HELMET, stackAmount);
			if (Math.random() <= 0.75){
				stack = enchanter.enchant(stack, r.nextInt(2)+1, false); // add 1 to 2 basic enchants
			}
		}
		else if (random == 26){
			stackAmount = 1;
			stack = new ItemStack(Material.IRON_BOOTS, stackAmount);
			if (Math.random() <= 0.75){
				stack = enchanter.enchant(stack, r.nextInt(2)+1, false); // add 1 to 2 basic enchants
			}
		}
		else if (random == 27){
			stackAmount = 8;
			stack = new ItemStack(Material.BOOK, r.nextInt(stackAmount));
		}
		else if (random == 28){
			stackAmount = 16;
			stack = new ItemStack(Material.MYCEL, r.nextInt(stackAmount));
		}
		else if (random == 29){
			stackAmount = 8;
			stack = new ItemStack(Material.COCOA, r.nextInt(stackAmount));
		}
		else if (random == 30){
			stackAmount = 32;
			stack = new ItemStack(Material.LOG, r.nextInt(stackAmount));
		}
		else if (random == 31){
			stackAmount = 8;
			stack = new ItemStack(Material.COOKED_BEEF, r.nextInt(stackAmount));
		}
		else if (random == 32){
			stackAmount = 0;
			if (r.nextInt(4) == 0){
				stackAmount = 0;
			}

			if (stackAmount != 0){
				stack = new ItemStack(Material.GOLDEN_APPLE, stackAmount, (short)1); // enchanted golden apple (1/4th time)
			}
			else{
				stack = new ItemStack(Material.AIR, 1);
			}
		}
		else if (random == 33){
			stackAmount = 8;
			stack = new ItemStack(Material.IRON_INGOT, r.nextInt(stackAmount)); // more iron
		}
		else if (random == 34){
			stackAmount = 10;
			stack = new ItemStack(Material.GOLD_INGOT, r.nextInt(stackAmount)); // more gold
		}
		else if (random == 35){
			stackAmount = 1;
			int recordID = 2256; // base id for first record
			recordID += r.nextInt(12); // 0 to 11
			stack = new ItemStack(Material.getMaterial(recordID), stackAmount); // random record
		}
		else if (random == 36){
			stackAmount = 10;
			stack = new ItemStack(Material.GOLD_INGOT, r.nextInt(stackAmount)); // more gold
		}
		else if (random == 37){
			stackAmount = 5;
			int index = r.nextInt(Core.items.size());
			stack = Core.items.get(index).clone(); // of the following: block breaker, block placer, or grinder
			stack.setAmount(r.nextInt(stackAmount)+1);
		}
		else if (random == 38){
			stackAmount = 5;
			int index = r.nextInt(Core.items.size());
			stack = Core.items.get(index).clone(); // of the following: block breaker, block placer, or grinder
			stack.setAmount(r.nextInt(stackAmount)+1);
		}
		else{
			stackAmount = 1;
			stack = new ItemStack(Material.FISHING_ROD, stackAmount);
			stack = enchanter.enchant(stack, r.nextInt(3)+1, false); // add 1 to 3 basic enchants
		}
		
		if (stack.getAmount() == 0){
			stack.setAmount(1);
		}
		return stack;
	}
	
	public ItemStack getLegendaryStack(){
		int stackAmount;
		int random = r.nextInt(28); // 0 to 27
		ItemStack stack;
		
		if (random == 0){
			stackAmount = 5;
			stack = new ItemStack(Material.GOLD_INGOT, r.nextInt(stackAmount)+2);
		}
		else if (random == 1){
			stackAmount = 4;
			stack = new ItemStack(Material.IRON_INGOT, r.nextInt(stackAmount)+4);
		}
		else if (random == 2){
			stackAmount = 5;
			stack = new ItemStack(Material.DIAMOND, r.nextInt(stackAmount)+2);
		}
		else if (random == 3){
			stackAmount = 1;
			stack = new ItemStack(Material.SADDLE, stackAmount);
		}
		else if (random == 4){
			stackAmount = 1;
			stack = new ItemStack(Material.IRON_PICKAXE, stackAmount);
			stack = enchanter.enchant(stack, r.nextInt(3)+1, true); // 1 to 3 enchants added
		}
		else if (random == 5){
			stackAmount = 1;
			stack = new ItemStack(Material.IRON_SWORD, stackAmount);
			stack = enchanter.enchant(stack, r.nextInt(3)+1, true); // 1 to 3 enchants added
		}
		else if (random == 6){
			stackAmount = 1;
			stack = new ItemStack(Material.IRON_SPADE, stackAmount);
			stack = enchanter.enchant(stack, r.nextInt(3)+1, true); // 1 to 3 enchants added
		}
		else if (random == 7){
			stackAmount = 1;
			stack = new ItemStack(Material.IRON_HELMET, stackAmount);
			stack = enchanter.enchant(stack, r.nextInt(3)+1, true); // 1 to 3 enchants added
		}
		else if (random == 8){
			stackAmount = 1;
			stack = new ItemStack(Material.IRON_CHESTPLATE, stackAmount);
			stack = enchanter.enchant(stack, r.nextInt(3)+1, true); // 1 to 3 enchants added
		}
		else if (random == 9){
			stackAmount = 1;
			stack = new ItemStack(Material.IRON_LEGGINGS, stackAmount);
			stack = enchanter.enchant(stack, r.nextInt(3)+1, true); // 1 to 3 enchants added
		}
		else if (random == 10){
			stackAmount = 1;
			stack = new ItemStack(Material.IRON_BOOTS, stackAmount);
			stack = enchanter.enchant(stack, r.nextInt(3)+1, true); // 1 to 3 enchants added
		}
		else if (random == 11){
			stackAmount = 1;
			stack = new ItemStack(Material.DIAMOND_PICKAXE, stackAmount);
			stack = enchanter.enchant(stack, r.nextInt(3)+1, true); // 1 to 3 enchants added
		}
		else if (random == 12){
			stackAmount = 1;
			stack = new ItemStack(Material.DIAMOND_SWORD, stackAmount);
			stack = enchanter.enchant(stack, r.nextInt(3)+1, true); // 1 to 3 enchants added
		}
		else if (random == 13){
			stackAmount = 1;
			stack = new ItemStack(Material.DIAMOND_SPADE, stackAmount);
			stack = enchanter.enchant(stack, r.nextInt(3)+1, true); // 1 to 3 enchants added
		}
		else if (random == 14){
			stackAmount = 1;
			stack = new ItemStack(Material.DIAMOND_HELMET, stackAmount);
			stack = enchanter.enchant(stack, r.nextInt(3)+1, true); // 1 to 3 enchants added
		}
		else if (random == 15){
			stackAmount = 1;
			stack = new ItemStack(Material.DIAMOND_CHESTPLATE, stackAmount);
			stack = enchanter.enchant(stack, r.nextInt(3)+1, true); // 1 to 3 enchants added
		}
		else if (random == 16){
			stackAmount = 1;
			stack = new ItemStack(Material.DIAMOND_LEGGINGS, stackAmount);
			stack = enchanter.enchant(stack, r.nextInt(3)+1, true); // 1 to 3 enchants added
		}
		else if (random == 17){
			stackAmount = 1;
			stack = new ItemStack(Material.DIAMOND_BOOTS, stackAmount);
			stack = enchanter.enchant(stack, r.nextInt(3)+1, true); // 1 to 3 enchants added
		}
		else if (random == 18){
			stackAmount = 4;
			stack = new ItemStack(Material.EMERALD, r.nextInt(stackAmount)+2); // 6 to 4 emeralds
		}
		else if (random == 19){
			stackAmount = 1;
			stack = new ItemStack(Material.IRON_BARDING, stackAmount);
		}
		else if (random == 20){
			stackAmount = 1;
			stack = new ItemStack(Material.GOLD_BARDING, stackAmount);
		}
		else if (random == 21){
			stackAmount = 1;
			stack = new ItemStack(Material.DIAMOND_BARDING, stackAmount);
		}
		else if (random == 22){
			stackAmount = 1;
			stack = new ItemStack(Material.GOLDEN_APPLE, stackAmount, (short)1);
		}
		else if (random == 23){
			stackAmount = 5;
			stack = new ItemStack(Material.GOLD_INGOT, r.nextInt(stackAmount)+2); // more gold
		}
		else if (random == 24){
			stackAmount = 4;
			stack = new ItemStack(Material.IRON_INGOT, r.nextInt(stackAmount)+4); // more iron
		}
		else if (random == 25){
			stackAmount = 5;
			stack = new ItemStack(Material.GOLD_INGOT, r.nextInt(stackAmount)+2); // more gold
		}
		else if (random == 26){
			stackAmount = 4;
			stack = new ItemStack(Material.IRON_INGOT, r.nextInt(stackAmount)+4); // more iron
		}
		else if (random == 27){
			stackAmount = 5;
			stack = new ItemStack(Material.GOLDEN_APPLE, r.nextInt(stackAmount)+1, (short)0);
		}
		else{
			stackAmount = 16;
			stack = new ItemStack(Material.GOLDEN_CARROT, r.nextInt(stackAmount));
		}
		
		if (stack.getAmount() == 0){
			stack.setAmount(1);
		}
		return stack;
	}
	
	public void spawnStructureGuardians(Location spawnLocation){
		int numToSpawn = r.nextInt(2)+1; // 1 or 2
		
		EntityType guardianType;
		Location offsetLocation;
		for (int i = 0; i < numToSpawn; i++){
			offsetLocation = spawnLocation.add(Math.random()-0.5, 0, Math.random()-0.5); // from -0.5 to +0.5 offset in x and z
			guardianType = getStructureGuardianMobType();
			if (guardianType == EntityType.ZOMBIE){
				Zombie zombie = (Zombie) spawnLocation.getWorld().spawnEntity(offsetLocation, guardianType);
				zombie.setRemoveWhenFarAway(false);
				zombie.setCustomName(ChatColor.DARK_PURPLE + "Structure Guardian");
				if (r.nextInt(2) == 1){
					zombie.getEquipment().setItemInMainHand(new ItemStack(Material.STONE_SWORD, 1));
					zombie.getEquipment().setItemInMainHandDropChance(0F);
				}
				else{
					zombie.getEquipment().setItemInMainHand(new ItemStack(Material.IRON_SWORD, 1));
					zombie.getEquipment().setItemInMainHandDropChance(0F);
				}
				addRandomArmorToGuardian(zombie);
			}
			if (guardianType == EntityType.SKELETON){
				Skeleton skeleton = (Skeleton) spawnLocation.getWorld().spawnEntity(offsetLocation, guardianType);
				skeleton.setRemoveWhenFarAway(false);
				skeleton.setCustomName(ChatColor.DARK_PURPLE + "Structure Guardian");
				skeleton.getEquipment().setItemInMainHand(new ItemStack(Material.BOW, 1));
				skeleton.getEquipment().setItemInMainHandDropChance(0F);
				addRandomArmorToGuardian(skeleton);
			}
		}
	}
	
	public void addRandomArmorToGuardian(Entity entity){
		if (!(entity instanceof LivingEntity)){
			return;
		}
		
		LivingEntity living = (LivingEntity) entity;
		ItemStack helm;
		ItemStack chest;
		ItemStack leg;
		ItemStack boot;
		
		int rand = r.nextInt(3); // 0 to 2
		if (rand == 0){
			// gold armor
			helm = new ItemStack(Material.GOLD_HELMET, 1);
			chest = new ItemStack(Material.GOLD_CHESTPLATE, 1);
			leg = new ItemStack(Material.GOLD_LEGGINGS, 1);
			boot = new ItemStack(Material.GOLD_BOOTS, 1);
			if (Math.random() < 0.25){
				helm = enchanter.enchant(helm, 1, false);
			}
			if (Math.random() < 0.25){
				chest = enchanter.enchant(chest, 1, false);
			}
			if (Math.random() < 0.25){
				leg = enchanter.enchant(leg, 1, false);
			}
			if (Math.random() < 0.25){
				boot = enchanter.enchant(boot, 1, false);
			}
		}
		else if (rand == 1){
			// leather armor
			helm = new ItemStack(Material.LEATHER_HELMET, 1);
			chest = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
			leg = new ItemStack(Material.LEATHER_LEGGINGS, 1);
			boot = new ItemStack(Material.LEATHER_BOOTS, 1);
			if (Math.random() < 0.25){
				helm = enchanter.enchant(helm, 1, true);
			}
			if (Math.random() < 0.25){
				chest = enchanter.enchant(chest, 1, true);
			}
			if (Math.random() < 0.25){
				leg = enchanter.enchant(leg, 1, true);
			}
			if (Math.random() < 0.25){
				boot = enchanter.enchant(boot, 1, true);
			}
		}
		else{
			// iron armor
			helm = new ItemStack(Material.IRON_HELMET, 1);
			chest = new ItemStack(Material.IRON_CHESTPLATE, 1);
			leg = new ItemStack(Material.IRON_LEGGINGS, 1);
			boot = new ItemStack(Material.IRON_BOOTS, 1);
			if (Math.random() < 0.25){
				helm = enchanter.enchant(helm, 1, false);
			}
			if (Math.random() < 0.25){
				chest = enchanter.enchant(chest, 1, false);
			}
			if (Math.random() < 0.25){
				leg = enchanter.enchant(leg, 1, false);
			}
			if (Math.random() < 0.25){
				boot = enchanter.enchant(boot, 1, false);
			}
		}
		
		living.getEquipment().setHelmet(helm);
		living.getEquipment().setHelmetDropChance(0F);
		living.getEquipment().setChestplate(chest);
		living.getEquipment().setChestplateDropChance(0F);
		living.getEquipment().setLeggings(leg);
		living.getEquipment().setLeggingsDropChance(0F);
		living.getEquipment().setBoots(boot);
		living.getEquipment().setBootsDropChance(0F);
		
	}
	
	public EntityType getStructureGuardianMobType(){
		int rand = r.nextInt(2); // 0 or 1
		if (rand == 0){
			return EntityType.ZOMBIE;
		}
		else{
			return EntityType.SKELETON;
		}
	}
	
	public PotionEffectType getRandomPotionEffectType(){
		int rand = r.nextInt(4); // 0 to 3
		if (rand == 0){
			return PotionEffectType.REGENERATION;
		}
		else if (rand == 1){
			return PotionEffectType.SPEED;
		}
		else if (rand == 2){
			return PotionEffectType.DAMAGE_RESISTANCE;
		}
		else{
			return PotionEffectType.INCREASE_DAMAGE;
		}
	}
	
	public EntityType getRandomEntityType(){
		int rand = r.nextInt(4); // 0 to 3
		if (rand == 0){
			return EntityType.ZOMBIE;
		}
		else if (rand == 1){
			return EntityType.SKELETON;
		}
		else if (rand == 2){
			return EntityType.SPIDER;
		}
		else{
			return EntityType.CREEPER;
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
