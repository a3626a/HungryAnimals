package oortcloud.hungryanimals.recipes;

import static net.minecraftforge.oredict.RecipeSorter.Category.SHAPED;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import oortcloud.hungryanimals.blocks.ModBlocks;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.items.ModItems;

public class CraftingHandler {
	
	public static void init() {
		registerRecipe();
	}

	private static void registerRecipe() {
		RecipeSorter.register(References.RESOURCESPREFIX+"shapeddistinctore",     ShapedDistinctOreRecipe.class,    SHAPED,    "after:forge:shapedore before:minecraft:shapeless");
		
		OreDictionary.registerOre("dustSaltpeter", ModItems.saltpeter);
		OreDictionary.registerOre("dustWoodAsh", ModItems.woodash);
		
		for (ItemStack i : OreDictionary.getOres("treeSapling")) {
			GameRegistry.addSmelting(i, new ItemStack(ModItems.woodash), 1.0F);
		}
		CraftingManager.getInstance().addShapelessRecipe(new ItemStack(Items.dye, 2, 15), new Object[] {ModItems.manure});
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.niterBed), "aba", "cdc", "aba", 'a', new ItemStack(Items.wheat), 'b', new ItemStack(ModItems.manure), 'c', "dustWoodAsh", 'd', new ItemStack(Blocks.dirt)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.niterBed), "aba", "cdc", "aba", 'a', new ItemStack(Items.wheat), 'c', new ItemStack(ModItems.manure), 'b', "dustWoodAsh", 'd', new ItemStack(Blocks.dirt)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.slingshot), "aba", "aaa"," a " ,'a', "stickWood", 'b', new ItemStack(ModItems.tendon)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.bola), "a a", " b ","a a" ,'a', "stone", 'b', new ItemStack(ModItems.tendon)));
		//GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.herbicide), "aba", "aca","ada" ,'a', "paneGlass", 'b', new ItemStack(Items.spider_eye), 'c', new ItemStack(Items.rotten_flesh), 'd',new ItemStack(Items.poisonous_potato)));
		GameRegistry.addRecipe(new ShapedDistinctOreRecipe(new ItemStack(ModItems.compositeWood), "abc", "abc", "abc", 'a', "logWood", 'b', new ItemStack(ModItems.animalGlue), 'c', "logWood"));
		CraftingManager.getInstance().addRecipe(new ItemStack(Items.lead), "aa ","aa ","  a", 'a',new ItemStack(ModItems.tendon));
		CraftingManager.getInstance().addRecipe(new ItemStack(ModItems.trough), "a a", "aaa", 'a', new ItemStack(ModItems.compositeWood));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.floorcover_leaf), "a","b", 'a', "treeLeaves", 'b', new ItemStack(ModItems.compositeWood)));
		CraftingManager.getInstance().addRecipe(new ItemStack(ModBlocks.floorcover_wool), "c", "a","b", 'a',new ItemStack(Items.feather), 'c',new ItemStack(Blocks.wool),'b', new ItemStack(ModItems.compositeWood));
		CraftingManager.getInstance().addRecipe(new ItemStack(ModBlocks.floorcover_hay), "a","b", 'a',new ItemStack(Blocks.hay_block), 'b', new ItemStack(ModItems.compositeWood));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.trapcover), "a a", " a ","a a" ,'a', "stickWood"));
	}
}
