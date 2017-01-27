package oortcloud.hungryanimals.entities.food_preference;

import java.util.Collections;
import java.util.HashMap;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.entities.food_preference.FoodPreferenceBlockState.HashBlockState;

public class FoodPreferenceBlockState implements IFoodPreference<IBlockState>{
	
	/*
	 * Map object is created once while reading from configuration.
	 * In other words, Map object is shared between entities or AI objects
	 */
	private HashMap<HashBlockState, Double> map = new HashMap<HashBlockState, Double>();
	private ICapabilityHungryAnimal cap;
	
	public FoodPreferenceBlockState(ICapabilityHungryAnimal cap) {
		this.cap = cap;
	}
	
	@Override
	public double getHunger(IBlockState food) {
		HashBlockState key;
		
		if (this.map.containsKey(key = new HashBlockState(food, true))) {
			return this.map.get(key);
		} else if (this.map.containsKey(key = new HashBlockState(food, false))) {
			return this.map.get(key);
		} else {
			return 0;
		}
	}

	@Override
	public boolean canEat(IBlockState food) {
		double hunger = getHunger(food);
		if (hunger == 0)
			return false;
		return cap.getHunger() + hunger < cap.getMaxHunger();
	}

	@Override
	public boolean shouldEat() {
		return cap.getHunger() + Collections.min(map.values()) < cap.getMaxHunger();
	}
	
	public static class HashBlockState {
		private IBlockState block;
		private boolean ignoreProperty;

		public HashBlockState(Block block) {
			this(block.getDefaultState(), true);
		}

		public HashBlockState(IBlockState block) {
			this(block, false);
		}

		public HashBlockState(IBlockState block, boolean ignoreProperty) {
			this.block = block;
			this.ignoreProperty = ignoreProperty;
		}

		@Override
		public boolean equals(Object obj) {
			if (this.ignoreProperty && ((HashBlockState) obj).ignoreProperty) {
				return block.getBlock() == ((HashBlockState) obj).block.getBlock();
			} else if (!this.ignoreProperty && !((HashBlockState) obj).ignoreProperty) {

				if (block.getBlock() != ((HashBlockState) obj).block.getBlock())
					return false;

				for (Object i : block.getProperties().keySet()) {
					IProperty property = (IProperty) i;
					if (!block.getValue(property).equals(((HashBlockState) obj).block.getValue(property)))
						return false;
				}

				return true;
			} else {
				return false;
			}
		}

		@Override
		public int hashCode() {
			if (ignoreProperty) {
				return block.getBlock().hashCode();
			} else {
				return block.hashCode();
			}
		}
		
		public String toString() {
			if (ignoreProperty) {
				return "("+String.valueOf(Block.REGISTRY.getNameForObject(block.getBlock()))+")";
			} else {
				ImmutableSet properties = block.getProperties().keySet();
				String[] propertyStrings = new String[properties.size()];
				int next = 0;
				for (Object i : properties) {
					IProperty property = (IProperty)i;
					propertyStrings[next++] = "(" + property.getName() + "," + block.getValue(property) + ")";
				}
				
				String propertyString = new String();
				propertyString+="("+propertyStrings[0];
				for (int i = 1; i < propertyStrings.length; i++) {
					propertyString+=","+propertyStrings[i];
				}
				propertyString+=")";
				return "("+String.valueOf(Block.REGISTRY.getNameForObject(block.getBlock()))+","+propertyString+")";
			}
		}
	}

}
