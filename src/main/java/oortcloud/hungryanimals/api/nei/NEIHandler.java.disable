package oortcloud.hungryanimals.api.nei;

import codechicken.nei.api.API;
import net.minecraft.item.ItemStack;
import oortcloud.hungryanimals.blocks.ModBlocks;

public class NEIHandler {

	public static void init() {
		API.registerRecipeHandler(new CompositeWoodRecipeHandler());
		API.registerUsageHandler(new CompositeWoodRecipeHandler());
		
		API.registerRecipeHandler(new AnimalGlueRecipeHandler());
		API.registerUsageHandler(new AnimalGlueRecipeHandler());
		
		API.hideItem(new ItemStack(ModBlocks.trough));
	}

}
