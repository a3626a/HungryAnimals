package oortcloud.hungryanimals.entities.properties.handler;

import java.util.HashMap;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.entity.ai.attributes.IAttribute;

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

}
