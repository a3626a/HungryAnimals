package oortcloud.hungryanimals.blocks;


import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import oortcloud.hungryanimals.core.lib.Strings;

@Mod.EventBusSubscriber
public class ModBlocks {
	public static Block excreta;
	public static Block niterBed;
	public static Block trough;
	public static Block trapcover;
	public static Block floorcover_leaf;
	public static Block floorcover_wool;
	public static Block floorcover_ironbar;
	public static Block floorcover_hay;

	public static final RegistryObject<Block> LEAVES = RegistryObject.of(new ResourceLocation("minecraft:oak_leaves"), ForgeRegistries.BLOCKS);
	public static final RegistryObject<Block> WOOL = RegistryObject.of(new ResourceLocation("minecraft:white_wool"), ForgeRegistries.BLOCKS);
	public static final RegistryObject<Block> HAY_BLOCK = RegistryObject.of(new ResourceLocation("minecraft:hay_block"), ForgeRegistries.BLOCKS);

	public static void init()
	{
		excreta = new ExcretaBlock();
		niterBed = new BlockNiterBed();
		trough = new BlockTrough();
		trapcover = new TrapCoverBlock();
		floorcover_leaf = new FloorCoverBlock(LEAVES.get().getDefaultState().getMaterial()).setRegistryName(Strings.blockFloorCoverLeafName);
		floorcover_wool = new FloorCoverBlock(WOOL.get().getDefaultState().getMaterial()).setRegistryName(Strings.blockFloorCoverWoolName);
		floorcover_hay = new FloorCoverBlock(HAY_BLOCK.get().getDefaultState().getMaterial()).setRegistryName(Strings.blockFloorCoverHayName);
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
	    event.getRegistry().registerAll(excreta, niterBed, trough, trapcover, floorcover_leaf, floorcover_wool, floorcover_hay);
	}
	
}
