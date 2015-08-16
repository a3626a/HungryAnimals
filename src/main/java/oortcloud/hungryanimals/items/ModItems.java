package oortcloud.hungryanimals.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.fluids.ModFluids;

public class ModItems {
	public static Item manure;
	public static Item woodash;
	public static Item saltpeter;
	public static Item trough;
	public static Item bola;
	public static Item slingshot;
	public static Item herbicide;
	public static Item tendon;
	public static Item debugGlass;
	public static Item wheel;
	public static Item straw;
	public static Item poppyseed;
	public static Item poppycrop;
	public static Item mixedFeed;
	public static Item animalGlue;
	public static Item compositeWood;
	public static Item compositeWoodCasing;
	public static Item blade;
	public static Item crankAnimal;
	public static Item oilpipet;
	
	public static void init() {
		trough = new ItemTrough();
		bola = new ItemBola();
		slingshot = new ItemSlingShot();
		// herbicide = new ItemHerbicide();
		debugGlass = new ItemDebugGlass();
		poppyseed = new ItemPoppySeed();
		crankAnimal = new ItemCrankAnimal();
		oilpipet = new ItemOilPipet(1000);
		manure = new Item().setUnlocalizedName(References.RESOURCESPREFIX + Strings.itemManureName).setCreativeTab(HungryAnimals.tabHungryAnimals);
		ModItems.register(manure);
		woodash = new Item().setUnlocalizedName(References.RESOURCESPREFIX + Strings.itemWoodashName).setCreativeTab(HungryAnimals.tabHungryAnimals);
		ModItems.register(woodash);
		saltpeter = new Item().setUnlocalizedName(References.RESOURCESPREFIX + Strings.itemSaltpeterName).setCreativeTab(HungryAnimals.tabHungryAnimals);
		ModItems.register(saltpeter);
		tendon = new Item().setUnlocalizedName(References.RESOURCESPREFIX + Strings.itemTendonName).setCreativeTab(HungryAnimals.tabHungryAnimals);
		ModItems.register(tendon);
		wheel = new Item().setUnlocalizedName(References.RESOURCESPREFIX + Strings.itemWheelName).setCreativeTab(HungryAnimals.tabHungryAnimals);
		ModItems.register(wheel);
		straw = new Item().setUnlocalizedName(References.RESOURCESPREFIX + Strings.itemStrawName).setCreativeTab(HungryAnimals.tabHungryAnimals);
		ModItems.register(straw);
		poppycrop = new Item().setUnlocalizedName(References.RESOURCESPREFIX + Strings.itemPoppyCropName).setCreativeTab(HungryAnimals.tabHungryAnimals);
		ModItems.register(poppycrop);
		mixedFeed = new Item().setUnlocalizedName(References.RESOURCESPREFIX + Strings.itemMixedFeedName).setCreativeTab(HungryAnimals.tabHungryAnimals);
		ModItems.register(mixedFeed);
		animalGlue = new Item().setUnlocalizedName(References.RESOURCESPREFIX + Strings.itemAnimalGlueName).setCreativeTab(HungryAnimals.tabHungryAnimals);
		ModItems.register(animalGlue);
		compositeWood = new Item().setUnlocalizedName(References.RESOURCESPREFIX + Strings.itemCompositeWoodName).setCreativeTab(HungryAnimals.tabHungryAnimals);
		ModItems.register(compositeWood);
		compositeWoodCasing = new Item().setUnlocalizedName(References.RESOURCESPREFIX + Strings.itemCompositeWoodCasingName).setCreativeTab(HungryAnimals.tabHungryAnimals);
		ModItems.register(compositeWoodCasing);
		blade = new Item().setUnlocalizedName(References.RESOURCESPREFIX + Strings.itemBladeName).setCreativeTab(HungryAnimals.tabHungryAnimals);
		ModItems.register(blade);
	}

	public static String getUnwrappedUnlocalizedName(String unlocalizedName) {
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);

	}

	public static String getName(String unlocalizedName) {
		return unlocalizedName.substring(unlocalizedName.indexOf(":") + 1);

	}

	public static void register(Item item) {
		GameRegistry.registerItem(item, getName(item.getUnlocalizedName()));
	}
}
