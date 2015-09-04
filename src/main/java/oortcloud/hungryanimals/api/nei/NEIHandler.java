package oortcloud.hungryanimals.api.nei;

import net.minecraftforge.fml.common.Loader;
import codechicken.nei.api.API;

public class NEIHandler {

	public static void init() {
			API.registerRecipeHandler(new CompositeWoodRecipeHandler());
			API.registerUsageHandler(new CompositeWoodRecipeHandler());
	}

}
