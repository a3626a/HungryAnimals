package oortcloud.hungryanimals.generation;

import java.util.List;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ConditionAnd implements ICondition {

	private List<ICondition> conditions;
	
	public ConditionAnd(List<ICondition> conditions) {
		this.conditions = conditions;
	}
	
	@Override
	public boolean canGrassGrow(World world, BlockPos pos) {
		 for (ICondition i : conditions) {
			 if (!i.canGrassGrow(world, pos))
				 return false;
		 }
		return true;
	}

}
