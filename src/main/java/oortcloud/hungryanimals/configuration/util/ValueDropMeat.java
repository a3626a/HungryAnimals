package oortcloud.hungryanimals.configuration.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import oortcloud.hungryanimals.entities.properties.FoodPreferenceItemStack.HashItemType;

public class ValueDropMeat {
	private HashItemType hashItem;
	private int min_amount;
	private int max_amount;
	
	public ValueDropMeat(HashItemType hashItem, int min_amount, int max_amount) {
		this.hashItem = hashItem;
		this.min_amount = min_amount;
		this.max_amount = max_amount;
	}
	
	public ValueDropMeat(Item item,int min_amount, int max_amount) {
		this(new HashItemType(item),min_amount,max_amount);
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
