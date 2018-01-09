package oortcloud.hungryanimals.generation;

import com.google.gson.JsonElement;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkProviderServer;
import oortcloud.hungryanimals.utils.HashBlockState;

public class ConditionAdjacent implements ICondition {

	private HashBlockState state;
	
	public ConditionAdjacent(HashBlockState state) {
		this.state = state;
	}
	
	@Override
	public boolean canGrassGrow(World world, BlockPos pos) {
		boolean flag = true;
		ChunkProviderServer provider = (ChunkProviderServer) world.getChunkProvider();
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (!provider.chunkExists((pos.getX() + i) >> 4, (pos.getZ() + j) >> 4)) {
					continue;
				}
				if (state.apply(world.getBlockState(pos.add(i, 0, j)))) {
					flag = false;
				}
			}
		}
		return flag;
	}
	
	public static ConditionAdjacent parse(JsonElement jsonEle) {
		HashBlockState state = HashBlockState.parse(jsonEle);
		return new ConditionAdjacent(state);
	}

}
