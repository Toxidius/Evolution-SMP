package Evolution.Relics;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.PolarBear;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import Evolution.Main.Core;

public class AxeOfBeheading implements Listener{
	
	private ItemStack relic;
	private ItemStack book;
	
	public AxeOfBeheading(){
		relic = new ItemStack(Material.DIAMOND_AXE, 1);
		ItemMeta meta = relic.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Axe of Beheading");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.DARK_RED + "Ancient Relic!");
		lore.add(ChatColor.DARK_RED + "Legend has it this axe has the ability to");
		lore.add(ChatColor.DARK_RED + "behead a mob when it's killed. (Uncommon)");
		meta.setLore(lore);
		relic.setItemMeta(meta);
		// Core.relics.add(relic); // we don't want this in the list of relics because then it'll drop from bosses
		
		book = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta bookMeta = (BookMeta) book.getItemMeta();
		bookMeta.setTitle(ChatColor.GOLD + "Legend of the Beheading Axe");
		bookMeta.setAuthor("Toxidius");
		bookMeta.addPage(ChatColor.BOLD + "Legend\n" + ChatColor.RESET + "" + ChatColor.BLACK + "It has been said that long ago a mystical warrior once roamed Minecraftia. In his quest to conquer all the beasts that once roamed this land he came across this very axe. Legend has it that this axe possesses special abilities.",
				ChatColor.BLACK + "It has the ability to capture monsters souls in a head that is dropped on their death. These monsters can then be summoned back into the world with statue made out of Emerald Blocks. Similar in shape to summoning a wither with a single head on top. ",
				ChatColor.BLACK + "Be weary, however, as it's said that summoning these beasts back into the world will cause them to mutate into their final form.");
		book.setItemMeta(bookMeta);
	}
	
	public ItemStack getRelic(){
		return relic.clone();
	}
	
	public ItemStack getBook(){
		return book.clone();
	}
	
	// drop head code and the drop axe code
	@EventHandler
	public void onMobKillWithAxe(EntityDamageByEntityEvent e){
		if (e.isCancelled())
			return;
		if (e.getEntity() instanceof Monster 
				&& e.getDamager() instanceof Player
				&& ((LivingEntity)e.getEntity()).getHealth()-e.getFinalDamage() <= 0.0
				&& ((LivingEntity)e.getEntity()).isDead() == false){
			// this entity is in the process of being killed given they arn't dead yet
			// check if they were killed using a beheading axe
			Player player = (Player) e.getDamager();
			ItemStack itemInHand = player.getInventory().getItemInMainHand();
			if (itemInHand != null
					&& itemInHand.hasItemMeta()
					&& itemInHand.getItemMeta().hasDisplayName()
					&& itemInHand.getItemMeta().getDisplayName().equals(ChatColor.RED + "Axe of Beheading")){
				// this entity was killed using beheading axe
				LivingEntity killed = (LivingEntity) e.getEntity();
				
				if (killed instanceof Zombie){
					if (Math.random() <= (1F/75)){ // 1 out of 75 chance of dropping a skull
						dropMobHead(killed);
					}
				}
				else if (killed instanceof Skeleton){
					if (Math.random() <= (1F/75)){ // 1 out of 75 chance of dropping a skull
						dropMobHead(killed);
					}
				}
				else if (killed instanceof Creeper){
					if (Math.random() <= (1F/75)){ // 1 out of 75 chance of dropping a skull
						dropMobHead(killed);
					}
				}
				else if (killed instanceof Spider){
					if (Math.random() <= (1F/50)){ // 1 out of 50 chance of dropping a skull
						dropMobHead(killed);
					}
				}
				else if (killed instanceof PolarBear){
					if (Math.random() <= (1F/10)){ // 1 out of 10 chance of dropping a skull
						dropMobHead(killed);
					}
				}
				else if (killed instanceof Slime){
					if (Math.random() <= (1F/75)){ // 1 out of 75 chance of dropping a skull
						dropMobHead(killed);
					}
				}
				else{
					// do nothing
				}
			}
			else{
				// mob not killed with beheading axe
				// random chance of dropping axe of beheading
				if (Math.random() <= 0.0025){ // 1 out of 400 chance of dropping axe
					// drop axe
					e.getEntity().getWorld().dropItem(e.getEntity().getLocation(), Core.axeOfBeheading.getRelic().clone());
					// drop "quest" book
					e.getEntity().getWorld().dropItem(e.getEntity().getLocation(), Core.axeOfBeheading.getBook().clone());
				}
			}
			
		}
		// end if
	}
	
