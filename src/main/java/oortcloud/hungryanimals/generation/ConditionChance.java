package oortcloud.hungryanimals.generation;

import com.google.gson.JsonElement;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ConditionChance implements ICondition {

	private double chance;
	
	public ConditionChance(double chance) {
		this.chance = chance;
	}
	
	@Override
	public boolean canGrassGrow(World world, BlockPos pos) {
		return world.rand.nextDouble() < chance;
	}
	
	public static class Serializer extends ICondition.Serializer {
		public ICondition deserialize(JsonElement jsonEle) {
			double chance = jsonEle.getAsDouble();
			return new ConditionChance(chance);
		}
	}
}
