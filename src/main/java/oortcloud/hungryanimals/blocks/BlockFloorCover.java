package oortcloud.hungryanimals.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.BlockRenderLayer;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.References;

public class BlockFloorCover extends Block {
	
	public Block material;

	public BlockFloorCover(Block blockIn, String UnlocalizedName) {
		super(blockIn.getDefaultState().getMaterial());
		setHarvestLevel("axe", 0);
		setHardness(2.0F);

		material=blockIn;
		setUnlocalizedName(References.MODID+"."+UnlocalizedName); 
		setRegistryName(UnlocalizedName);
		setCreativeTab(HungryAnimals.tabHungryAnimals);
	}

	@Override
	public boolean isFullCube(BlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(BlockState state) {
		return false;
	}
	
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
    
}