	// drop head a mob is wearing if killed with axe
	@EventHandler
	public void onMobWithHeadKillWithAxe(EntityDamageByEntityEvent e){
		if (e.isCancelled())
			return;
		if (e.getEntity() instanceof Monster 
				&& (e.getEntity() instanceof Zombie || e.getEntity() instanceof Skeleton)
				&& e.getDamager() instanceof Player
				&& ((LivingEntity)e.getEntity()).getHealth()-e.getFinalDamage() <= 0.0
				&& ((LivingEntity)e.getEntity()).isDead() == false
				&& ((Monster)e.getEntity()).getEquipment().getHelmet() != null
				&& ((Monster)e.getEntity()).getEquipment().getHelmet().getType() == Material.SKULL_ITEM){
			// this entity is in the process of being killed given they arn't dead yet
			// check if they were killed using a beheading axe
			Player player = (Player) e.getDamager();
			ItemStack itemInHand = player.getInventory().getItemInMainHand();
			if (itemInHand != null
					&& itemInHand.hasItemMeta()
					&& itemInHand.getItemMeta().hasDisplayName()
					&& itemInHand.getItemMeta().getDisplayName().equals(ChatColor.RED + "Axe of Beheading")){
				// this mob with a head was killed with beheading axe
				// 50% chance of dropping that head
				if (Math.random() < 0.5){
					e.getEntity().getWorld().dropItem(e.getEntity().getLocation(), ((Monster)e.getEntity()).getEquipment().getHelmet());
				}
			}
		}
	}
	
	public void dropMobHead(LivingEntity entity){
		ItemStack head = null;
		if (entity instanceof Zombie){
			head = new ItemStack(Material.SKULL_ITEM, 1, (byte)2); // zombie skull item stack
		}
		else if (entity instanceof Skeleton){
			if (((Skeleton)entity).getType() == EntityType.SKELETON){
				// old code to check type: (((Skeleton)entity).getSkeletonType() == SkeletonType.NORMAL)
				head = new ItemStack(Material.SKULL_ITEM, 1, (byte)0); // skeleton skull item stack
			}
			else{
				head = new ItemStack(Material.SKULL_ITEM, 1, (byte)1); // wither skeleton skull item stack
			}
		}
		else if (entity instanceof Creeper){
			head = new ItemStack(Material.SKULL_ITEM, 1, (byte)4); // creeper skull item stack
		}
		else if (entity instanceof Spider){
			head = new ItemStack(Material.SKULL_ITEM, 1, (byte)3); // player skull item stack
			SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
			skullMeta.setOwner("MHF_Spider");
			skullMeta.setDisplayName(ChatColor.RESET + "Spider Head");
			head.setItemMeta(skullMeta);
		}
		else if (entity instanceof PolarBear){
			head = new ItemStack(Material.SKULL_ITEM, 1, (byte)3); // player skull item stack
			SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
			skullMeta.setOwner("polarbear9");
			skullMeta.setDisplayName(ChatColor.RESET + "Polar Bear Head");
			head.setItemMeta(skullMeta);
		}
		else if (entity instanceof Slime){
			head = new ItemStack(Material.SKULL_ITEM, 1, (byte)3); // player skull item stack
			SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
			skullMeta.setOwner("MHF_Slime");
			skullMeta.setDisplayName(ChatColor.RESET + "Slime Head");
			head.setItemMeta(skullMeta);
		}
		else{
			head = null;
		}
		
		if (head != null){
			entity.getLocation().getWorld().dropItem(entity.getLocation(), head);
		}
	}

}
