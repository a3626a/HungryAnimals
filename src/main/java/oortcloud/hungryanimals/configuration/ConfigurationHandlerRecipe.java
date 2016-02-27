package oortcloud.hungryanimals.configuration;

import java.io.File;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.items.ModItems;
import oortcloud.hungryanimals.recipes.RecipeAnimalGlue;

public class ConfigurationHandlerRecipe {

	public static Configuration config;

	private static final String CATEGORY_AnimalGlue = "animalglue";
	private static final String KEY_AnimalGlueRecipeList = "Animal Glue Recipe List";

	public static void init(File file) {
		config = new Configuration(file);
		config.load();
	}

	public static void sync() {
		HungryAnimals.logger.info("Configuration: Recipe start");
		String[] recipeString;

		HungryAnimals.logger.info("Configuration: Read and Register Animals Glue Recipe");
		recipeString = config.get(CATEGORY_AnimalGlue, KEY_AnimalGlueRecipeList,
				new String[] { "(" + Item.itemRegistry.getNameForObject(Items.leather) + ")=(6)", "(" + Item.itemRegistry.getNameForObject(ModItems.tendon) + ")=(4)", "(" + Item.itemRegistry.getNameForObject(Items.bone) + ")=(2)" })
				.getStringList();

		for (String i : recipeString) {
			RecipeAnimalGlue.readConfiguration(i);
		}

		config.save();

	}

}
