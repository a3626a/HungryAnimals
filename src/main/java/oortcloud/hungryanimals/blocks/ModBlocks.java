package oortcloud.hungryanimals.blocks;


import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {
	public static Block millStone;
	public static Block excreta;
	public static Block niterBed;
	public static Block trough;
	public static Block trapcover;
	public static Block axle;
	public static Block crankPlayer;
	public static Block thresher;
	public static Block poppy;
	public static Block millstone;
	public static Block blender;
	public static Block crankAnimal;
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
		axle = new BlockAxle();
		crankPlayer = new BlockCrankPlayer();
		thresher = new BlockThresher();
		poppy = new BlockPoppy();
		millstone = new BlockMillstone();
		blender = new BlockBlender();
		crankAnimal = new BlockCrankAnimal();
		floorcover_leaf = new BlockFloorCover(Blocks.leaves, References.RESOURCESPREFIX + Strings.blockFloorCoverLeafName);
		floorcover_wool = new BlockFloorCover(Blocks.wool, References.RESOURCESPREFIX + Strings.blockFloorCoverWoolName);
		floorcover_hay = new BlockFloorCover(Blocks.hay_block, References.RESOURCESPREFIX + Strings.blockFloorCoverHayName);
	}

	public static String getName(String unlocalizedName)
	{
		return unlocalizedName.substring(unlocalizedName.indexOf(":") + 1);
		
	}
	
	public static String getUnwrappedUnlocalizedName(String unlocalizedName)
	{
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
		
	}
	
	public static void register(Block block) {
		GameRegistry.registerBlock(block, getName(block.getUnlocalizedName()));
	}
}
