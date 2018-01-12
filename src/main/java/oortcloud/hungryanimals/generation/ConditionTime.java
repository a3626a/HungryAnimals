package oortcloud.hungryanimals.generation;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ConditionTime implements ICondition {

	@Override
	public boolean canGrassGrow(World world, BlockPos pos) {
		return false;
	}

}
