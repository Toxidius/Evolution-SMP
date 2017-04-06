package Evolution.Main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.FlowerPot;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.java.JavaPlugin;

import Evolution.Bosses.BossDrops;
import Evolution.Bosses.BossLoader;
import Evolution.Bosses.BossSummoner;
import Evolution.Bosses.CreeperBoss;
import Evolution.Bosses.PolarBearBoss;
import Evolution.Bosses.PumpkinBoss;
import Evolution.Bosses.SpiderBoss;
import Evolution.Bosses.ZombieBoss;
import Evolution.GameMechanics.AntiEndermenPickup;
import Evolution.GameMechanics.AnvilCombining;
import Evolution.GameMechanics.AprilFoolsCreeperTroll;
import Evolution.GameMechanics.BlockBreaker;
import Evolution.GameMechanics.BlockPlacer;
import Evolution.GameMechanics.BloodMoon;
import Evolution.GameMechanics.CustomProjectileDamage;
import Evolution.GameMechanics.EggThrowFix;
import Evolution.GameMechanics.EnderDragonDrops;
import Evolution.GameMechanics.EntityExplosions;
import Evolution.GameMechanics.FancyFishing;
import Evolution.GameMechanics.FancySounds;
import Evolution.GameMechanics.FixChunkUnloading;
import Evolution.GameMechanics.FixInfiniteElytraFlight;
import Evolution.GameMechanics.GrieferLagHell;
import Evolution.GameMechanics.Grinder;
import Evolution.GameMechanics.HarderMobs;
import Evolution.GameMechanics.HorseRideFix;
import Evolution.GameMechanics.InventorySnooping;
import Evolution.GameMechanics.Loggers;
import Evolution.GameMechanics.MobsWithHeads;
import Evolution.GameMechanics.NightTimeCorneringEvent;
import Evolution.GameMechanics.PathBlockSpeedBoost;
import Evolution.GameMechanics.PigmenNoDamageSwipeAttack;
import Evolution.GameMechanics.PlayerAFK;
import Evolution.GameMechanics.PlayerChat;
import Evolution.GameMechanics.PlayerJoin;
import Evolution.GameMechanics.PlayerQuit;
import Evolution.GameMechanics.PlayerSleep;
import Evolution.GameMechanics.PufferFishIntoxication;
import Evolution.GameMechanics.RelicDestroyByExplosionFix;
import Evolution.GameMechanics.RottenFleshNausia;
import Evolution.GameMechanics.ServerInfo;
import Evolution.GameMechanics.ServerMOTD;
import Evolution.GameMechanics.StripMineTroll;
import Evolution.GameMechanics.TabList;
import Evolution.GameMechanics.TopHatWatcher;
import Evolution.GameMechanics.VillagerToWitchFix;
import Evolution.HolidayRelics.ChristmasParty;
import Evolution.HolidayRelics.EnchantedParticles;
import Evolution.HolidayRelics.MistleToe;
import Evolution.HolidayRelics.NewYearsParty;
import Evolution.HolidayRelics.PartyPopper;
import Evolution.HolidayRelics.SnowTime;
import Evolution.HolidayRelics.SnowballGun;
import Evolution.Messaging.MessageCommandExecutor;
import Evolution.Relics.AxeOfBeheading;
import Evolution.Relics.ExpExtractor;
import Evolution.Relics.FireworkBomb;
import Evolution.Relics.FortunatePickaxe;
import Evolution.Relics.HealingElixir;
import Evolution.Relics.HoeOfPossession;
import Evolution.Relics.JackHammer;
import Evolution.Relics.LamboCart9000;
import Evolution.Relics.LumberAxe;
import Evolution.Relics.PhenomenalBow;
import Evolution.Relics.PowderOfBlockMorphing;
import Evolution.Relics.SavingGraceBed;
import Evolution.Relics.SlimeChunkLocator;
import Evolution.Relics.SpeedyMcGeePickaxe;
import Evolution.Relics.SwordOfExperience;
import Evolution.Relics.SwordOfFreezing;
import Evolution.Relics.WateringWand;

public class Core extends JavaPlugin implements Listener{

	public static JavaPlugin thisPlugin;
	
	// custom structures data
	public static Random r;
	public static WorldTools worldTools;
	public static int numStructures = 0;
	public static File[] structures;
	public static int numUndergroundStructures = 0;
	public static File[] undergroundStructures;
	public static ArrayList<Material> attachedBlocks;
	Material material = Material.DIRT;
	Byte data = 0;
	int radius = 3;
	int x1 = 0;
	int x2 = 0;
	int y1 = 0;
	int y2 = 0;
	int z1 = 0;
	int z2 = 0;
	
	// misc objects
	public static ArrayList<ItemStack> items;
	public static ArrayList<ItemStack> relics;
	public static char heart = '\u2764';
	
