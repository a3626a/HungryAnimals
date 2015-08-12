package oortcloud.hungryanimals.blocks;

import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.blocks.BlockExcreta.EnumType;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.items.ItemBlockFloorCover;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFloorCover extends Block {
	
	public Block material;

	public BlockFloorCover(Block blockIn, String UnlocalizedName) {
		super(blockIn.getMaterial());
		
		material=blockIn;
		
		this.setHardness(2.0F);
		this.setUnlocalizedName(UnlocalizedName);
		this.setCreativeTab(HungryAnimals.tabHungryAnimals);
		GameRegistry.registerBlock(this, ItemBlockFloorCover.class, ModBlocks.getName(this.getUnlocalizedName()));
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public int getBlockColor()
    {
		return material.getBlockColor();
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public int getRenderColor(IBlockState state)
    {
        return material.getBlockColor();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
    {
        return material.colorMultiplier(worldIn, pos);
    }
	
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
    	return material.getBlockLayer();
    }
    
}
