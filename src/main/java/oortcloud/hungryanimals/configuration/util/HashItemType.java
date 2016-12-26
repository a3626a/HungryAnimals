package oortcloud.hungryanimals.configuration.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class HashItemType {
	private Item item;
	private int damage;
	private boolean ignoreDamage;

	public HashItemType(Item item) {
		this.item = item;
		this.ignoreDamage = true;
	}

	public HashItemType(Item item, int damage) {
		this.item = item;
		this.damage = damage;
		this.ignoreDamage = false;
	}

	@Override
	public boolean equals(Object obj) {
		if (this.ignoreDamage && ((HashItemType) obj).ignoreDamage) {
			return this.item == ((HashItemType) obj).item;
		} else if (!this.ignoreDamage && !((HashItemType) obj).ignoreDamage) {
			return this.item == ((HashItemType) obj).item && this.damage == ((HashItemType) obj).damage;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return item.getUnlocalizedName().hashCode() + (damage << 16) + ((ignoreDamage ? 1 : 0) << 15);
	}

	public ItemStack toItemStack() {
		return new ItemStack(item, 1, damage);
	}

	public String toString() {
		if (ignoreDamage) {
			return "(" + String.valueOf(Item.REGISTRY.getNameForObject(item)) + ")";
		} else {
			return "(" + String.valueOf(Item.REGISTRY.getNameForObject(item)) + "," + damage + ")";
		}
	}
}
