package oortcloud.hungryanimals.generation;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.block.state.IBlockState;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.utils.HashBlockState;

public class Generator {

	public ICondition condition;
	public List<IBlockState> states;
	
	public Generator(ICondition condition, IBlockState state) {
		this.condition = condition;
		this.states = new ArrayList<IBlockState>();
		this.states.add(state);
	}

	public Generator(ICondition condition, List<IBlockState> states) {
		this.condition = condition;
		this.states = states;
	}
	
	public static Generator parse(JsonElement jsonEle) {
		if (! (jsonEle instanceof JsonObject)) {
			HungryAnimals.logger.error("Generator must an json object.");
			throw new JsonSyntaxException(jsonEle.toString());
		}
		JsonObject jsonObj = (JsonObject) jsonEle;
		JsonElement jsonCondition = jsonObj.get("condition");
		ICondition condition = Conditions.parse(jsonCondition);
		
		JsonElement jsonGrass = jsonObj.get("grass");
		if (jsonGrass instanceof JsonArray) {
			JsonArray jsonStates = (JsonArray) jsonGrass;
			List<IBlockState> states = new ArrayList<IBlockState>();
			for (JsonElement jsonState : jsonStates) {
				HashBlockState grass = HashBlockState.parse(jsonState);
				IBlockState state = grass.toBlockState();
				states.add(state);
			}
			return new Generator(condition, states);
		} else {
			HashBlockState grass = HashBlockState.parse(jsonGrass);
			IBlockState state = grass.toBlockState();
			return new Generator(condition, state);
		}
	}
	
}
