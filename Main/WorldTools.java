package Evolution.Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

public class WorldTools {
	
	private World tempWorld;
	
	public WorldTools(){ // constructor
	}
	
	public boolean loadWorld(String worldName){
		tempWorld = Bukkit.getServer().createWorld( new WorldCreator(worldName));
		if ( !(tempWorld == null) ){ // checks if the world actually exists
			Bukkit.getServer().getWorlds().add(tempWorld);
			return true; // world loaded successfully
		}
		return false; // world doesn't exist
	}
	
	public boolean unloadWorld(String worldName){
		tempWorld = Bukkit.getWorld(worldName);
		if ( tempWorld != null ){ // checks if the world is acutally loaded
			return Bukkit.unloadWorld(worldName, true);
		}
		return false;
	}
	
	public boolean checkWorldExists(String worldName){
		// returns true if the specified world folder exists
		
		String directory = Bukkit.getWorlds().get(0).getWorldFolder().getParent(); // gets the main server folder
		directory = directory.substring(0, directory.length()-1); // remove the last character from the directory which is a "."
		directory += worldName; // appends the world name to get it's main folder
		File path = new File(directory);
		if (path.exists()){
			return true; // world exists
		}
		return false; // world doesn't exist
	}
	
	public boolean createWorld(String worldName){
		WorldCreator worldCreator = new WorldCreator(worldName);
		worldCreator.environment(Environment.NORMAL);
		worldCreator.type(WorldType.NORMAL);
		worldCreator.generateStructures(true);
		worldCreator.createWorld();
		return true;
	}
	
	public boolean deleteWorld(String worldName){
		// returns true if the world was deleted successfully
		
		String directory = Bukkit.getWorlds().get(0).getWorldFolder().getParent(); // gets the main server folder
		directory = directory.substring(0, directory.length()-1); // remove the last character from the directory which is a "."
		directory += worldName; // appends the world name to get it's main folder
		File path = new File(directory);
		
		if (path.exists() == false){
			return false; // world doesn't exist
		}
		
		return deleteDirectory(path);
	}
	
	public boolean deleteDirectory(File path){
		if(path.exists()) {
			File files[] = path.listFiles();
			
			// iterates though the files and directories, deleting all the contents
			for(int i = 0; i < files.length; i++) {
				if(files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
			path.delete(); // delete the path itself
			return true; // all subfiles were deleted
		}
		return false; // the path doesn't exist, and thus nothing was deleted
	}
	
	public boolean copyWorld(String sourceWorldName, String targetWorldName){
		String sourceDirectory = Bukkit.getWorlds().get(0).getWorldFolder().getParent(); // gets the main folder
		sourceDirectory = sourceDirectory.substring(0, sourceDirectory.length()-1); // remove the last character from the directory which is a "."
		sourceDirectory += sourceWorldName; // appends the world name to get it's main folder
		File source = new File(sourceDirectory);
		
		if (source.exists() == false){
			return false; // source world doesn't exist
		}
		
		String targetWorldDirectory = Bukkit.getWorlds().get(0).getWorldFolder().getParent(); // gets the main folder
		targetWorldDirectory = targetWorldDirectory.substring(0, targetWorldDirectory.length()-1); // remove the last character from the directory which is a "."
		targetWorldDirectory += targetWorldName;
		
		File target = new File(targetWorldDirectory);
		if (target.exists() == false){
			target.mkdirs(); // create the directory
		}
		
		//Bukkit.getServer().broadcastMessage("Source: " + source.getAbsolutePath() + " " + source.exists());
		//Bukkit.getServer().broadcastMessage("Target: " + target.getAbsolutePath() + " " + target.exists());
		
		if (copyFiles(source, target) == false){
			return false;
		}
		return true;
	}
	
	public boolean copyFiles(File source, File target){
		try {
			ArrayList<String> ignore = new ArrayList<String>();
			ignore.add("uid.dat");
			ignore.add("session.dat");
			if(!ignore.contains(source.getName())) {
				if(source.isDirectory()) {
					if(!target.exists())
						target.mkdirs();
					String files[] = source.list();
					for (String file : files) {
						File srcFile = new File(source, file);
						File destFile = new File(target, file);
						copyFiles(srcFile, destFile);
					}
				} else {
					InputStream in = new FileInputStream(source);
					OutputStream out = new FileOutputStream(target);
					byte[] buffer = new byte[1024];
					int length;
					while ((length = in.read(buffer)) > 0)
						out.write(buffer, 0, length);
					in.close();
					out.close();
	            }
	        }
	    } catch (IOException e) {
	    	System.out.println("IO Error while copying files from " + source.getAbsolutePath() + " to " + target.getAbsolutePath());
	    	return false;
	    }
		return true;
	}

}