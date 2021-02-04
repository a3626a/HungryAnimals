package oortcloud.hungryanimals.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import oortcloud.hungryanimals.core.lib.Strings;

public class NiterBedBlock extends Block {

	public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 7);

	public static double ripeningProbability;

	public NiterBedBlock() {
		super(
				Block.Properties
						.create(Material.EARTH)
						.harvestLevel(0)
						.harvestTool(ToolType.SHOVEL)
						.hardnessAndResistance(0.5F)
						.tickRandomly()
		);
		setDefaultState(this.stateContainer.getBaseState().with(AGE, 0));
		setRegistryName(Strings.blockNiterBedName);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(AGE);
	}

	@Override
	public void tick(BlockState state, World worldIn, BlockPos pos, Random rand) {
		int age = state.get(AGE);
		if (age < 7 && rand.nextDouble() < NiterBedBlock.ripeningProbability) {
			worldIn.setBlockState(pos, state.with(AGE, age + 1), 2);
		}
	}
//	@Override
//	public int quantityDropped(BlockState state, int fortune, Random random) {
//		return (Integer) state.get(AGE) == 7 ? (3 + random.nextInt(3)) : 1;
//	}
//
//	@Override
//	public Item getItemDropped(BlockState state, Random rand, int fortune) {
//		return (Integer) state.get(AGE) == 7 ? ModItems.SALTPETER.get() : super.getItemDropped(state, rand, fortune);
//	}
}
