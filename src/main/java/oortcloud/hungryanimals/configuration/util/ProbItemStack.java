package oortcloud.hungryanimals.configuration.util;

import net.minecraft.item.ItemStack;

public class ProbItemStack {
	public double prob;
	public ItemStack item;

	public ProbItemStack(double prob, ItemStack item) {
		this.prob = prob;
		this.item = item;
	}

}
