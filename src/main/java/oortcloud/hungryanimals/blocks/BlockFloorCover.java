package oortcloud.hungryanimals.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.common.registry.GameRegistry;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.items.ItemBlockFloorCover;

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
		GameRegistry.register(this);
		GameRegistry.register(new ItemBlockFloorCover(blockIn).setRegistryName(UnlocalizedName));
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
    
}
