package oortcloud.hungryanimals.entities.food_preferences;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;

public class FoodPreferenceItemStack implements IFoodPreference<ItemStack> {

	private Map<HashItemType, Double> map = new HashMap<HashItemType, Double>();

	@Override
	public double getHunger(ItemStack food) {
		HashItemType key;

		if (this.map.containsKey(key = new HashItemType(food.getItem()))) {
			return this.map.get(key);
		} else if (this.map.containsKey(key = new HashItemType(food.getItem(), food.getItemDamage()))) {
			return this.map.get(key);
		} else {
			return 0;
		}
	}

	@Override
	public boolean canEat(ICapabilityHungryAnimal cap, ItemStack food) {
		double hunger = getHunger(food);
		if (hunger == 0)
			return false;
		return cap.getHunger() + hunger < cap.getMaxHunger();
	}

	@Override
	public boolean shouldEat(ICapabilityHungryAnimal cap) {
		return cap.getHunger() + Collections.min(map.values()) < cap.getMaxHunger();
	}

	public static class HashItemType {
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

}
