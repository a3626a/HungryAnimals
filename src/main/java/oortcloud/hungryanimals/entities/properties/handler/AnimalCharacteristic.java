package oortcloud.hungryanimals.entities.properties.handler;

import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttribute;
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

}
