package oortcloud.hungryanimals.entities.food_preferences;

import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fml.common.eventhandler.Event;
import oortcloud.hungryanimals.entities.event.EntityEventHandler.Pair;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceIngredient.FoodPreferenceIngredientEntry;
import oortcloud.hungryanimals.utils.HashBlockState;

public class HungryAnimalRegisterEvent extends Event {

	private final Class<? extends EntityAnimal> entity;

	public HungryAnimalRegisterEvent(Class<? extends EntityAnimal> entity) {
		this.entity = entity;
	}

	public Class<? extends EntityAnimal> getEntityClass() {
		return entity;
	}
	
	public static class FoodPreferenceBlockStateRegisterEvent extends HungryAnimalRegisterEvent {

		private final Map<HashBlockState, Pair<Double, Double>> map;
				
		public FoodPreferenceBlockStateRegisterEvent(Class<? extends EntityAnimal> entity, Map<HashBlockState, Pair<Double, Double>> map) {
			super(entity);
			this.map = map;
		}
		
		public void put(Block block, Pair<Double, Double> hunger) {
			map.put(new HashBlockState(block), hunger);
		}
		
		public void put(IBlockState block, boolean ignoreProperty, Pair<Double, Double> hunger) {
			map.put(new HashBlockState(block, ignoreProperty), hunger);
		}
		
		public Map<HashBlockState, Pair<Double, Double>> getMap() {
			return map;
		}
	}

	public static class FoodPreferenceItemStackRegisterEvent extends HungryAnimalRegisterEvent {

		private final List<FoodPreferenceIngredientEntry> list;
				
		public FoodPreferenceItemStackRegisterEvent(Class<? extends EntityAnimal> entity, List<FoodPreferenceIngredientEntry> list) {
			super(entity);
			this.list = list;
		}
		
		public void put(Ingredient item, double nutrient, double stomach) {
			list.add(new FoodPreferenceIngredientEntry(item, nutrient, stomach));
		}
		
		public List<FoodPreferenceIngredientEntry> getList() {
			return list;
		}
		
	}
	
}
