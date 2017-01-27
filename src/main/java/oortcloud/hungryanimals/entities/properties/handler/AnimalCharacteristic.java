package oortcloud.hungryanimals.entities.properties.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttribute;
import oortcloud.hungryanimals.configuration.util.ValueDropMeat;
import oortcloud.hungryanimals.configuration.util.ValueDropRandom;
import oortcloud.hungryanimals.configuration.util.ValueDropRare;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceBlockState.HashBlockState;
import oortcloud.hungryanimals.entities.food_preferences.FoodPreferenceItemStack.HashItemType;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryAnimal;

public class AnimalCharacteristic {

	/***
	 * Characteristic is a data storage for immutable values.
	 * For example, edible foods and blocks, drop items, and attributes.
	 * These properties are not changed during game play. 
	 */

	public HashMap<IAttribute, Pair<Boolean,Double>> attributeMap;

	public AnimalCharacteristic() {
		attributeMap = new HashMap<IAttribute, Pair<Boolean,Double>>();
		hunger_food = new HashMap<HashItemType, Double>();
		hunger_block = new HashMap<HashBlockState, Double>();
		drop_meat = new ArrayList<ValueDropMeat>();
		drop_random = new ArrayList<ValueDropRandom>();
		drop_rare = new ArrayList<ValueDropRare>();
	}

	public void putAttribute(IAttribute attribute, double value, boolean shouldRegister) {
		attributeMap.put(attribute, Pair.of(shouldRegister,value));
	}
	
	public void applyAttributes(ExtendedPropertiesHungryAnimal extendedProperty) {
		EntityLivingBase entity = extendedProperty.entity;
		for (Entry<IAttribute, Pair<Boolean, Double>> i : attributeMap.entrySet()) {
			entity.getAttributeMap().getAttributeInstance(i.getKey()).setBaseValue(i.getValue().getRight());
		}
	}

	public void registerAttributes(EntityLivingBase entity) {
		for (IAttribute i : attributeMap.keySet()) {
			if (entity.getAttributeMap().getAttributeInstance(i) == null && attributeMap.get(i).getLeft()) {
				entity.getAttributeMap().registerAttribute(i);
			}
		}
	}

	public String[] toStringHungerFood() {
		Set<HashItemType> keys = hunger_food.keySet();
		String[] ret = new String[keys.size()];
		Iterator<HashItemType> keyIterator = keys.iterator();
		int next = 0;
		while (keyIterator.hasNext()) {
			HashItemType i = (HashItemType) keyIterator.next();
			double value = hunger_food.get(i);
			ret[next++] = i.toString() + "=(" + value + ")";
		}
		return ret;
	}

	public String[] toStringHungerBlock() {
		Set<HashBlockState> keys = hunger_block.keySet();
		String[] ret = new String[keys.size()];
		int next = 0;
		for (HashBlockState i : keys) {
			double value = hunger_block.get(i);
			ret[next++] = i.toString() + "=(" + value + ")";
		}
		return ret;
	}

}
