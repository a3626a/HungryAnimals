package oortcloud.hungryanimals.items;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.blocks.ModBlocks;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;

public class ModItems {
	public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, References.MODID);

	public static RegistryObject<Item> MANURE = register(Strings.itemManureName);
	public static RegistryObject<Item> WOOD_ASH = register(Strings.itemWoodAshName);
	public static RegistryObject<Item> SALTPETER = register(Strings.itemSaltpeterName);
	public static RegistryObject<Item> TENDON = register(Strings.itemTendonName);
	public static RegistryObject<Item> ANIMAL_GLUE = register(Strings.itemAnimalGlueName);
	public static RegistryObject<Item> COMPOSITE_WOOD = register(Strings.itemCompositeWoodName);

	public static Item trough;
	public static Item bola;
	public static Item slingshot;
	public static Item debugGlass;

	public static Item herbicide;

	public static void init() {
		trough = new ItemTrough();
		bola = new ItemBola();
		slingshot = new ItemSlingShot();
		debugGlass = new ItemDebugGlass();

		//herbicide = new ItemHerbicide();
	}
	
	public static void registerItems(RegistryEvent.Register<Item> event) {
	    event.getRegistry().register(new ItemBlock(ModBlocks.EXCRETA.get()).setRegistryName(ModBlocks.EXCRETA.get().getRegistryName()));
	    event.getRegistry().register(new ItemBlock(ModBlocks.FLOOR_COVER_HAY.get()).setRegistryName(ModBlocks.FLOOR_COVER_HAY.get().getRegistryName()));
	    event.getRegistry().register(new ItemBlock(ModBlocks.FLOOR_COVER_LEAF.get()).setRegistryName(ModBlocks.FLOOR_COVER_LEAF.get().getRegistryName()));
	    event.getRegistry().register(new ItemBlock(ModBlocks.FLOOR_COVER_WOOL.get()).setRegistryName(ModBlocks.FLOOR_COVER_WOOL.get().getRegistryName()));
	    event.getRegistry().register(new ItemBlock(ModBlocks.NITER_BED.get()).setRegistryName(ModBlocks.NITER_BED.get().getRegistryName()));
	    event.getRegistry().register(new ItemBlock(ModBlocks.TRAP_COVER.get()).setRegistryName(ModBlocks.TRAP_COVER.get().getRegistryName()));
	}

	private static RegistryObject<Item> register(String name) {
		return ITEMS.register(
				name,
				() -> new Item(new Item.Properties().group(HungryAnimals.tabHungryAnimals)).setRegistryName(name)
		);
	}

}
