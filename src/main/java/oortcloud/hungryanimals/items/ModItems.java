package oortcloud.hungryanimals.items;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.Strings;

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
	public static Item animalGlue;
	public static Item compositeWood;
	
	public static void init() {
		trough = new ItemTrough();
		bola = new ItemBola();
		slingshot = new ItemSlingShot();
		debugGlass = new ItemDebugGlass();
		manure = new Item().setUnlocalizedName(Strings.itemManureName).setCreativeTab(HungryAnimals.tabHungryAnimals);
		ModItems.register(manure);
		woodash = new Item().setUnlocalizedName(Strings.itemWoodashName).setCreativeTab(HungryAnimals.tabHungryAnimals);
		ModItems.register(woodash);
		saltpeter = new Item().setUnlocalizedName(Strings.itemSaltpeterName).setCreativeTab(HungryAnimals.tabHungryAnimals);
		ModItems.register(saltpeter);
		tendon = new Item().setUnlocalizedName(Strings.itemTendonName).setCreativeTab(HungryAnimals.tabHungryAnimals);
		ModItems.register(tendon);
		animalGlue = new Item().setUnlocalizedName(Strings.itemAnimalGlueName).setCreativeTab(HungryAnimals.tabHungryAnimals);
		ModItems.register(animalGlue);
		compositeWood = new Item().setUnlocalizedName(Strings.itemCompositeWoodName).setCreativeTab(HungryAnimals.tabHungryAnimals);
		ModItems.register(compositeWood);
		
		//herbicide = new ItemHerbicide();
	}

	public static String getName(String unlocalizedName) {
		return unlocalizedName.substring(unlocalizedName.indexOf("item.") + 5);
	}

	public static void register(Item item) {
		// TODO Item Registration System
		GameRegistry.registerItem(item, getName(item.getUnlocalizedName()));
	}
}
