package oortcloud.hungryanimals.items;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.block.ModBlocks;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;

public class ModItems {
	// Minecraft or other mod items
	public static final RegistryObject<Item> WHEAT = RegistryObject.of(new ResourceLocation("minecraft:wheat"), ForgeRegistries.ITEMS);
	public static final RegistryObject<Item> STICK = RegistryObject.of(new ResourceLocation("minecraft:stick"), ForgeRegistries.ITEMS);
	public static final RegistryObject<Item> COBBLESTONE = RegistryObject.of(new ResourceLocation("minecraft:cobblestone"), ForgeRegistries.ITEMS);
	// Minecraft or other mod items

	public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, References.MODID);

	public static RegistryObject<Item> MANURE = register(Strings.itemManureName);
	public static RegistryObject<Item> WOOD_ASH = register(Strings.itemWoodAshName);
	public static RegistryObject<Item> SALTPETER = register(Strings.itemSaltpeterName);
	public static RegistryObject<Item> TENDON = register(Strings.itemTendonName);
	public static RegistryObject<Item> ANIMAL_GLUE = register(Strings.itemAnimalGlueName);
	public static RegistryObject<Item> COMPOSITE_WOOD = register(Strings.itemCompositeWoodName);

	public static RegistryObject<Item> EXCRETA = register(ModBlocks.EXCRETA);
	public static RegistryObject<Item> FLOOR_COVER_HAY = register(ModBlocks.FLOOR_COVER_HAY);
	public static RegistryObject<Item> FLOOR_COVER_LEAF = register(ModBlocks.FLOOR_COVER_LEAF);
	public static RegistryObject<Item> FLOOR_COVER_WOOL = register(ModBlocks.FLOOR_COVER_WOOL);
	public static RegistryObject<Item> NITER_BED = register(ModBlocks.NITER_BED);
	public static RegistryObject<Item> TRAP_COVER = register(ModBlocks.TRAP_COVER);

	public static RegistryObject<Item> TROUGH = ITEMS.register(
			ModBlocks.TROUGH.get().getRegistryName().getPath(),
			() -> new TroughItem(ModBlocks.TROUGH.get(), new Item.Properties().group(HungryAnimals.tabHungryAnimals)).setRegistryName(ModBlocks.TROUGH.get().getRegistryName())
	);
	public static RegistryObject<Item> SLINGSHOT = ITEMS.register(Strings.itemSlingShotName, SlingShotItem::new);
	public static RegistryObject<Item> BOLA = ITEMS.register(Strings.itemBolaName, BolaItem::new);
	public static RegistryObject<Item> DEBUG_GLASS = ITEMS.register(Strings.itemDebugGlassName, DebugGlassItem::new);

	private static RegistryObject<Item> register(String name) {
		return ITEMS.register(
				name,
				() -> new Item(new Item.Properties().group(HungryAnimals.tabHungryAnimals)).setRegistryName(name)
		);
	}

	private static RegistryObject<Item> register(RegistryObject<Block> block) {
		ResourceLocation resourceLocation = block.get().getRegistryName();
		return ITEMS.register(
				resourceLocation.getPath(),
				() -> new BlockItem(block.get(), new Item.Properties().group(HungryAnimals.tabHungryAnimals)).setRegistryName(resourceLocation)
		);
	}
}
