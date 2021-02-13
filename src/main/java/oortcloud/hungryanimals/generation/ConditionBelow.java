package oortcloud.hungryanimals.generation;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import oortcloud.hungryanimals.utils.HashBlockState;

public class ConditionBelow implements ICondition {

	private List<HashBlockState> states;
	
	public ConditionBelow(List<HashBlockState> states) {
		this.states = states;
	}
	
	public ConditionBelow(HashBlockState state) {
		this.states = new ArrayList<>();
		this.states.add(state);
	}
	
	@Override
	public boolean canGrassGrow(World world, BlockPos pos) {
		for (HashBlockState state : states) {
			if (state.apply(world.getBlockState(pos.down()))) {
				return true;
			}
		}
		return false;
	}

	public static class Serializer extends ICondition.Serializer {
		public ICondition deserialize(JsonElement jsonEle) {
			if (jsonEle instanceof JsonArray) {
				JsonArray jsonArr = (JsonArray) jsonEle;
				List<HashBlockState> states = new ArrayList<HashBlockState>();
				for (JsonElement jsonState : jsonArr) {
					HashBlockState state = HashBlockState.parse(jsonState);
					states.add(state);
				}
				return new ConditionBelow(states);
			} else {
				HashBlockState state = HashBlockState.parse(jsonEle);
				return new ConditionBelow(state);
			}
		}
	}
}
