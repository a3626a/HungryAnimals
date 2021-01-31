package oortcloud.hungryanimals.generation;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.block.BlockState;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.utils.HashBlockState;

public class GrassGenerator {

	public ICondition condition;
	public List<BlockState> states;
	
	public GrassGenerator(ICondition condition, BlockState state) {
		this.condition = condition;
		this.states = new ArrayList<BlockState>();
		this.states.add(state);
	}

	public GrassGenerator(ICondition condition, List<BlockState> states) {
		this.condition = condition;
		this.states = states;
	}
	
	public static GrassGenerator parse(JsonElement jsonEle) {
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
			List<BlockState> states = new ArrayList<BlockState>();
			for (JsonElement jsonState : jsonStates) {
				HashBlockState grass = HashBlockState.parse(jsonState);
				BlockState state = grass.toBlockState();
				states.add(state);
			}
			return new GrassGenerator(condition, states);
		} else {
			HashBlockState grass = HashBlockState.parse(jsonGrass);
			BlockState state = grass.toBlockState();
			return new GrassGenerator(condition, state);
		}
	}
	
}
