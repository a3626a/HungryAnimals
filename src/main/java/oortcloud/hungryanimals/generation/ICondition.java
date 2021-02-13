package oortcloud.hungryanimals.generation;

import com.google.gson.JsonElement;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

public interface ICondition {
	boolean canGrassGrow(World world, BlockPos pos);
	abstract class Serializer extends ForgeRegistryEntry<ICondition.Serializer> {
		public abstract ICondition deserialize(JsonElement p_212870_1_);
	}
}

