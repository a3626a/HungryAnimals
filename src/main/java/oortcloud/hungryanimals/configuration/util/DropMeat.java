package oortcloud.hungryanimals.configuration.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class DropMeat {
	private Item item;
	private int damage;
	private int min_amount;
	private int max_amount;
	
	public DropMeat(Item item, int damage, int min_amount, int max_amount) {
		this.item = item;
		this.damage = damage;
		this.min_amount = min_amount;
		this.max_amount = max_amount;
	}
	
	public DropMeat(ItemStack itemstack, int min_amount, int max_amount) {
		this(itemstack.getItem(), itemstack.getItemDamage(), min_amount, max_amount);
	}
	
	public DropMeat(HashItem hashitem, int min_amount, int max_amount) {
		this(hashitem.toItemStack(),min_amount,max_amount);
	}
	
	/**
	 * 
	 * @param hunger ; hunger value 0.0~1.0
	 * @return
	 */
	public ItemStack getDrop(double hunger) {
		ItemStack stack = new ItemStack(item,1,damage);
		stack.stackSize= min_amount+(int)((max_amount-min_amount+1)*hunger)-(int)hunger;
		return stack;
	}
	
	public ItemStack getItemStack() {
		return new ItemStack(item,1,damage);
	}
}
