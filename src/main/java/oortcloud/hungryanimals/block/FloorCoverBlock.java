package oortcloud.hungryanimals.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

public class FloorCoverBlock extends Block {
	private static final VoxelShape LEG1 = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 2.0D, 6.0D, 2.0D);
	private static final VoxelShape LEG2 = Block.makeCuboidShape(14.0D, 0.0D, 0.0D, 16.0D, 6.0D, 2.0D);
	private static final VoxelShape LEG3 = Block.makeCuboidShape(0.0D, 0.0D, 14.0D, 2.0D, 6.0D, 16.0D);
	private static final VoxelShape LEG4 = Block.makeCuboidShape(14.0D, 0.0D, 14.0D, 16.0D, 6.0D, 16.0D);
	private static final VoxelShape BODY = Block.makeCuboidShape(0.0D, 6.0D, 0.0D, 16.0D, 16.0D, 16.0D);
	private static final VoxelShape VOXEL_SHAPE = VoxelShapes.or(BODY, LEG1, LEG2, LEG3, LEG4);

	public FloorCoverBlock(Material material) {
		super(Block.Properties
				.create(material)
				.harvestLevel(0)
				.harvestTool(ToolType.AXE)
				.hardnessAndResistance(2.0F));
	}

	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return VOXEL_SHAPE;
	}
	
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
    
}
