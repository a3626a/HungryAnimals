package oortcloud.hungryanimals.api.nei;

import oortcloud.hungryanimals.blocks.ModBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import codechicken.nei.api.API;

public class NEIHandler {

	public static void init() {
		API.registerRecipeHandler(new CompositeWoodRecipeHandler());
		API.registerUsageHandler(new CompositeWoodRecipeHandler());
		
		API.registerRecipeHandler(new BlenderRecipeHandler());
		API.registerUsageHandler(new BlenderRecipeHandler());
		
		API.registerRecipeHandler(new AnimalGlueRecipeHandler());
		API.registerUsageHandler(new AnimalGlueRecipeHandler());
		
		API.hideItem(new ItemStack(ModBlocks.crankAnimal));
		API.hideItem(new ItemStack(ModBlocks.poppy));
		API.hideItem(new ItemStack(ModBlocks.trough));
	}

}
