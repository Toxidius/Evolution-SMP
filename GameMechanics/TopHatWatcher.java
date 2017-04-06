package Evolution.GameMechanics;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import Evolution.Main.Core;

public class TopHatWatcher implements Runnable{

	private Player player;
	private ArmorStand stand1;
	private ArmorStand stand2;
	private int id;
	
	public TopHatWatcher(Player player) {
		this.player = player;
		stand1 = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
		stand1.setHelmet(new ItemStack(Material.WOOL, 1, (short)15));
		stand1.setGravity(false);
		stand1.setMarker(true); // no interacting by players
		stand1.setVisible(false);
		stand1.setSmall(true);
		stand2 = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
		stand2.setHelmet(new ItemStack(Material.CARPET, 1, (short)15));
		stand2.setGravity(false);
		stand2.setMarker(true); // no interacting by players
		stand2.setVisible(false);
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 1, 1); // every tick
	}
	
	public void end(){
		stand1.remove();
		stand2.remove();
		Bukkit.getScheduler().cancelTask(id);
	}

	@Override
	public void run() {
		if (player == null
				|| player.isOnline() == false){
			end();
		}
		if (player.isSneaking()){
			end();
		}
		
		stand1.teleport(player.getLocation().add(0.0, 1.20, 0.0));
		//stand1.setHeadPose(new EulerAngle(player.getLocation().getPitch(), 0.0, 0.0));
		stand2.teleport(player.getLocation().add(0.0, 0.5, 0.0));
	}
}