package Evolution.GameMechanics;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import Evolution.Main.Core;

public class BloodMoon implements Listener, Runnable{

	private ArrayList<PotionEffectType> potionEffectTypes;
	public boolean bloodMoonActive = false;
	
	public BloodMoon(){
		potionEffectTypes = new ArrayList<PotionEffectType>();
		potionEffectTypes.add(PotionEffectType.SPEED);
		potionEffectTypes.add(PotionEffectType.REGENERATION);
		potionEffectTypes.add(PotionEffectType.DAMAGE_RESISTANCE);
		potionEffectTypes.add(PotionEffectType.HEALTH_BOOST);
		potionEffectTypes.add(PotionEffectType.JUMP);
		potionEffectTypes.add(PotionEffectType.INCREASE_DAMAGE);
		
		// start bloodmoon checker
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 40, 40); // every 2 seconds
	}
	
	@Override
	public void run() {
		// check to see that a blood moon has started
		if (bloodMoonActive == false && isBloodMoon()){
			// start blood moon event
			bloodMoonActive = true;
			
			// start message
			Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "Blood Moon is now commencing!!!");
			
			// start sound
			for (Player player : Bukkit.getOnlinePlayers()){
				if (player.isOnline()){
					player.playSound(player.getLocation(), Sound.ENTITY_WOLF_HOWL, 0.25f, 1f); // volume/distance and pitch
				}
			}
		}
		else if (bloodMoonActive == true && isBloodMoon() == false){
			// end blood moon event
			bloodMoonActive = false;
			
			// start message
			Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "You survived this blood moon... Next time you might not be so lucky!");
			
			// end sound
			for (Player player : Bukkit.getOnlinePlayers()){
				if (player.isOnline()){
					player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 0.25f, 2f); // volume/distance and pitch
				}
			}
		}
	}
	
	// blood moon on the nights when the moon is full
	@EventHandler
	public void onBloodMoonMonsterSpawn(EntitySpawnEvent e){
		if (e.getEntity() instanceof Monster 
				&& e.getEntity().getWorld().getName().equals("world")
				&& bloodMoonActive == true){
			Monster monster = (Monster) e.getEntity();
			// give the monster a random potion effect for 24000 ticks (1 in-game day) and a level of 1 to 4
			PotionEffectType potionEffectType = potionEffectTypes.get( Core.r.nextInt(potionEffectTypes.size()) );
			int level = Core.r.nextInt(5); // 0 to 4
			if (potionEffectType == PotionEffectType.DAMAGE_RESISTANCE && level > 2){
				level = 2;
			}
			else if (potionEffectType == PotionEffectType.REGENERATION && level > 2){
				level = 2;
			}
			else if (potionEffectType == PotionEffectType.INCREASE_DAMAGE && level > 1){
				level = 1;
			}
			else if (potionEffectType == PotionEffectType.SPEED && level > 3){
				level = 3;
			}
			monster.addPotionEffect(new PotionEffect(potionEffectType, 24000, level, false, true)); // give mob potion effect
			
			// if creeper add metadata tag "isBloodMoonCreeper" to true
			if (e.getEntity() instanceof Creeper){
				e.getEntity().setMetadata("isBloodMoonCreeper", new FixedMetadataValue(Core.thisPlugin, true));
			}
		}
	}
	
	// prevent overpowered area effects from creepers in overworld during bloodmoons
	@EventHandler
	public void onAreaEffectCloudSpawn(EntityExplodeEvent e){
		if (e.getEntity() instanceof Creeper
				&& e.getEntity().getWorld().equals(Bukkit.getWorlds().get(0))
				&& e.getEntity().hasMetadata("isBloodMoonCreeper")
				&& e.getEntity().getMetadata("isBloodMoonCreeper").get(0).asBoolean() == true){
			Creeper creeper = (Creeper) e.getEntity();
			for (PotionEffect effect : creeper.getActivePotionEffects()){
				creeper.removePotionEffect(effect.getType());
			}
		}
	}
	
	public boolean isNight(){
		World world = Bukkit.getWorlds().get(0);
		if (world.getTime() >= 12000){
			return true;
		}
		return false;
	}
	
	public boolean isFullMoon(){
		World world = Bukkit.getWorlds().get(0);
		if (world.getFullTime()%192000 >= 0 && world.getFullTime()%192000 <= 24000){
			return true;
		}
		return false;
	}
	
	public boolean isBloodMoon(){
		if (isNight() && isFullMoon()){
			return true;
		}
		return false;
	}
}
