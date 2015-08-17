package oortcloud.hungryanimals.configuration.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class DropMeat {
	private HashItem hashItem;
	private int min_amount;
	private int max_amount;
	
	public DropMeat(HashItem hashItem, int min_amount, int max_amount) {
		this.hashItem = hashItem;
		this.min_amount = min_amount;
		this.max_amount = max_amount;
	}
	
	public DropMeat(Item item,int min_amount, int max_amount) {
		this(new HashItem(item),min_amount,max_amount);
	}
	
	/**
	 * 
	 * @param hunger ; hunger value 0.0~1.0
	 * @return
	 */
	public ItemStack getDrop(double hunger) {
		ItemStack stack = hashItem.toItemStack();
		stack.stackSize= min_amount+(int)((max_amount-min_amount+1)*hunger)-(int)hunger;
		return stack;
	}
	
	public ItemStack getItemStack() {
		return hashItem.toItemStack();
	}
	
	public String toString() {
		return "("+hashItem.toString()+","+min_amount+","+max_amount+")";
	}
}