	// game mechanics
	public static CustomStructures customStructures;
	public static BlockBreaker blockBreaker;
	public static BlockPlacer blockPlacer;
	public static Grinder grinder;
	public static AnvilCombining anvilCombining;
	public static StripMineTroll stripMineTroll;
	public static HarderMobs harderMobs;
	public static BloodMoon bloodMoon;
	public static CustomProjectileDamage customProjectileDamage;
	public static NightTimeCorneringEvent nightTimeCorneringEvent;
	public static PlayerChat playerChat;
	public static PlayerJoin playerJoin;
	public static PlayerQuit playerQuit;
	public static AntiEndermenPickup antiEndermanPickup;
	public static EnderDragonDrops enderDragonDrops;
	public static EntityExplosions entityExplosions;
	public static Loggers loggers;
	public static ServerInfo serverInfo;
	public static PlayerSleep playerSleep;
	public static ServerMOTD serverMOTD;
	public static MobsWithHeads mobsWithHeads;
	public static MessageCommandExecutor messageCommandExecutor; // responsive for /msg and /r
	public static TabList tabList;
	public static EggThrowFix eggThrowFix;
	public static FancyFishing fancyFishing;
	public static PlayerAFK playerAFK;
	public static PigmenNoDamageSwipeAttack pigmenNoDamageSwipeAttack;
	public static PufferFishIntoxication pufferFishIntoxication;
	public static FixInfiniteElytraFlight fixInfiniteElytraFlight;
	public static InventorySnooping inventorySnooping;
	public static PathBlockSpeedBoost pathBlockSpeedBoost;
	public static HorseRideFix horseRideFix;
	public static RottenFleshNausia rottenFleshNausia;
	public static VillagerToWitchFix villagerToWitchFix;
	public static RelicDestroyByExplosionFix relicDestoryByExplosionFix;
	public static GrieferLagHell grieferLagHell;
	public static FancySounds fancySounds;
	public static FixChunkUnloading fixChunkUnloading;
	public static AprilFoolsCreeperTroll aprilFoolsCreeperTroll;
	
	// relic objects
	public static JackHammer jackHammer;
	public static FortunatePickaxe fortunatePickaxe;
	public static AxeOfBeheading axeOfBeheading;
	public static SwordOfExperience swordOfExperience;
	public static SwordOfFreezing swordOfFreezing;
	public static SpeedyMcGeePickaxe speedyMcGeePickaxe;
	public static SavingGraceBed savingGraceBed;
	public static HoeOfPossession hoeOfPossession;
	public static WateringWand wateringWand;
	public static FireworkBomb fireworkBomb;
	public static PowderOfBlockMorphing powderOfBlockMorphing;
	public static LamboCart9000 lamboCart9000;
	public static SlimeChunkLocator slimeChunkLocator;
	public static LumberAxe lumberAxe;
	public static ExpExtractor expExtractor;
	public static PhenomenalBow phenomenalBow;
	public static HealingElixir healingElixir;
	
	// christmas relic objects
	public static SnowballGun snowballGun;
	public static MistleToe mistleToe;
	public static ChristmasParty christmasParty;
	public static SnowTime snowTime;
	public static EnchantedParticles enchantedPartcles;
	public static NewYearsParty newYearsParty;
	public static PartyPopper partyPopper;
	
	// boss objects
	public static BossLoader bossLoader;
	public static BossSummoner bossSummoner;
	public static BossDrops bossDrops;
	public static SpiderBoss spiderBoss;
	public static ZombieBoss zombieBoss;
	public static PolarBearBoss polarBearBoss;
	public static PumpkinBoss pumpkinBoss;
	public static CreeperBoss creeperBoss;
	
