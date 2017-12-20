package oortcloud.hungryanimals.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.items.ModItems;

@Mod.EventBusSubscriber
public class CraftingHandler {
	
	public static void init() {
		registerRecipe();
	}

	private static void registerRecipe() {
		//RecipeSorter.register(References.RESOURCESPREFIX+"shapeddistinctore",     ShapedDistinctOreRecipe.class,    SHAPED,    "after:forge:shapedore before:minecraft:shapeless");
		
		OreDictionary.registerOre("dustSaltpeter", ModItems.saltpeter);
		OreDictionary.registerOre("dustWoodAsh", ModItems.woodash);
		
		for (ItemStack i : OreDictionary.getOres("treeSapling")) {
			GameRegistry.addSmelting(i, new ItemStack(ModItems.woodash), 1.0F);
		}
		//GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.herbicide), "aba", "aca","ada" ,'a', "paneGlass", 'b', new ItemStack(Items.spider_eye), 'c', new ItemStack(Items.rotten_flesh), 'd',new ItemStack(Items.poisonous_potato)));
	}
	
	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
	    event.getRegistry().registerAll(new ShapedDistinctOreRecipe(new ResourceLocation(References.MODID, "compositewood"), new ItemStack(ModItems.compositeWood), "abc", "abc", "abc", 'a', "logWood", 'b', new ItemStack(ModItems.animalGlue), 'c', "logWood"));
	}
}
