package Evolution.GameMechanics;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerReplenishTradeEvent;

import Evolution.Main.ParticleEffect;

public class VillagerTradeReplenishEffect implements Listener{
	
	public VillagerTradeReplenishEffect() {
	}
	
	@EventHandler
	public void onVillagerTradeReplenish(VillagerReplenishTradeEvent e){
		Location temp;
		for (double y = 0; y <= 2.0; y+=0.1){
			temp = e.getEntity().getLocation().add(Math.random()-0.5, y, Math.random()-0.5);
			ParticleEffect.ENDROD.display(0F, 0F, 0F, 0.01F, 1, temp, 40.0);
			//ParticleEffect.DRAGONBREATH.display(0F, 0F, 0F, 0.01F, 1, temp, 40.0);
			//ParticleEffect.DRAGONBREATH.display(0F, 0F, 0F, 0.01F, 1, temp, 40.0);
		}
	}
	
	/*
	@EventHandler
	public void onVillagerTradeReplenishMaxTradeUses(VillagerReplenishTradeEvent e){
		if (e.getEntity().getProfession() == Profession.FARMER
				&& (e.getRecipe().getIngredients().get(0).getType() == Material.MELON_BLOCK
					|| e.getRecipe().getIngredients().get(0).getType() == Material.PUMPKIN) ){
			//Bukkit.getServer().broadcastMessage("uses: " + e.getRecipe().getUses() + " -- maxUses: " + e.getRecipe().getMaxUses());
			
			e.getRecipe().setMaxUses(1000);
			e.getRecipe().setUses(0);
			for (MerchantRecipe recipe : e.getEntity().getRecipes()){
				Bukkit.getServer().broadcastMessage(recipe.getIngredients().get(0).getType() + " -- " + recipe.getUses() + " uses of " + recipe.getMaxUses());
			}
		}
	}
	*/
}