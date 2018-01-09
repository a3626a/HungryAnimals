package oortcloud.hungryanimals.generation;

import com.google.gson.JsonElement;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import oortcloud.hungryanimals.utils.HashBlockState;

public class ConditionBelow implements ICondition {

	private HashBlockState state;
	
	public ConditionBelow(HashBlockState state) {
		this.state = state;
	}
	
	@Override
	public boolean canGrassGrow(World world, BlockPos pos) {
		return state.apply(world.getBlockState(pos.down()));
	}

	public static ConditionBelow parse(JsonElement jsonEle) {
		HashBlockState state = HashBlockState.parse(jsonEle);
		return new ConditionBelow(state);
	}
	
}
