package Evolution.Relics;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import Evolution.Main.Core;

public class HoeOfPossessionWatcher implements Runnable{

	private Player player;
	private Entity entity;
	private int id;
	
	public HoeOfPossessionWatcher(Player player, Entity entity){
		this.player = player;
		this.entity = entity;
		Core.hoeOfPossession.possessedEntities.add(entity);
		id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.thisPlugin, this, 0, 1); // run every tick
	}
	
	public void stopPosession(){
		Bukkit.getScheduler().cancelTask(id);
		Core.hoeOfPossession.possessedEntities.remove(entity);
		entity.setFallDistance(0F);
	}
	
	@Override
	public void run() {
		if ( (entity == null) || (entity.isDead() == true) ){
			stopPosession();
		}
		if (player == null || player.isOnline() == false){
			stopPosession();
			return;
		}
		if (player.isSneaking()){
			stopPosession();
			return;
		}
		if (entity.getLocation().getWorld().equals( player.getLocation().getWorld() ) == false){
			stopPosession();
			return;
		}
		if (entity instanceof Player){
			if ( ((Player)entity).isSneaking() ){
				stopPosession();
				return;
			}
		}
		Location playerLocation = player.getLocation();
		
		// calculate the location of which the entity should be held at (similar to gravity gun in some games)
		Location location = new Location(playerLocation.getWorld(), playerLocation.getX(), playerLocation.getY(), playerLocation.getZ(), entity.getLocation().getYaw(), entity.getLocation().getPitch());
		Vector direction = player.getEyeLocation().getDirection();
		location.setX( location.getX() + direction.getX()*4);
		if (direction.getY() <= -0.15){
			location.setY( location.getY() );
		}
		else{
			location.setY( location.getY() + 1 + direction.getY()*4 );
		}
		location.setZ( location.getZ() + direction.getZ()*4);
		
		// we want the entity to "go to" the location, so we set their velocity in order to get them to this location
		Vector difference;
		if (entity instanceof Player){
			// we multiply by 0.4 because otherwise there is some weird inconsistency which leads to the player flying around like crazy
			// probably due to difference in when the velocity is applied to the user compared to mobs
			difference = location.subtract(entity.getLocation()).toVector().multiply(0.40); 
		}
		else{
			difference = location.subtract(entity.getLocation()).toVector();
		}
		entity.setVelocity(difference);
		entity.setFallDistance(0F);
		
		// old way (using teleportation):
		//entity.teleport(location);
		//entity.setVelocity(new Vector(0.0, 0.10, 0.0));
	}
}
