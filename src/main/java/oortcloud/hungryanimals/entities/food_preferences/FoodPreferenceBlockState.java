package oortcloud.hungryanimals.entities.food_preferences;

import java.util.Map;

import net.minecraft.block.state.IBlockState;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.utils.HashBlockState;
import oortcloud.hungryanimals.utils.Pair;

public class FoodPreferenceBlockState implements IFoodPreference<IBlockState> {

	private Map<HashBlockState, Pair<Double, Double>> map;

	public FoodPreferenceBlockState(Map<HashBlockState, Pair<Double, Double>> map) {
		this.map = map;
	}

	@Override
	public double getNutrient(IBlockState food) {
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
	public double getStomach(IBlockState food) {
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
	public boolean canEat(ICapabilityHungryAnimal cap, IBlockState food) {
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

}
