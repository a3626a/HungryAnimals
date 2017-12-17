package oortcloud.hungryanimals.blocks;


import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
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
	
	public static void init()
	{
		excreta = new BlockExcreta();
		niterBed = new BlockNiterBed();
		trough = new BlockTrough();
		trapcover = new BlockTrapCover();
		floorcover_leaf = new BlockFloorCover(Blocks.LEAVES, Strings.blockFloorCoverLeafName);
		floorcover_wool = new BlockFloorCover(Blocks.WOOL, Strings.blockFloorCoverWoolName);
		floorcover_hay = new BlockFloorCover(Blocks.HAY_BLOCK, Strings.blockFloorCoverHayName);
	}

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
	    event.getRegistry().registerAll(excreta, niterBed, trough, trapcover, floorcover_leaf, floorcover_wool, floorcover_hay);
	}
	
}
