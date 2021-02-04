package oortcloud.hungryanimals.block;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;

public class ModBlocks {
	// Minecraft or other mod blocks
	public static final RegistryObject<Block> OAK_LEAVES = RegistryObject.of(new ResourceLocation("minecraft:oak_leaves"), ForgeRegistries.BLOCKS);
	public static final RegistryObject<Block> WHITE_WOOL = RegistryObject.of(new ResourceLocation("minecraft:white_wool"), ForgeRegistries.BLOCKS);
	public static final RegistryObject<Block> HAY_BLOCK = RegistryObject.of(new ResourceLocation("minecraft:hay_block"), ForgeRegistries.BLOCKS);
	public static final RegistryObject<Block> DIRT = RegistryObject.of(new ResourceLocation("minecraft:dirt"), ForgeRegistries.BLOCKS);
	public static final RegistryObject<Block> GRASS_BLOCK = RegistryObject.of(new ResourceLocation("minecraft:grass_block"), ForgeRegistries.BLOCKS);
	public static final RegistryObject<Block> SAND = RegistryObject.of(new ResourceLocation("minecraft:sand"), ForgeRegistries.BLOCKS);
	// Minecraft or other mod blocks

	public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, References.MODID);
	public static final RegistryObject<Block> EXCRETA = BLOCKS.register(Strings.blockExcretaName, ExcretaBlock::new);
	public static final RegistryObject<Block> TROUGH = BLOCKS.register(Strings.blockTroughName, TroughBlock::new);
	public static final RegistryObject<Block> TRAP_COVER = BLOCKS.register(Strings.blockTrapCoverName, TrapCoverBlock::new);
	public static final RegistryObject<Block> NITER_BED = BLOCKS.register(Strings.blockNiterBedName, NiterBedBlock::new);
	public static final RegistryObject<Block> FLOOR_COVER_LEAF = BLOCKS.register(
			Strings.blockFloorCoverLeafName,
			() -> new FloorCoverBlock(OAK_LEAVES.get().getDefaultState().getMaterial()).setRegistryName(Strings.blockFloorCoverLeafName)
	);
	public static final RegistryObject<Block> FLOOR_COVER_WOOL = BLOCKS.register(
			Strings.blockFloorCoverWoolName,
			() -> new FloorCoverBlock(WHITE_WOOL.get().getDefaultState().getMaterial()).setRegistryName(Strings.blockFloorCoverWoolName)
	);
	public static final RegistryObject<Block> FLOOR_COVER_HAY = BLOCKS.register(
			Strings.blockFloorCoverHayName,
			() -> new FloorCoverBlock(HAY_BLOCK.get().getDefaultState().getMaterial()).setRegistryName(Strings.blockFloorCoverHayName)
	);
}
