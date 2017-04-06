package Evolution.GameMechanics;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.server.PluginDisableEvent;

import Evolution.Main.Core;

public class FancyFishing implements Listener{

	public Set<ArmorStand> activeArmorStands;
	
	public FancyFishing(){
		activeArmorStands = new HashSet<ArmorStand>();
	}
	
	@EventHandler
	public void onPlayerFish(PlayerFishEvent e){
		if (e.getState() == State.FISHING){
			//Bukkit.getServer().broadcastMessage("triggered");
			// start fish runnable at the bobbers location
			FancyFishing thisClass = this;
			for (int i = 0; i < Core.r.nextInt(2)+3; i++){
				Bukkit.getScheduler().scheduleSyncDelayedTask(Core.thisPlugin, new Runnable(){
					@Override
					public void run() {
						if (e.getHook() != null 
								&& e.getHook().getLocation().add(0.0, -0.25, 0.0).getBlock().isLiquid()
								&& e.getHook().getLocation().add(0.0, -1.25, 0.0).getBlock().isLiquid()
								&& e.getHook().getLocation().add(0.0, -2.25, 0.0).getBlock().isLiquid()
								&& e.getHook().getLocation().add(0.0, -3.25, 0.0).getBlock().isLiquid()){
							//Bukkit.getServer().broadcastMessage("in water");
							@SuppressWarnings("unused")
							FancyFishingRunnable runnable = new FancyFishingRunnable(e.getHook(), thisClass, ((Player)e.getHook().getShooter()).getLocation().getYaw());
						}
					}
				}, 40);
			}
			
			
		}
	}
	
	@EventHandler
	public void onServerReload(PluginDisableEvent e){
		for (ArmorStand stand : activeArmorStands){
			stand.remove();
		}
	}
	
	@EventHandler
	public void onPlayerInteractFishArmorStand(PlayerInteractAtEntityEvent e){
		if (e.getRightClicked() instanceof ArmorStand
				&& e.getRightClicked().getCustomName() != null
				&& e.getRightClicked().getCustomName().equals("FancyFish")){
			e.setCancelled(true); // prevent right clicking the fish armor stand
		}
	}
	
	@EventHandler
	public void onFishArmorStandDamage(EntityDamageEvent e){
		if (e.getEntity() instanceof ArmorStand
				&& e.getEntity().getCustomName() != null
				&& e.getEntity().getCustomName().equals("FancyFish")){
			e.setCancelled(true); // prevent damaging the fish armor stand
		}
	}
}
