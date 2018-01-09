package oortcloud.hungryanimals.generation;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.block.state.IBlockState;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.utils.HashBlockState;

public class Generator {

	public ICondition condition;
	public IBlockState blockstate;
	
	public Generator(ICondition condition, IBlockState blockstate) {
		this.condition = condition;
		this.blockstate = blockstate;
	}

	public static Generator parse(JsonElement jsonEle) {
		if (! (jsonEle instanceof JsonObject)) {
			HungryAnimals.logger.error("Generator must an object.");
			throw new JsonSyntaxException(jsonEle.toString());
		}
		JsonObject jsonObj = (JsonObject) jsonEle;
		JsonElement jsonCondition = jsonObj.get("condition");
		ICondition condition = Conditions.parse(jsonCondition);
		
		JsonElement jsonGrass = jsonObj.get("grass");
		HashBlockState grass = HashBlockState.parse(jsonGrass);
		IBlockState blockstate = grass.toBlockState();
		
		return new Generator(condition, blockstate);
	}
	
}
