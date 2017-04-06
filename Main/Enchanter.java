package Evolution.Main;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class Enchanter {
	
	private Random r;
	private ArrayList<Enchantment> damageEnchants;
	private ArrayList<Enchantment> swordEnchants;
	private ArrayList<Enchantment> pickaxeLootEnchants;
	private ArrayList<Enchantment> pickaxeEnchants;
	private ArrayList<Enchantment> shovelEnchants;
	private ArrayList<Enchantment> axeEnchants;
	private ArrayList<Enchantment> bowEnchants;
	private ArrayList<Enchantment> rodEnchants;
	
	private ArrayList<Enchantment> protectionEnchants;
	private ArrayList<Enchantment> chestplateEnchants;
	private ArrayList<Enchantment> helmetEnchants;
	private ArrayList<Enchantment> leggingsEnchants;
	private ArrayList<Enchantment> bootsEnchants;
	
	public Enchanter(){
		r = new Random();
		
		damageEnchants = new ArrayList<Enchantment>();
		damageEnchants.add(Enchantment.DAMAGE_ALL);
		damageEnchants.add(Enchantment.DAMAGE_ARTHROPODS);
		damageEnchants.add(Enchantment.DAMAGE_UNDEAD);
		
		swordEnchants = new ArrayList<Enchantment>();
		swordEnchants.add(Enchantment.DURABILITY);
		swordEnchants.add(Enchantment.LOOT_BONUS_MOBS);
		swordEnchants.add(Enchantment.FIRE_ASPECT);
		
		pickaxeLootEnchants = new ArrayList<Enchantment>();
		pickaxeLootEnchants.add(Enchantment.LOOT_BONUS_BLOCKS);
		pickaxeLootEnchants.add(Enchantment.SILK_TOUCH);
		
		pickaxeEnchants = new ArrayList<Enchantment>();
		pickaxeEnchants.add(Enchantment.DURABILITY);
		pickaxeEnchants.add(Enchantment.DIG_SPEED);
		
		shovelEnchants = new ArrayList<Enchantment>();
		shovelEnchants.add(Enchantment.DURABILITY);
		shovelEnchants.add(Enchantment.DIG_SPEED);
		
		axeEnchants = new ArrayList<Enchantment>();
		axeEnchants.add(Enchantment.DURABILITY);
		axeEnchants.add(Enchantment.DIG_SPEED);
		
		rodEnchants = new ArrayList<Enchantment>();
		rodEnchants.add(Enchantment.DURABILITY);
		rodEnchants.add(Enchantment.LURE);
		axeEnchants.add(Enchantment.LUCK);
		
		bowEnchants = new ArrayList<Enchantment>();
		bowEnchants.add(Enchantment.DURABILITY);
		bowEnchants.add(Enchantment.ARROW_DAMAGE);
		bowEnchants.add(Enchantment.ARROW_INFINITE);
		bowEnchants.add(Enchantment.ARROW_KNOCKBACK);
		bowEnchants.add(Enchantment.ARROW_FIRE);
		
		protectionEnchants = new ArrayList<Enchantment>();
		protectionEnchants.add(Enchantment.PROTECTION_ENVIRONMENTAL);
		protectionEnchants.add(Enchantment.PROTECTION_EXPLOSIONS);
		protectionEnchants.add(Enchantment.PROTECTION_FIRE);
		protectionEnchants.add(Enchantment.PROTECTION_PROJECTILE);
		
		chestplateEnchants = new ArrayList<Enchantment>(); // TODO add mending enchants for all items
		chestplateEnchants.add(Enchantment.DURABILITY);
		chestplateEnchants.add(Enchantment.THORNS);
		
		helmetEnchants = new ArrayList<Enchantment>();
		helmetEnchants.addAll(chestplateEnchants);
		helmetEnchants.add(Enchantment.OXYGEN);
		helmetEnchants.add(Enchantment.WATER_WORKER);
		
		leggingsEnchants = new ArrayList<Enchantment>();
		leggingsEnchants.addAll(chestplateEnchants);
		
		bootsEnchants = new ArrayList<Enchantment>();
		bootsEnchants.addAll(chestplateEnchants);
		bootsEnchants.add(Enchantment.DEPTH_STRIDER);
	}
	
	public enum ItemType{
		Tool, Armor, Other;
	}
	
	public ItemStack enchant(ItemStack baseItem, int numEnchantments, boolean isOP){
		Enchantment enchantToAdd = null;
		int levelToAdd;
		ItemType itemType = getItemType(baseItem);
		if (itemType == ItemType.Other){
			return baseItem; // don't add anything as it's some other item than I've programmed for
		}
		
		// add specified number of enchantments to the baseItem
		for (int i = 0; i < numEnchantments; i++){
			if (itemType == ItemType.Armor){
				for (int j = 0; j < 5; j++){ // try 5 times to find an applicable enchantment
					enchantToAdd = getRandomArmorEnchatment(baseItem);
					if (baseItem.containsEnchantment(enchantToAdd) == false){
						break; // found a good enchant
					}
				}
				levelToAdd = getRandomEnchantLevel(enchantToAdd, isOP); // get a level for it
				baseItem.addEnchantment(enchantToAdd, levelToAdd); // add the enchantment to the base item
			}
			else{
				for (int j = 0; j < 5; j++){ // try 5 times to find an applicable enchantment
					enchantToAdd = getRandomToolEnchatment(baseItem);
					if (baseItem.containsEnchantment(enchantToAdd) == false){
						break; // found a good enchant
					}
				}
				levelToAdd = getRandomEnchantLevel(enchantToAdd, isOP); // get a level for it
				baseItem.addEnchantment(enchantToAdd, levelToAdd); // add the enchantment to the base item
			}
		}
		
		return baseItem; // return the now enchanted itemstack
	}
	
	public ItemType getItemType(ItemStack baseItem){
		Material material = baseItem.getType();
		if (material == Material.DIAMOND_HELMET || material == Material.IRON_HELMET || material == Material.GOLD_HELMET || material == Material.LEATHER_HELMET || material == Material.CHAINMAIL_HELMET){
			return ItemType.Armor;
		}
		else if (material == Material.DIAMOND_CHESTPLATE || material == Material.IRON_CHESTPLATE || material == Material.GOLD_CHESTPLATE || material == Material.LEATHER_CHESTPLATE || material == Material.CHAINMAIL_CHESTPLATE){
			return ItemType.Armor;
		}
		else if (material == Material.DIAMOND_LEGGINGS || material == Material.IRON_LEGGINGS || material == Material.GOLD_LEGGINGS || material == Material.LEATHER_LEGGINGS || material == Material.CHAINMAIL_LEGGINGS){
			return ItemType.Armor;
		}
		else if (material == Material.DIAMOND_BOOTS || material == Material.IRON_BOOTS || material == Material.GOLD_BOOTS || material == Material.LEATHER_BOOTS || material == Material.CHAINMAIL_BOOTS){
			return ItemType.Armor;
		}
		else if (material == Material.DIAMOND_SWORD || material == Material.IRON_SWORD || material == Material.GOLD_SWORD || material == Material.STONE_SWORD || material == Material.WOOD_SWORD){
			return ItemType.Tool;
		}
		else if (material == Material.DIAMOND_PICKAXE || material == Material.IRON_PICKAXE || material == Material.GOLD_PICKAXE || material == Material.STONE_PICKAXE || material == Material.WOOD_PICKAXE){
			return ItemType.Tool;
		}
		else if (material == Material.DIAMOND_AXE || material == Material.IRON_AXE || material == Material.GOLD_AXE || material == Material.STONE_AXE || material == Material.WOOD_AXE){
			return ItemType.Tool;
		}
		else if (material == Material.DIAMOND_SPADE || material == Material.IRON_SPADE || material == Material.GOLD_SPADE || material == Material.GOLD_SPADE || material == Material.WOOD_SPADE){
			return ItemType.Tool;
		}
		else if (material == Material.BOW){
			return ItemType.Tool;
		}
		else if (material == Material.FISHING_ROD){
			return ItemType.Tool;
		}
		else{
			return ItemType.Other;
		}
	}
	
	public Enchantment getRandomArmorEnchatment(ItemStack baseItem){
		Enchantment enchant;
		Material material = baseItem.getType();
		if (hasProtectionEnchant(baseItem) == false){
			enchant = protectionEnchants.get(r.nextInt(protectionEnchants.size())); // add protection enchant first
			return enchant;
		}
		
		// regular enchants
		if (material == Material.DIAMOND_HELMET || material == Material.IRON_HELMET || material == Material.GOLD_HELMET || material == Material.LEATHER_HELMET || material == Material.CHAINMAIL_HELMET){
			enchant = helmetEnchants.get(r.nextInt(helmetEnchants.size()));
		}
		else if (material == Material.DIAMOND_CHESTPLATE || material == Material.IRON_CHESTPLATE || material == Material.GOLD_CHESTPLATE || material == Material.LEATHER_CHESTPLATE || material == Material.CHAINMAIL_CHESTPLATE){
			enchant = chestplateEnchants.get(r.nextInt(chestplateEnchants.size()));
		}
		else if (material == Material.DIAMOND_LEGGINGS || material == Material.IRON_LEGGINGS || material == Material.GOLD_LEGGINGS || material == Material.LEATHER_LEGGINGS || material == Material.CHAINMAIL_LEGGINGS){
			enchant = leggingsEnchants.get(r.nextInt(leggingsEnchants.size()));
		}
		else if (material == Material.DIAMOND_BOOTS || material == Material.IRON_BOOTS || material == Material.GOLD_BOOTS || material == Material.LEATHER_BOOTS || material == Material.CHAINMAIL_BOOTS){
			enchant = bootsEnchants.get(r.nextInt(bootsEnchants.size()));
		}
		else{
			enchant = Enchantment.PROTECTION_ENVIRONMENTAL; // fail safe
		}
		
		return enchant;
	}
	
	public Enchantment getRandomToolEnchatment(ItemStack baseItem){
		Enchantment enchant;
		Material material = baseItem.getType();
		
		if ( (material == Material.DIAMOND_SWORD || material == Material.IRON_SWORD || material == Material.GOLD_SWORD || material == Material.STONE_SWORD || material == Material.WOOD_SWORD)
				&& hasDamageEnchant(baseItem) == false){
			enchant = damageEnchants.get(r.nextInt(damageEnchants.size())); // add damage enchant first
			return enchant;
		}
		else if ( (material == Material.DIAMOND_PICKAXE || material == Material.IRON_PICKAXE || material == Material.GOLD_PICKAXE || material == Material.STONE_PICKAXE || material == Material.WOOD_PICKAXE)
				&& hasPickaxeLootEnchant(baseItem) == false){
			enchant = pickaxeLootEnchants.get(r.nextInt(pickaxeLootEnchants.size())); // add pickaxe loot enchant first
			return enchant;
		}
		else if ( (material == Material.DIAMOND_AXE || material == Material.IRON_AXE || material == Material.GOLD_AXE || material == Material.STONE_AXE || material == Material.WOOD_AXE)
				&& hasPickaxeLootEnchant(baseItem) == false){
			enchant = pickaxeLootEnchants.get(r.nextInt(pickaxeLootEnchants.size())); // add pickaxe loot enchant first
			return enchant;
		}
		else if ( (material == Material.DIAMOND_SPADE || material == Material.IRON_SPADE || material == Material.GOLD_SPADE || material == Material.GOLD_SPADE || material == Material.WOOD_SPADE)
				&& hasPickaxeLootEnchant(baseItem) == false){
			enchant = pickaxeLootEnchants.get(r.nextInt(pickaxeLootEnchants.size())); // add pickaxe loot enchant first
			return enchant;
		}
		
		// regular enchants
		if (material == Material.DIAMOND_SWORD || material == Material.IRON_SWORD || material == Material.GOLD_SWORD || material == Material.STONE_SWORD || material == Material.WOOD_SWORD){
			enchant = swordEnchants.get(r.nextInt(swordEnchants.size()));
		}
		else if (material == Material.DIAMOND_PICKAXE || material == Material.IRON_PICKAXE || material == Material.GOLD_PICKAXE || material == Material.STONE_PICKAXE || material == Material.WOOD_PICKAXE){
			enchant = pickaxeEnchants.get(r.nextInt(pickaxeEnchants.size()));
		}
		else if (material == Material.DIAMOND_AXE || material == Material.IRON_AXE || material == Material.GOLD_AXE || material == Material.STONE_AXE || material == Material.WOOD_AXE){
			enchant = axeEnchants.get(r.nextInt(axeEnchants.size()));
		}
		else if (material == Material.DIAMOND_SPADE || material == Material.IRON_SPADE || material == Material.GOLD_SPADE || material == Material.GOLD_SPADE || material == Material.WOOD_SPADE){
			enchant = shovelEnchants.get(r.nextInt(shovelEnchants.size()));
		}
		else if (material == Material.BOW){
			enchant = bowEnchants.get(r.nextInt(bowEnchants.size()));
		}
		else if (material == Material.FISHING_ROD){
			enchant = rodEnchants.get(r.nextInt(rodEnchants.size()));
		}
		else{
			enchant = Enchantment.DURABILITY; // fail safe
		}
		
		return enchant;
	}
	
	public boolean hasProtectionEnchant(ItemStack baseItem){
		for (Enchantment enchant : protectionEnchants){
			if (baseItem.containsEnchantment(enchant)){
				return true;
			}
		}
		return false;
	}
	
	public boolean hasDamageEnchant(ItemStack baseItem){
		for (Enchantment enchant : damageEnchants){
			if (baseItem.containsEnchantment(enchant)){
				return true;
			}
		}
		return false;
	}
	
	public boolean hasPickaxeLootEnchant(ItemStack baseItem){
		for (Enchantment enchant : pickaxeLootEnchants){
			if (baseItem.containsEnchantment(enchant)){
				return true;
			}
		}
		return false;
	}
	
	public int getRandomEnchantLevel(Enchantment enchant, boolean isOP){
		int level;
		if (isOP == true){
			level = r.nextInt( (int)Math.round(enchant.getMaxLevel()/2.0) ) + (int)Math.round(enchant.getMaxLevel()/2.0); // half max level to max level
			if (level == 0){
				level = 1;
			}
		}
		else{
			level = r.nextInt( (int)Math.round(enchant.getMaxLevel()/2.0) ) + 1; // 1 to half max level
		}
		
		return level;
	}

}
