package oortcloud.hungryanimals.configuration.util;

import java.util.Random;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import oortcloud.hungryanimals.entities.properties.FoodPreferenceItemStack.HashItemType;

public class ValueDropRare {
	private HashItemType hashItem;
	private double probability;
	
	public ValueDropRare(HashItemType item, double probability) {
		this.hashItem = item;
		this.probability = probability;
	}
	
	public ValueDropRare(Item item, double probability) {
		this(new HashItemType(item), probability);
	}
	
	/**
	 * 
	 * @param rand
	 * @return
	 */
	public ItemStack getDrop(Random rand, int looting) {
		if (rand.nextDouble() < probability+(0.01*looting)) {
			return hashItem.toItemStack();
		}
		return null;
	}
	
	public ItemStack getItemStack() {
		return hashItem.toItemStack();
	}
	
	public String toString() {
		return "("+hashItem.toString()+","+probability+")";
	}
}
