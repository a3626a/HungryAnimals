package oortcloud.hungryanimals.configuration.util;

import java.util.Random;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class HashDropRandom {
	private HashItemType hashItem;
	private int min_amount;
	private int max_amount;
	
	public HashDropRandom(HashItemType hashitem, int min_amount, int max_amount) {
		this.hashItem = hashitem;
		this.min_amount = min_amount;
		this.max_amount = max_amount;
	}
	
	public HashDropRandom(Item item, int min_amount, int max_amount) {
		this(new HashItemType(item),min_amount,max_amount);
	}
	
	/**
	 * 
	 * @param rand
	 * @return
	 */
	public ItemStack getDrop(Random rand) {
		ItemStack stack = hashItem.toItemStack();
		stack.stackSize= min_amount+rand.nextInt(max_amount-min_amount+1);
		return stack;
	}
	
	public ItemStack getItemStack() {
		return hashItem.toItemStack();
	}
	
	public String toString() { 
		return "("+hashItem.toString()+","+min_amount+","+max_amount+")";
	}
}
