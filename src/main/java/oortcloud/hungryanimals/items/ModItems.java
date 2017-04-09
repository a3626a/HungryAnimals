package oortcloud.hungryanimals.items;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.References;
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
		//debugGlass = new ItemDebugGlass();
		manure = new Item().setUnlocalizedName(References.MODID+"."+Strings.itemManureName).setRegistryName(Strings.itemManureName).setCreativeTab(HungryAnimals.tabHungryAnimals);
		GameRegistry.register(manure);
		woodash = new Item().setUnlocalizedName(References.MODID+"."+Strings.itemWoodashName).setRegistryName(Strings.itemWoodashName).setCreativeTab(HungryAnimals.tabHungryAnimals);
		GameRegistry.register(woodash);
		saltpeter = new Item().setUnlocalizedName(References.MODID+"."+Strings.itemSaltpeterName).setRegistryName(Strings.itemSaltpeterName).setCreativeTab(HungryAnimals.tabHungryAnimals);
		GameRegistry.register(saltpeter);
		tendon = new Item().setUnlocalizedName(References.MODID+"."+Strings.itemTendonName).setRegistryName(Strings.itemTendonName).setCreativeTab(HungryAnimals.tabHungryAnimals);
		GameRegistry.register(tendon);
		animalGlue = new Item().setUnlocalizedName(References.MODID+"."+Strings.itemAnimalGlueName).setRegistryName(Strings.itemAnimalGlueName).setCreativeTab(HungryAnimals.tabHungryAnimals);
		GameRegistry.register(animalGlue);
		compositeWood = new Item().setUnlocalizedName(References.MODID+"."+Strings.itemCompositeWoodName).setRegistryName(Strings.itemCompositeWoodName).setCreativeTab(HungryAnimals.tabHungryAnimals);
		GameRegistry.register(compositeWood);
		
		//herbicide = new ItemHerbicide();
	}

}
