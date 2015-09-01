package oortcloud.hungryanimals.configuration.util;

import net.minecraft.item.ItemStack;

public class HashProbabilityItemStack {
	public double prob;
	public ItemStack item;

	public HashProbabilityItemStack(double prob, ItemStack item) {
		this.prob = prob;
		this.item = item;
	}

}
