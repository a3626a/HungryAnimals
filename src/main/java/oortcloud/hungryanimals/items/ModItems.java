package oortcloud.hungryanimals.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.blocks.ModBlocks;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;

@Mod.EventBusSubscriber
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
		manure = new Item().setUnlocalizedName(References.MODID+"."+Strings.itemManureName).setRegistryName(Strings.itemManureName).setCreativeTab(HungryAnimals.tabHungryAnimals);
		woodash = new Item().setUnlocalizedName(References.MODID+"."+Strings.itemWoodashName).setRegistryName(Strings.itemWoodashName).setCreativeTab(HungryAnimals.tabHungryAnimals);
		saltpeter = new Item().setUnlocalizedName(References.MODID+"."+Strings.itemSaltpeterName).setRegistryName(Strings.itemSaltpeterName).setCreativeTab(HungryAnimals.tabHungryAnimals);
		tendon = new Item().setUnlocalizedName(References.MODID+"."+Strings.itemTendonName).setRegistryName(Strings.itemTendonName).setCreativeTab(HungryAnimals.tabHungryAnimals);
		animalGlue = new Item().setUnlocalizedName(References.MODID+"."+Strings.itemAnimalGlueName).setRegistryName(Strings.itemAnimalGlueName).setCreativeTab(HungryAnimals.tabHungryAnimals);
		compositeWood = new Item().setUnlocalizedName(References.MODID+"."+Strings.itemCompositeWoodName).setRegistryName(Strings.itemCompositeWoodName).setCreativeTab(HungryAnimals.tabHungryAnimals);
		
		//herbicide = new ItemHerbicide();
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
	    event.getRegistry().registerAll(trough, bola, slingshot, debugGlass, manure, woodash, saltpeter, tendon, animalGlue, compositeWood);
	    
	    event.getRegistry().register(new ItemBlock(ModBlocks.excreta).setRegistryName(ModBlocks.excreta.getRegistryName()));
	    event.getRegistry().register(new ItemBlock(ModBlocks.floorcover_hay).setRegistryName(ModBlocks.floorcover_hay.getRegistryName()));
	    event.getRegistry().register(new ItemBlock(ModBlocks.floorcover_leaf).setRegistryName(ModBlocks.floorcover_leaf.getRegistryName()));
	    event.getRegistry().register(new ItemBlock(ModBlocks.floorcover_wool).setRegistryName(ModBlocks.floorcover_wool.getRegistryName()));
	    event.getRegistry().register(new ItemBlock(ModBlocks.niterBed).setRegistryName(ModBlocks.niterBed.getRegistryName()));
	    event.getRegistry().register(new ItemBlock(ModBlocks.trapcover).setRegistryName(ModBlocks.trapcover.getRegistryName()));
	}

}
