package oortcloud.hungryanimals.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.items.ModItems;

public class TrapCoverBlock extends Block {
	private static final VoxelShape VOXEL_SHAPE = Block.makeCuboidShape(0.0D, 14.0D, 0.0D, 16.0D, 16.0D, 16.0D);

	public TrapCoverBlock() {
		super(Block.Properties
				.create(Material.WOOD)
				.harvestLevel(0)
				.harvestTool(ToolType.AXE)
				.hardnessAndResistance(2.0F));
		setRegistryName(Strings.blockTrapCoverName);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return VOXEL_SHAPE;
	}

	@Override
	public void tick(BlockState state, World worldIn, BlockPos pos, Random rand) {
		if (!worldIn.isRemote && state.getBlock() == this) {
			worldIn.removeBlock(pos, false);
			spawnAsEntity(worldIn, pos, new ItemStack(ModItems.STICK.get(), 2 + worldIn.rand.nextInt(4)));
			for (Direction i : Direction.Plane.HORIZONTAL) {
				worldIn.getPendingBlockTicks().scheduleTick(pos.offset(i), this, 3);
			}
		}
	}

	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
		worldIn.getPendingBlockTicks().scheduleTick(pos, this, 3);
	}
}
