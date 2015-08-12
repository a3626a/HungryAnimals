package oortcloud.hungryanimals.configuration.util;

import java.util.Random;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class DropRare {
	private ItemStack item;
	private double probability;
	
	public DropRare(ItemStack item, double probability) {
		this.item = item;
		this.probability = probability;
	}
	
	public DropRare(HashItem hashitem, double probability) {
		this(hashitem.toItemStack(),probability);
	}
	
	/**
	 * 
	 * @param rand
	 * @return
	 */
	public ItemStack getDrop(Random rand, int looting) {
		if (rand.nextDouble() < probability*(0.5*looting)) {
			return item;
		}
		return null;
	}
	
	public ItemStack getItemStack() {
		return item;
	}
}
