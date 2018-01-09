package oortcloud.hungryanimals.generation;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ICondition {
	public boolean canGrassGrow(World world, BlockPos pos);
}
