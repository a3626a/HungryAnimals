package oortcloud.hungryanimals.configuration.util;

import java.util.Random;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class DropRandom {
	private Item item;
	private int damage;
	private int min_amount;
	private int max_amount;
	
	public DropRandom(Item item, int damage, int min_amount, int max_amount) {
		this.item = item;
		this.damage = damage;
		this.min_amount = min_amount;
		this.max_amount = max_amount;
	}
	
	public DropRandom(ItemStack itemstack, int min_amount, int max_amount) {
		this(itemstack.getItem(), itemstack.getItemDamage(), min_amount, max_amount);
	}
	
	public DropRandom(HashItem hashitem, int min_amount, int max_amount) {
		this(hashitem.toItemStack(),min_amount,max_amount);
	}
	
	/**
	 * 
	 * @param rand
	 * @return
	 */
	public ItemStack getDrop(Random rand) {
		ItemStack stack = new ItemStack(item,1,damage);
		stack.stackSize= min_amount+rand.nextInt(max_amount-min_amount+1);
		return stack;
	}
	
	public ItemStack getItemStack() {
		return new ItemStack(item,1,damage);
	}
}
