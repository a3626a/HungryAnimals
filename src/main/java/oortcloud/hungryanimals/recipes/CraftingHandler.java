package oortcloud.hungryanimals.recipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import oortcloud.hungryanimals.items.ModItems;

@Mod.EventBusSubscriber
public class CraftingHandler {
	
	public static void init() {
		registerRecipe();
	}

	@SuppressWarnings("null")
	private static void registerRecipe() {
		//RecipeSorter.register(References.RESOURCESPREFIX+"shapeddistinctore",     ShapedDistinctOreRecipe.class,    SHAPED,    "after:forge:shapedore before:minecraft:shapeless");
		
		OreDictionary.registerOre("dustSaltpeter", ModItems.SALTPETER.get());
		OreDictionary.registerOre("dustWoodAsh", ModItems.WOOD_ASH.get());
		
		for (ItemStack i : OreDictionary.getOres("treeSapling")) {
			GameRegistry.addSmelting(i, new ItemStack(ModItems.WOOD_ASH.get()), 1.0F);
		}
		//GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.herbicide), "aba", "aca","ada" ,'a', "paneGlass", 'b', new ItemStack(Items.spider_eye), 'c', new ItemStack(Items.rotten_flesh), 'd',new ItemStack(Items.poisonous_potato)));
	}
}
