package oortcloud.hungryanimals.configuration.util;

import java.util.Random;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class DropRare {
	private HashItem hashItem;
	private double probability;
	
	public DropRare(HashItem item, double probability) {
		this.hashItem = item;
		this.probability = probability;
	}
	
	public DropRare(Item item, double probability) {
		this(new HashItem(item), probability);
	}
	
	/**
	 * 
	 * @param rand
	 * @return
	 */
	public ItemStack getDrop(Random rand, int looting) {
		if (rand.nextDouble() < probability*(0.5*looting)) {
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