	// listeners
	
	
	@Override
	public void onEnable(){
		// important objects
		thisPlugin = this;
		r = new Random();
		worldTools = new WorldTools();
		attachedBlocks = new ArrayList<Material>();
		attachedBlocks.add(Material.TORCH);
		attachedBlocks.add(Material.TRAP_DOOR);
		attachedBlocks.add(Material.WOOD_DOOR);
		attachedBlocks.add(Material.BED);
		attachedBlocks.add(Material.SIGN);
		attachedBlocks.add(Material.SIGN_POST);
		attachedBlocks.add(Material.LEVER);
		attachedBlocks.add(Material.STONE_BUTTON);
		attachedBlocks.add(Material.WOOD_BUTTON);
		attachedBlocks.add(Material.REDSTONE_TORCH_ON);
		attachedBlocks.add(Material.REDSTONE_TORCH_OFF);
		
		// misc objects
		relics = new ArrayList<ItemStack>();
		items = new ArrayList<ItemStack>();
		
		// game mechanics
		blockBreaker = new BlockBreaker();
		blockPlacer = new BlockPlacer();
		grinder = new Grinder();
		anvilCombining = new AnvilCombining();
		stripMineTroll = new StripMineTroll();
		harderMobs = new HarderMobs();
		bloodMoon = new BloodMoon();
		customProjectileDamage = new CustomProjectileDamage();
		nightTimeCorneringEvent = new NightTimeCorneringEvent();
		playerChat = new PlayerChat();
		playerJoin = new PlayerJoin();
		playerQuit = new PlayerQuit();
		antiEndermanPickup = new AntiEndermenPickup();
		enderDragonDrops = new EnderDragonDrops();
		entityExplosions = new EntityExplosions();
		loggers = new Loggers();
		serverInfo = new ServerInfo();
		playerSleep = new PlayerSleep();
		serverMOTD = new ServerMOTD();
		mobsWithHeads = new MobsWithHeads();
		messageCommandExecutor = new MessageCommandExecutor();
		tabList = new TabList();
		eggThrowFix = new EggThrowFix();
		fancyFishing = new FancyFishing();
		playerAFK = new PlayerAFK();
		pigmenNoDamageSwipeAttack = new PigmenNoDamageSwipeAttack();
		pufferFishIntoxication = new PufferFishIntoxication();
		fixInfiniteElytraFlight = new FixInfiniteElytraFlight();
		inventorySnooping = new InventorySnooping();
		pathBlockSpeedBoost = new PathBlockSpeedBoost();
		horseRideFix = new HorseRideFix();
		rottenFleshNausia = new RottenFleshNausia();
		villagerToWitchFix = new VillagerToWitchFix();
		relicDestoryByExplosionFix = new RelicDestroyByExplosionFix();
		grieferLagHell = new GrieferLagHell();
		fancySounds = new FancySounds();
		fixChunkUnloading = new FixChunkUnloading();
		aprilFoolsCreeperTroll = new AprilFoolsCreeperTroll();
		
		// relic objects
		jackHammer = new JackHammer();
		fortunatePickaxe = new FortunatePickaxe();
		axeOfBeheading = new AxeOfBeheading();
		swordOfExperience = new SwordOfExperience();
		swordOfFreezing = new SwordOfFreezing();
		speedyMcGeePickaxe = new SpeedyMcGeePickaxe();
		savingGraceBed = new SavingGraceBed();
		hoeOfPossession = new HoeOfPossession();
		wateringWand = new WateringWand();
		fireworkBomb = new FireworkBomb();
		powderOfBlockMorphing = new PowderOfBlockMorphing();
		lamboCart9000 = new LamboCart9000();
		slimeChunkLocator = new SlimeChunkLocator();
		lumberAxe = new LumberAxe();
		expExtractor = new ExpExtractor();
		phenomenalBow = new PhenomenalBow();
		healingElixir = new HealingElixir();
		
		// christmas relic objects
		snowballGun = new SnowballGun();
		mistleToe = new MistleToe();
		christmasParty = new ChristmasParty();
		snowTime = new SnowTime();
		enchantedPartcles = new EnchantedParticles();
		newYearsParty = new NewYearsParty();
		partyPopper = new PartyPopper();
		
		// boss objects
		bossLoader = new BossLoader();
		bossSummoner = new BossSummoner();
		bossDrops = new BossDrops();
		spiderBoss = new SpiderBoss();
		zombieBoss = new ZombieBoss();
		polarBearBoss = new PolarBearBoss();
		pumpkinBoss = new PumpkinBoss();
		creeperBoss = new CreeperBoss();
		
		// register listener classes
		Bukkit.getPluginManager().registerEvents(this, thisPlugin);
		// game mechanics
		Bukkit.getPluginManager().registerEvents(blockBreaker, thisPlugin);
		Bukkit.getPluginManager().registerEvents(blockPlacer, thisPlugin);
		Bukkit.getPluginManager().registerEvents(grinder, thisPlugin);
		Bukkit.getPluginManager().registerEvents(anvilCombining, thisPlugin);
		Bukkit.getPluginManager().registerEvents(stripMineTroll, thisPlugin);
		Bukkit.getPluginManager().registerEvents(harderMobs, thisPlugin);
		Bukkit.getPluginManager().registerEvents(bloodMoon, thisPlugin);
		Bukkit.getPluginManager().registerEvents(customProjectileDamage, thisPlugin);
		Bukkit.getPluginManager().registerEvents(playerChat, thisPlugin);
		Bukkit.getPluginManager().registerEvents(antiEndermanPickup, thisPlugin);
		Bukkit.getPluginManager().registerEvents(enderDragonDrops, thisPlugin);
		Bukkit.getPluginManager().registerEvents(entityExplosions, thisPlugin);
		Bukkit.getPluginManager().registerEvents(loggers, thisPlugin);
		Bukkit.getPluginManager().registerEvents(serverInfo, thisPlugin);
		Bukkit.getPluginManager().registerEvents(playerSleep, thisPlugin);
		Bukkit.getPluginManager().registerEvents(playerJoin, thisPlugin);
		Bukkit.getPluginManager().registerEvents(playerQuit, thisPlugin);
		Bukkit.getPluginManager().registerEvents(serverMOTD, thisPlugin);
		Bukkit.getPluginManager().registerEvents(mobsWithHeads, thisPlugin);
		Bukkit.getPluginManager().registerEvents(tabList, thisPlugin);
		Bukkit.getPluginManager().registerEvents(eggThrowFix, thisPlugin);
		Bukkit.getPluginManager().registerEvents(fancyFishing, thisPlugin);
		Bukkit.getPluginManager().registerEvents(playerAFK, thisPlugin);
		Bukkit.getPluginManager().registerEvents(pigmenNoDamageSwipeAttack, thisPlugin);
		Bukkit.getPluginManager().registerEvents(pufferFishIntoxication, thisPlugin);
		Bukkit.getPluginManager().registerEvents(fixInfiniteElytraFlight, thisPlugin);
		Bukkit.getPluginManager().registerEvents(inventorySnooping, thisPlugin);
		Bukkit.getPluginManager().registerEvents(horseRideFix, thisPlugin);
		Bukkit.getPluginManager().registerEvents(villagerToWitchFix, thisPlugin);
		Bukkit.getPluginManager().registerEvents(relicDestoryByExplosionFix, thisPlugin);
		Bukkit.getPluginManager().registerEvents(grieferLagHell, thisPlugin);
		Bukkit.getPluginManager().registerEvents(fancySounds, thisPlugin);
		Bukkit.getPluginManager().registerEvents(fixChunkUnloading, thisPlugin);
		Bukkit.getPluginManager().registerEvents(aprilFoolsCreeperTroll, thisPlugin);
		// relics
		Bukkit.getPluginManager().registerEvents(jackHammer, thisPlugin);
		Bukkit.getPluginManager().registerEvents(axeOfBeheading, thisPlugin);
		Bukkit.getPluginManager().registerEvents(swordOfExperience, thisPlugin);
		Bukkit.getPluginManager().registerEvents(swordOfFreezing, thisPlugin);
		Bukkit.getPluginManager().registerEvents(savingGraceBed, thisPlugin);
		Bukkit.getPluginManager().registerEvents(hoeOfPossession, thisPlugin);
		Bukkit.getPluginManager().registerEvents(wateringWand, thisPlugin);
		Bukkit.getPluginManager().registerEvents(fireworkBomb, thisPlugin);
		Bukkit.getPluginManager().registerEvents(powderOfBlockMorphing, thisPlugin);
		Bukkit.getPluginManager().registerEvents(lamboCart9000, thisPlugin);
		Bukkit.getPluginManager().registerEvents(slimeChunkLocator, thisPlugin);
		Bukkit.getPluginManager().registerEvents(lumberAxe, thisPlugin);
		Bukkit.getPluginManager().registerEvents(expExtractor, thisPlugin);
		Bukkit.getPluginManager().registerEvents(healingElixir, thisPlugin);
		// christmas relics
		Bukkit.getPluginManager().registerEvents(snowballGun, thisPlugin);
		Bukkit.getPluginManager().registerEvents(mistleToe, thisPlugin);
		Bukkit.getPluginManager().registerEvents(christmasParty, thisPlugin);
		Bukkit.getPluginManager().registerEvents(snowTime, thisPlugin);
		Bukkit.getPluginManager().registerEvents(enchantedPartcles, thisPlugin);
		Bukkit.getPluginManager().registerEvents(newYearsParty, thisPlugin);
		Bukkit.getPluginManager().registerEvents(partyPopper, thisPlugin);
		// bosses
		Bukkit.getPluginManager().registerEvents(bossLoader, thisPlugin);
		Bukkit.getPluginManager().registerEvents(bossSummoner, thisPlugin);
		Bukkit.getPluginManager().registerEvents(bossDrops, thisPlugin);
		Bukkit.getPluginManager().registerEvents(spiderBoss, thisPlugin);
		Bukkit.getPluginManager().registerEvents(zombieBoss, thisPlugin);
		Bukkit.getPluginManager().registerEvents(polarBearBoss, thisPlugin);
		Bukkit.getPluginManager().registerEvents(pumpkinBoss, thisPlugin);
		Bukkit.getPluginManager().registerEvents(creeperBoss, thisPlugin);
		
		// register command executors
		this.getCommand("spiderboss").setExecutor(spiderBoss);
		this.getCommand("zombieboss").setExecutor(zombieBoss);
		this.getCommand("polarbearboss").setExecutor(polarBearBoss);
		this.getCommand("pumpkinboss").setExecutor(pumpkinBoss);
		this.getCommand("creeperboss").setExecutor(creeperBoss);
		this.getCommand("setcolor").setExecutor(playerChat);
		this.getCommand("listcolors").setExecutor(playerChat);
		this.getCommand("info").setExecutor(serverInfo);
		this.getCommand("c").setExecutor(playerSleep);
		this.getCommand("setmotd").setExecutor(serverMOTD);
		this.getCommand("msg").setExecutor(messageCommandExecutor);
		this.getCommand("r").setExecutor(messageCommandExecutor);
		this.getCommand("isee").setExecutor(inventorySnooping);
		this.getCommand("esee").setExecutor(inventorySnooping);
		this.getCommand("lag").setExecutor(grieferLagHell);
		
		// check if structure folders exist
		File structuresDir = new File("Structures");
		if (structuresDir.exists() == false){
			structuresDir.mkdirs(); // create the folder
		}
		structures = structuresDir.listFiles();
		numStructures = structures.length;
		File undergroundStructuresDir = new File("StructuresUnderground");
		if (undergroundStructuresDir.exists() == false){
			undergroundStructuresDir.mkdirs(); // create the folder
		}
		undergroundStructures = undergroundStructuresDir.listFiles();
		numUndergroundStructures = undergroundStructures.length;
		
		// setup the block populator for custom/main world
		//worldTools.createWorld("world_");
		//worldTools.loadWorld("world_");
		customStructures = new CustomStructures();
		World mainWorld = Bukkit.getWorlds().get(0);
		mainWorld.getPopulators().clear();
		mainWorld.getPopulators().add(customStructures);
		mainWorld.setDifficulty(Difficulty.HARD);

		// startup runnable events
		
	}
	
