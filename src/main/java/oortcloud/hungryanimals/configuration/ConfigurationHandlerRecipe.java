package oortcloud.hungryanimals.configuration;

import java.io.File;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import oortcloud.hungryanimals.items.ModItems;
import oortcloud.hungryanimals.recipes.RecipeAnimalGlue;
import oortcloud.hungryanimals.recipes.RecipeBlender;
import oortcloud.hungryanimals.recipes.RecipeMillstone;
import oortcloud.hungryanimals.recipes.RecipeThresher;

public class ConfigurationHandlerRecipe {

	public static Configuration config;

	public static final String CATEGORY_Thresher = "thresher";
	public static final String KEY_thresherRecipeList = "Thresher Recipe List";

	public static final String CATEGORY_Millstone = "millstone";
	public static final String KEY_millstoneRecipeList = "Millstone Recipe List";

	public static final String CATEGORY_Blender = "blender";
	public static final String KEY_blenderRecipeList = "Blender Recipe List";

	private static final String CATEGORY_AnimalGlue = "animalglue";
	private static final String KEY_AnimalGlueRecipeList = "Animal Glue Recipe List";

	public static void init(File file) {
		config = new Configuration(file);
		config.load();
	}

	public static void sync() {

		String[] recipeString;
		recipeString = config.get(
				CATEGORY_Thresher,
				KEY_thresherRecipeList,
				new String[] {
						"(" + Item.itemRegistry.getNameForObject(Items.wheat) + ")=(0.5," + Item.itemRegistry.getNameForObject(Items.wheat_seeds) + "),(1.0,"
								+ Item.itemRegistry.getNameForObject(ModItems.straw) + ")",
						"(" + Item.itemRegistry.getNameForObject(ModItems.poppycrop) + ")=(0.5," + Item.itemRegistry.getNameForObject(ModItems.poppyseed) + "),(1.0,"
								+ Item.itemRegistry.getNameForObject(ModItems.straw) + ")" }).getStringList();

		for (String i : recipeString) {
			RecipeThresher.readConfiguration(i);
		}

		recipeString = config.get(
				CATEGORY_Millstone,
				KEY_millstoneRecipeList,
				new String[] { "(" + Item.itemRegistry.getNameForObject(Items.wheat_seeds) + ")=(10)", "(" + Item.itemRegistry.getNameForObject(Items.pumpkin_seeds) + ")=(10)",
						"(" + Item.itemRegistry.getNameForObject(Items.melon_seeds) + ")=(10)", "(" + Item.itemRegistry.getNameForObject(ModItems.poppyseed) + ")=(50)" })
				.getStringList();

		for (String i : recipeString) {
			RecipeMillstone.readConfiguration(i);
		}

		recipeString = config.get(	
				CATEGORY_Blender,
				KEY_blenderRecipeList,
				new String[] {
						"(" + Item.itemRegistry.getNameForObject(Items.carrot) + "),(" + Item.itemRegistry.getNameForObject(Items.potato) + ")=("
								+ Item.itemRegistry.getNameForObject(ModItems.mixedFeed) + ")",
						"(" + Item.itemRegistry.getNameForObject(ModItems.saltpeter) + "),(" + Item.itemRegistry.getNameForObject(ModItems.manure) + ")=("
								+ Item.itemRegistry.getNameForObject(Items.dye) + ",6,15)" }).getStringList();

		for (String i : recipeString) {
			RecipeBlender.readConfiguration(i);
		}
		
		recipeString = config.get(
				CATEGORY_AnimalGlue,
				KEY_AnimalGlueRecipeList,
				new String[] {
						"(" + Item.itemRegistry.getNameForObject(Items.leather) + ")=(6)",
						"(" + Item.itemRegistry.getNameForObject(ModItems.tendon) + ")=(4)",
						"(" + Item.itemRegistry.getNameForObject(Items.bone) + ")=(2)"}).getStringList();

		for (String i : recipeString) {
			RecipeAnimalGlue.readConfiguration(i);
		}

		config.save();

	}

}
