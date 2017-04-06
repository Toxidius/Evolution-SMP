package Evolution.GameMechanics;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import Evolution.Main.Core;

public class MobsWithHeads implements Listener{
	
	public MobsWithHeads(){
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onMonsterSpawnAddHead(EntitySpawnEvent e){
		if (e.getEntity() instanceof Monster 
				&& (e.getEntity() instanceof Zombie || e.getEntity() instanceof Skeleton) ){
			if (Math.random() <= 0.005){ // 0.0033 old
				// 1 in 200 chance of getting a custom head
				Monster monster = (Monster) e.getEntity();
				ItemStack skull = getRandomHead();
				
				// place a skull block below the mob of the head type it's wearing
				// we do this because there is a strange bug with skull skins not loading if not in block form
				if ( ((SkullMeta)skull.getItemMeta()).hasOwner() 
						&& (monster.getLocation().getBlock().getType() == Material.AIR || monster.getLocation().getBlock().getType() == Material.LONG_GRASS)){
					Block skullBlock = monster.getLocation().getBlock();
					skullBlock.setType(Material.SKULL);
					skullBlock.setData((byte)1);
					Skull skullState = (Skull) skullBlock.getState();
					skullState.setSkullType(SkullType.PLAYER);
					skullState.setOwner( ((SkullMeta)skull.getItemMeta()).getOwner() );
					skullState.update();
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(Core.thisPlugin, new Runnable(){
						@Override
						public void run() {
							skullBlock.setType(Material.AIR);
							
							monster.getEquipment().setHelmet(skull);
						}
					}, 10L);
				}
			}
		}
	}
	
	public ItemStack getRandomHead(){
		ItemStack head;
		
		if (Bukkit.getOnlinePlayers().size() >= 3){
			// online player head
			int random = Core.r.nextInt( Bukkit.getOnlinePlayers().size() );
			String name = ((Player)Bukkit.getOnlinePlayers().toArray()[random]).getName();
			
			head = new ItemStack(Material.SKULL_ITEM, 1, (byte)3); // player skull item stack
			SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
			skullMeta.setOwner(name);
			head.setItemMeta(skullMeta);
		}
		else{
			// random mob head
			int random = Core.r.nextInt(14); // 0 to 13
			SkullMeta skullMeta;
			
			switch(random){
				case 0:
					head = new ItemStack(Material.SKULL_ITEM, 1, (byte)3); // player skull item stack
					skullMeta = (SkullMeta) head.getItemMeta();
					skullMeta.setOwner("MHF_MushroomCow");
					head.setItemMeta(skullMeta);
					break;
				case 1:
					head = new ItemStack(Material.SKULL_ITEM, 1, (byte)3); // player skull item stack
					skullMeta = (SkullMeta) head.getItemMeta();
					skullMeta.setOwner("MHF_Cake");
					head.setItemMeta(skullMeta);
					break;
				case 2:
					head = new ItemStack(Material.SKULL_ITEM, 1, (byte)3); // player skull item stack
					skullMeta = (SkullMeta) head.getItemMeta();
					skullMeta.setOwner("MHF_Ghast");
					head.setItemMeta(skullMeta);
					break;
				case 3:
					head = new ItemStack(Material.SKULL_ITEM, 1, (byte)3); // player skull item stack
					skullMeta = (SkullMeta) head.getItemMeta();
					skullMeta.setOwner("MHF_Pig");
					head.setItemMeta(skullMeta);
					break;
				case 4:
					head = new ItemStack(Material.SKULL_ITEM, 1, (byte)3); // player skull item stack
					skullMeta = (SkullMeta) head.getItemMeta();
					skullMeta.setOwner("MHF_Slime");
					head.setItemMeta(skullMeta);
					break;
				case 5:
					head = new ItemStack(Material.SKULL_ITEM, 1, (byte)3); // player skull item stack
					skullMeta = (SkullMeta) head.getItemMeta();
					skullMeta.setOwner("MHF_Spider");
					head.setItemMeta(skullMeta);
					break;
				case 6:
					head = new ItemStack(Material.SKULL_ITEM, 1, (byte)3); // player skull item stack
					skullMeta = (SkullMeta) head.getItemMeta();
					skullMeta.setOwner("polarbear9"); // polar bear skin
					head.setItemMeta(skullMeta);
					break;
				case 7:
					head = new ItemStack(Material.SKULL_ITEM, 1, (byte)3); // player skull item stack
					skullMeta = (SkullMeta) head.getItemMeta();
					skullMeta.setOwner("MHF_Witch");
					head.setItemMeta(skullMeta);
					break;
				case 8:
					head = new ItemStack(Material.SKULL_ITEM, 1, (byte)3); // player skull item stack
					skullMeta = (SkullMeta) head.getItemMeta();
					skullMeta.setOwner("MHF_Villager");
					head.setItemMeta(skullMeta);
				case 9:
					head = new ItemStack(Material.SKULL_ITEM, 1, (byte)3); // player skull item stack
					skullMeta = (SkullMeta) head.getItemMeta();
					skullMeta.setOwner("MarkoGul"); // evil pumpkin skin
					head.setItemMeta(skullMeta);
					break;
				case 10:
					head = new ItemStack(Material.SKULL_ITEM, 1, (byte)3); // player skull item stack
					skullMeta = (SkullMeta) head.getItemMeta();
					skullMeta.setOwner("TrollerK1ng"); // troll skin
					head.setItemMeta(skullMeta);
					break;
				case 11:
					head = new ItemStack(Material.SKULL_ITEM, 1, (byte)2); // zombie skull item stack
					break;
				case 12:
					head = new ItemStack(Material.SKULL_ITEM, 1, (byte)4); // creeper skull item stack
					break;
				case 13:
					head = new ItemStack(Material.SKULL_ITEM, 1, (byte)0); // skeleton skull item stack
					break;
				default:
					head = new ItemStack(Material.SKULL_ITEM, 1, (byte)2); // zombie skull item stack
					break;
			}
		}
		
		return head;
	}

}