	@Override
	public void onDisable(){
		// loop through all players making sure all are visible
		for (Player player : Bukkit.getOnlinePlayers()){
			if (player.isOnline()){
				for (Player otherPlayer : Bukkit.getOnlinePlayers()){
					if (otherPlayer.isOnline()){
						player.showPlayer(otherPlayer);
					}
				}
			}
		}
		
		powderOfBlockMorphing.stopAll();
	}
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if (cmd.getName().equalsIgnoreCase("tools")){
			if ( !(sender instanceof Player)){
				sender.sendMessage("Must be player to use this command.");
				return true;
			}
			Player player = (Player) sender;
			if (!player.isOp()){
				sender.sendMessage("Must be OP to use this command.");
				return true;
			}
			
			ItemStack wand1 = new ItemStack(Material.DIAMOND_HOE, 1);
			ItemMeta meta1 = wand1.getItemMeta();
			meta1.setDisplayName(ChatColor.DARK_RED + "Section Selector");
			wand1.setItemMeta(meta1);
			
			ItemStack wand2 = new ItemStack(Material.EMERALD, 1);
			ItemMeta meta2 = wand2.getItemMeta();
			meta2.setDisplayName(ChatColor.DARK_RED + "Info");
			wand2.setItemMeta(meta2);
			
			ItemStack wand3 = new ItemStack(Material.EMERALD, 1);
			ItemMeta meta3 = wand2.getItemMeta();
			meta3.setDisplayName(ChatColor.DARK_RED + "Outline Chunk");
			wand3.setItemMeta(meta3);
			
			player.getInventory().addItem(wand1);
			player.getInventory().addItem(wand2);
			player.getInventory().addItem(wand3);
			return true;
		}
		else if (cmd.getName().equalsIgnoreCase("getrelics")){
			if ( !(sender instanceof Player)){
				sender.sendMessage("Must be player to use this command.");
				return true;
			}
			Player player = (Player) sender;
			if (!player.isOp()){
				sender.sendMessage("Must be OP to use this command.");
				return true;
			}
			
			for (ItemStack stack : relics){
				player.getInventory().addItem(stack);
			}
			return true;
		}
		else if (cmd.getName().equalsIgnoreCase("getitems")){
			if ( !(sender instanceof Player)){
				sender.sendMessage("Must be player to use this command.");
				return true;
			}
			Player player = (Player) sender;
			if (!player.isOp()){
				sender.sendMessage("Must be OP to use this command.");
				return true;
			}
			
			for (ItemStack stack : items){
				player.getInventory().addItem(stack);
			}
			return true;
		}
		else if (cmd.getName().equalsIgnoreCase("save")){
			if ( !(sender instanceof Player)){
				sender.sendMessage("Must be player to use this command.");
				return true;
			}
			Player player = (Player) sender;
			if (!player.isOp()){
				sender.sendMessage("Must be OP to use this command.");
				return true;
			}
			if (args.length < 2){
				sender.sendMessage("Must specify a file name! Ex: /save test 4");
				sender.sendMessage("To save the structure to test.txt with structure ground at y=4");
				return true;
			}
			if ( (x1 == 0) && (x2 == 0)  && (y1 == 0) && (y2 == 0) && (z1 == 0) && (z2 == 0)){
				sender.sendMessage("One or more positions were not defined.");
				return true;
			}
			String fileName = "Structures\\" + args[0] + ".txt";
			int groundY = Integer.valueOf(args[1]);
			
			// check if file already exists
			File file = new File(fileName);
			if (file.exists()){
				player.sendMessage(ChatColor.RED + fileName + " already exists and will be Overwritten");
				file.delete();
			}
			
			// prep for structure saving
			int startX, startY, startZ;
			int endX, endY, endZ;
			int offX, offY, offZ;
			int numBlocks = 0;
			World world = player.getWorld();
			Location location;
			Block temp;
			if (x1 < x2){
				startX = x1;
				endX = x2;
			}
			else{
				startX = x2;
				endX = x1;
			}
			
			if (y1 < y2){
				startY = y1;
				endY = y2;
			}
			else{
				startY = y2;
				endY = y1;
			}
			
			if (z1 < z2){
				startZ = z1;
				endZ = z2;
			}
			else{
				startZ = z2;
				endZ = z1;
			}
			
			player.sendMessage(ChatColor.GRAY + "Writing blocks to " + fileName + "...");
			try{
				FileWriter fileWriter = new FileWriter(fileName, true); // open the file name in fileName for writing in append mode
				BufferedWriter bw = new BufferedWriter(fileWriter); // create new buffered writer using the fileWriter object
				
				// loop though all the blocks writing them to the file with the buffered file writer
				for (int y = startY; y <= endY; y++){
					for (int x = startX; x <= endX; x++){
						for (int z = startZ; z <= endZ; z++){
							location = new Location(world, (double)x, (double)y, (double)z );
							offX = x - startX;
							offY = y - groundY;
							offZ = z - startZ;
							temp = world.getBlockAt(location);
							if (temp.getType() == Material.SKULL){
								Skull skull = (Skull) temp.getState();
								bw.write(offX + ":" + offY + ":" + offZ + ":" + temp.getTypeId() + ":" + temp.getData() + ":" + skull.getSkullType().name() + ":" + skull.getRotation().name());
							}
							else if (temp.getType() == Material.SIGN_POST || temp.getType() == Material.WALL_SIGN || temp.getType() == Material.SIGN){
								Sign sign = (Sign) temp.getState();
								bw.write(offX + ":" + offY + ":" + offZ + ":" + temp.getTypeId() + ":" + temp.getData() + ":\"" + sign.getLine(0) + "\":\"" + sign.getLine(1) + "\":\"" + sign.getLine(2) + "\":\"" + sign.getLine(3) + "\"");
							}
							else{
								bw.write(offX + ":" + offY + ":" + offZ + ":" + temp.getTypeId() + ":" + temp.getData());
							}
							bw.newLine();
							numBlocks++;
						}
					}
				}
				
				bw.flush(); // flush out any remaining text from the buffer into the file
				bw.close(); // close the file
			} catch(Exception ex){
				System.out.println("--------------------------------------------");
				System.out.println("Exception while writing to file: " + ex.getMessage());
				System.out.println("--------------------------------------------");
				ex.printStackTrace();
			}
			player.sendMessage(ChatColor.GRAY + "Finished writing " + numBlocks + " blocks to the file " + fileName);
			
			return true;
		}
		else if (cmd.getName().equalsIgnoreCase("load")){
			if ( !(sender instanceof Player)){
				sender.sendMessage("Must be player to use this command.");
				return true;
			}
			if (args.length < 1){
				sender.sendMessage("Must specify a file name! Ex: /load test");
				sender.sendMessage("To load the structure to test.txt");
				return true;
			}
			
			Player player = (Player) sender;
			if (!player.isOp()){
				sender.sendMessage("Must be OP to use this command.");
				return true;
			}
			File structuresDir = new File("Structures");
			if (structuresDir.exists() == false){
				player.sendMessage("Structures directory doesn't exist!");
				return true;
			}
			File structureFile = null;
			for (File file : structuresDir.listFiles()){
				if (file.getName().equals(args[0] + ".txt")){
					structureFile = file;
				}
			}
			String fileName = args[0] + ".txt";
			
			// check if file already exists
			if (structureFile == null){
				player.sendMessage(fileName + " doesn't exist!");
				return true;
			}
			if (structureFile.exists() == false){
				player.sendMessage(fileName + " doesn't exist!");
				return true;
			}
			
			// prep for structure loading
			int startX, startY, startZ;
			int x, y, z;
			//Material blockMaterial;
			int blockID;
			Byte blockData;
			int containedBlockID;
			Byte containedBlockData;
			int numBlocks = 0;
			ArrayList<String> theAttachedBlocks = new ArrayList<String>();
			World world = player.getWorld();
			Location location;
			Block block;
			Material blockMaterial;
			
			startX = player.getLocation().getBlockX();
			startY = player.getLocation().getBlockY();
			startZ = player.getLocation().getBlockZ();
			
			player.sendMessage(ChatColor.GRAY + "Reading blocks from " + fileName + "...");
			try{
				FileReader fileReader = new FileReader(structureFile);
				BufferedReader br = new BufferedReader(fileReader);
				String line;
				String[] part;
				while ((line = br.readLine()) != null) {
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
						
						blockMaterial = Material.getMaterial(blockID);
						if (attachedBlocks.contains(blockMaterial)){
							theAttachedBlocks.add(line);
							continue; // skip this block for now because it's attached to something else
						}
						
						location = new Location(world, x, y, z);
						block = location.getBlock();
						block.setTypeId(blockID, false); // don't update nearby blocks when setting type
						block.setData(blockData, false); // don't update nearby blocks when setting data
					}
					else if (part.length == 7){
						// flower pot blocks -- unused for now until the FlowerPot API is fixed
						x = startX + Integer.valueOf(part[0]);
						y = startY + Integer.valueOf(part[1]);
						z = startZ + Integer.valueOf(part[2]);
						blockID = Integer.valueOf(part[3]);
						blockData = Byte.valueOf(part[4]);
						containedBlockID = Integer.valueOf(part[5]);
						containedBlockData = Byte.valueOf(part[6]);
						location = new Location(world, x, y, z);
						block = location.getBlock();
						block.setTypeId(blockID, false);
						block.setData(blockData, false);
						FlowerPot pot = (FlowerPot) block.getState().getData();
						pot.setContents( new MaterialData(containedBlockID, containedBlockData) );
					}
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
				else if (part.length == 7){
					// flower pot blocks -- unused for now until the FlowerPot API is fixed
					x = startX + Integer.valueOf(part[0]);
					y = startY + Integer.valueOf(part[1]);
					z = startZ + Integer.valueOf(part[2]);
					blockID = Integer.valueOf(part[3]);
					blockData = Byte.valueOf(part[4]);
					containedBlockID = Integer.valueOf(part[5]);
					containedBlockData = Byte.valueOf(part[6]);
					location = new Location(world, x, y, z);
					block = location.getBlock();
					block.setTypeIdAndData(blockID, blockData, false); // set the block id and data, and ignore physics
					FlowerPot pot = (FlowerPot) block.getState().getData();
					pot.setContents( new MaterialData(containedBlockID, containedBlockData) );
				}
			}
			player.sendMessage(ChatColor.GRAY + "Finished reading " + numBlocks + " blocks from the file " + fileName);
			
			return true;
		}
		else if (cmd.getName().equalsIgnoreCase("warps")){
			if ( !(sender instanceof Player)){
				sender.sendMessage("Must be player to use this command.");
				return true;
			}
			if (!((Player)sender).isOp()){
				sender.sendMessage("Must be OP to use this command.");
				return true;
			}
			sender.sendMessage(ChatColor.GRAY + "Currently loaded worlds: ");
			for (World world : Bukkit.getWorlds()){
				sender.sendMessage(ChatColor.GRAY + world.getName());
			}
			return true;
		}
		else if (cmd.getName().equalsIgnoreCase("warp")){
			if ( !(sender instanceof Player)){
				sender.sendMessage("Must be player to use this command.");
				return true;
			}
			if (!((Player)sender).isOp()){
				sender.sendMessage("Must be OP to use this command.");
				return true;
			}
			if (args.length < 1){
				sender.sendMessage("Must specify a world to warp to. Ex: /warp world");
				return true;
			}
			if (worldTools.checkWorldExists(args[0]) == false){
				sender.sendMessage("The world " + args[0] + " doesn't exist!");
				return true;
			}
			// load the world if it isn't already loaded
			worldTools.loadWorld(args[0]);
			Player player = (Player) sender;
			World world = Bukkit.getWorld(args[0]);
			Location spawnLocation = world.getSpawnLocation();
			if (spawnLocation != null){
				player.teleport(spawnLocation);
			}
			else{
				player.teleport(new Location(world, 0, 10, 0));
			}
			return true;
		}
		else if (cmd.getName().equalsIgnoreCase("locate")){
			if (!sender.isOp()){
				sender.sendMessage("Must be OP to use this command.");
				return true;
			}
			
			for (Player player : Bukkit.getServer().getOnlinePlayers()){
				sender.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + player.getName() + ": " + player.getWorld().getName() + " " + player.getLocation().getBlockX() + " " + player.getLocation().getBlockY() + " " + player.getLocation().getBlockZ());
			}
			return true;
		}
		else if (cmd.getName().equalsIgnoreCase("vanish")){
			if (!(sender instanceof Player)){
				sender.sendMessage("Must be player to use this command.");
				return true;
			}
			if (!sender.isOp()){
				sender.sendMessage("Must be OP to use this command.");
				return true;
			}
			
			Player target = (Player) sender;
			
			for (Player player : Bukkit.getServer().getOnlinePlayers()){
				player.hidePlayer(target);
			}
			
			sender.sendMessage("You should now be vanished to all online players.");
			return true;
		}
		else if (cmd.getName().equalsIgnoreCase("unvanish")){
			if (!(sender instanceof Player)){
				sender.sendMessage("Must be player to use this command.");
				return true;
			}
			if (!sender.isOp()){
				sender.sendMessage("Must be OP to use this command.");
				return true;
			}
			
			Player target = (Player) sender;
			
			for (Player player : Bukkit.getServer().getOnlinePlayers()){
				player.showPlayer(target);
			}
			
			sender.sendMessage("You should now be visible to all online players.");
			return true;
		}
		else if (cmd.getName().equalsIgnoreCase("ping")){
			if (!(sender instanceof Player)){
				sender.sendMessage("Must be player to use this command.");
				return true;
			}
			Player player = (Player) sender;
			player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Ping: " + getPing(player) + "ms");
			return true;
		}
		else if (cmd.getName().equalsIgnoreCase("pings")){
			if (!(sender instanceof Player)){
				sender.sendMessage("Must be player to use this command.");
				return true;
			}
			Player cmdSender = (Player) sender;
			for (Player player : Bukkit.getOnlinePlayers()){
				if (player.isOnline()){
					cmdSender.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + player.getName() + " Ping: " + getPing(player) + "ms");
				}
			}
			return true;
		}
		else if (cmd.getName().equalsIgnoreCase("nearby")){
			if (!(sender instanceof Player)){
				sender.sendMessage("Must be player to use this command.");
				return true;
			}
			Player player = (Player) sender;
			for (Entity entity : player.getNearbyEntities(250.0, 250.0, 250.0)){
				if (entity.getCustomName() != null){
					Location location = entity.getLocation();
					player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + entity.getCustomName() + " (" + entity.getType().name() + ") at " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ()); 
				}
			}
			return true;
		}
		else if (cmd.getName().equalsIgnoreCase("usage")){
			float using = Runtime.getRuntime().freeMemory();
			using = (float) (Math.round((using/1000000)*100.0) / 100.0);
			float total = Runtime.getRuntime().totalMemory();
			total = (float) (Math.round((total/1000000)*100.0) / 100.0);
			float max = Runtime.getRuntime().maxMemory();
			max = (float) (Math.round((max/1000000)*100.0) / 100.0);
			
			sender.sendMessage(ChatColor.GREEN + "Using: " + using + " MB of " + total + " MB allocated");
			sender.sendMessage(ChatColor.GREEN + "Max: " + max + " MB");
			sender.sendMessage(ChatColor.GREEN + "Available Processors(Cores): " + Runtime.getRuntime().availableProcessors());
			sender.sendMessage(ChatColor.GREEN + "OS Name: " + System.getProperty("os.name"));
			sender.sendMessage(ChatColor.GREEN + "OS Version: " + System.getProperty("os.version"));
			sender.sendMessage(ChatColor.GREEN + "OS Arch: " + System.getProperty("os.arch"));
			return true;
		}
		else if (cmd.getName().equalsIgnoreCase("b")){
			World mainWorld = Bukkit.getWorlds().get(0);
			sender.sendMessage("Block Populators: ");
			for (BlockPopulator blockPopulator : mainWorld.getPopulators()){
				sender.sendMessage(blockPopulator.toString());
			}
			sender.sendMessage("CustomStructures on: " + mainWorld.getPopulators().contains(customStructures));
			sender.sendMessage("Num newly generated chunks: " + customStructures.numChunksGenerated);
			sender.sendMessage("Num newly generated structures: " + customStructures.numStructuresGenerated);
			sender.sendMessage("Structure Per Chunk: " + (float)customStructures.numStructuresGenerated/customStructures.numChunksGenerated);
			return true;
		}
		else if (cmd.getName().equalsIgnoreCase("test")){
			if (!(sender instanceof Player)){
				sender.sendMessage("Must be player to use this command.");
				return true;
			}
			if (!sender.isOp()){
				sender.sendMessage("Must be OP to use this command.");
				return true;
			}
			Player player = (Player) sender;
			
			/*
			player.getInventory().addItem(snowballGun.getRelic());
			player.getInventory().addItem(mistleToe.getRelic());
			player.getInventory().addItem(christmasParty.getRelic());
			player.getInventory().addItem(snowTime.getRelic());
			player.getInventory().addItem(enchantedPartcles.getRelic());
			player.getInventory().addItem(newYearsParty.getRelic());
			player.getInventory().addItem(partyPopper.getRelic());
			*/
			
			@SuppressWarnings("unused")
			TopHatWatcher watcher = new TopHatWatcher(player);
			
			/*
			for (int i = 0; i < 8; i++){
				Pig pig = (Pig) player.getWorld().spawnEntity(player.getLocation().add(0.0, 3.0, 0.0), EntityType.PIG);
				pig.setBaby();
				pig.setGravity(false);
				pig.setLeashHolder(player);
				@SuppressWarnings("unused")
				BalloonAnimalWatcher watcher = new BalloonAnimalWatcher(pig, player);
			}
			*/
			
			
			return true;
		}
		
		return false;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e){
		if ( (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) 
				&& (e.getPlayer().getItemInHand().hasItemMeta()) 
				&& (e.getPlayer().getItemInHand().getItemMeta().getDisplayName() != null) 
				&& (e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.DARK_RED + "Section Selector")) ){
			e.setCancelled(true);
			x1 = e.getClickedBlock().getX();
			y1 = e.getClickedBlock().getY();
			z1 = e.getClickedBlock().getZ();
			e.getPlayer().sendMessage(ChatColor.AQUA + "Position 1 set to x:" + x1 + " y:" + y1 + " z:" + z1);
		}
		else if ( (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) 
				&& (e.getPlayer().getItemInHand().hasItemMeta()) 
				&& (e.getPlayer().getItemInHand().getItemMeta().getDisplayName() != null) 
				&& (e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.DARK_RED + "Section Selector")) ){
			e.setCancelled(true);
			x2 = e.getClickedBlock().getX();
			y2 = e.getClickedBlock().getY();
			z2 = e.getClickedBlock().getZ();
			e.getPlayer().sendMessage(ChatColor.AQUA + "Position 2 set to x:" + x2 + " y:" + y2 + " z:" + z2);
		}
		else if ( (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) 
				&& (e.getPlayer().getItemInHand().hasItemMeta()) 
				&& (e.getPlayer().getItemInHand().getItemMeta().getDisplayName() != null) 
				&& (e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.DARK_RED + "Info")) ){
			e.setCancelled(true);
			e.getPlayer().sendMessage(ChatColor.AQUA + "Block: " + e.getClickedBlock().getType().name() + " (" + e.getClickedBlock().getTypeId() + ") -- Data: " + e.getClickedBlock().getData());
			e.getPlayer().sendMessage(ChatColor.GRAY + "Material Data: " + e.getClickedBlock().getState().getData() + " -- Raw Data: " + e.getClickedBlock().getState().getRawData());
		}
		else if ( (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) 
				&& (e.getPlayer().getItemInHand().hasItemMeta()) 
				&& (e.getPlayer().getItemInHand().getItemMeta().getDisplayName() != null) 
				&& (e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.DARK_RED + "Outline Chunk")) ){
			e.setCancelled(true);
			
			int x1 = e.getClickedBlock().getX();
			int y1 = e.getClickedBlock().getY();
			int z1 = e.getClickedBlock().getZ();
			Location temp;
			World world = e.getPlayer().getWorld();
			
			for (int x = x1; x <= x1+17; x++){
				temp = new Location(world, x, y1, z1);
				temp.getBlock().setType(Material.STAINED_CLAY);
				temp.getBlock().setData((byte)14);
			}
			
			for (int x = x1; x <= x1+17; x++){
				temp = new Location(world, x, y1, z1+17);
				temp.getBlock().setType(Material.STAINED_CLAY);
				temp.getBlock().setData((byte)14);
			}
			
			for (int z = z1; z <= z1+17; z++){
				temp = new Location(world, x1, y1, z);
				temp.getBlock().setType(Material.STAINED_CLAY);
				temp.getBlock().setData((byte)14);
			}
			
			for (int z = z1; z <= z1+17; z++){
				temp = new Location(world, x1+17, y1, z);
				temp.getBlock().setType(Material.STAINED_CLAY);
				temp.getBlock().setData((byte)14);
			}
		}
	}
	
	@EventHandler
	public void onEntitySpawn(CreatureSpawnEvent e){
		if (e.getSpawnReason() == SpawnReason.SPAWNER && e.getEntityType() == EntityType.PIG){
			e.setCancelled(true);
		}
	}
	
	public int getPing(Player p) {
		try {
			Class<?> CPClass;
			String serverName = Bukkit.getServer().getClass().getPackage().getName();
			String serverVersion = serverName.substring(serverName.lastIndexOf(".") + 1, serverName.length());
			CPClass = Class.forName("org.bukkit.craftbukkit." + serverVersion + ".entity.CraftPlayer");
			Object CraftPlayer = CPClass.cast(p);

			Method getHandle = CraftPlayer.getClass().getMethod("getHandle", new Class[0]);
			Object EntityPlayer = getHandle.invoke(CraftPlayer, new Object[0]);

			Field ping = EntityPlayer.getClass().getDeclaredField("ping");

			return ping.getInt(EntityPlayer);
		} catch (Exception e) {
				e.printStackTrace();
		}
		return 0;
	}
}
