package oortcloud.hungryanimals.entities.food_preferences;

import java.util.Map;

import net.minecraftforge.fluids.FluidStack;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.utils.Pair;

import javax.annotation.Nonnull;

public class FoodPreferenceFluid implements IFoodPreference<FluidStack> {

	private Map<String, Pair<Double, Double>> map;

	public FoodPreferenceFluid(Map<String, Pair<Double, Double>> map) {
		this.map = map;
	}
	
	@Override
	public boolean canEat(@Nonnull ICapabilityHungryAnimal cap, FluidStack food) {
		double stomach = getStomach(food);
		return stomach > 0 && shouldEat(cap);
	}

	@Override
	public boolean shouldEat(ICapabilityHungryAnimal cap) {
		return cap.getStomach() < cap.getMaxStomach();
	}

	@Override
	public double getNutrient(FluidStack food) {
		if (this.map.containsKey(food.getFluid().getName())) {
			return this.map.get(food.getFluid().getName()).left/1000.0*food.amount;
		} else {
			return 0;
		}
	}

	@Override
	public double getStomach(FluidStack food) {
		if (this.map.containsKey(food.getFluid().getName())) {
			return this.map.get(food.getFluid().getName()).right/1000.0*food.amount;
		} else {
			return 0;
		}
	}

}
