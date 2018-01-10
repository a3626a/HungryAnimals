package oortcloud.hungryanimals.generation;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkProviderServer;
import oortcloud.hungryanimals.utils.HashBlockState;

public class ConditionAdjacent implements ICondition {

	private List<HashBlockState> states;
	
	public ConditionAdjacent(HashBlockState state) {
		this.states = new ArrayList<HashBlockState>();
		this.states.add(state);
	}
	
	public ConditionAdjacent(List<HashBlockState> states) {
		this.states = states;
	}
	
	@Override
	public boolean canGrassGrow(World world, BlockPos pos) {
		ChunkProviderServer provider = (ChunkProviderServer) world.getChunkProvider();
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (!provider.chunkExists((pos.getX() + i) >> 4, (pos.getZ() + j) >> 4)) {
					continue;
				}
				for (HashBlockState state : states) {
					if (state.apply(world.getBlockState(pos.add(i, 0, j)))) {
						return false;
					}
				}

			}
		}
		return true;
	}
	
	public static ConditionAdjacent parse(JsonElement jsonEle) {
		if (jsonEle instanceof JsonArray) {
			JsonArray jsonArr = (JsonArray) jsonEle;
			List<HashBlockState> states = new ArrayList<HashBlockState>();
			for (JsonElement jsonState : jsonArr) {
				HashBlockState state = HashBlockState.parse(jsonState);
				states.add(state);
			}
			return new ConditionAdjacent(states);
		} else {
			HashBlockState state = HashBlockState.parse(jsonEle);
			return new ConditionAdjacent(state);
		}
	}

}
