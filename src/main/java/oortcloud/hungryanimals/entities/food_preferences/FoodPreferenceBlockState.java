package oortcloud.hungryanimals.entities.food_preferences;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.minecraft.block.BlockState;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.utils.HashBlockState;
import oortcloud.hungryanimals.utils.Pair;

import javax.annotation.Nonnull;

public class FoodPreferenceBlockState implements IFoodPreference<BlockState> {

	private Map<HashBlockState, Pair<Double, Double>> map;

	public FoodPreferenceBlockState(Map<HashBlockState, Pair<Double, Double>> map) {
		this.map = map;
	}

	@Override
	public double getNutrient(BlockState food) {
		HashBlockState key;

		if (this.map.containsKey(key = new HashBlockState(food, true))) {
			return this.map.get(key).left;
		} else if (this.map.containsKey(key = new HashBlockState(food, false))) {
			return this.map.get(key).left;
		} else {
			return 0;
		}
	}

	@Override
	public double getStomach(BlockState food) {
		HashBlockState key;

		if (this.map.containsKey(key = new HashBlockState(food, true))) {
			return this.map.get(key).right;
		} else if (this.map.containsKey(key = new HashBlockState(food, false))) {
			return this.map.get(key).right;
		} else {
			return 0;
		}
	}

	@Override
	public boolean canEat(@Nonnull ICapabilityHungryAnimal cap, BlockState food) {
		double stomach = getStomach(food);
		return stomach > 0 && shouldEat(cap);
	}

	@Override
	public boolean shouldEat(ICapabilityHungryAnimal cap) {
		return cap.getStomach() < cap.getMaxStomach();
	}


	@Override
	public String toString() {
		return map.toString();
	}

	public List<BlockState> getKeys() {
		return map.keySet().stream().map((x)->x.toBlockState()).collect(Collectors.toList());
	}
	
}
