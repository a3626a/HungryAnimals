package oortcloud.hungryanimals.entities.food_preferences;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;

public class FoodPreferenceItemStack implements IFoodPreference<ItemStack> {

	private Map<HashItemType, Double> map = new HashMap<HashItemType, Double>();
	private final double min;
	public FoodPreferenceItemStack(Map<HashItemType, Double> map) {
		this.map = map;
		min = Collections.min(map.values());
	}
	
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
		return cap.getHunger() + min < cap.getMaxHunger();
	}

	@Override
	public String toString() {
		return map.toString();
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
			return this.item.toString();
		}

		public static class Serializer implements JsonDeserializer<HashItemType>, JsonSerializer<HashItemType> {
			public HashItemType deserialize(JsonElement ele, Type type, JsonDeserializationContext context) throws JsonParseException {
				JsonObject jsonobject = ele.getAsJsonObject();

				String name = JsonUtils.getString(jsonobject, "name");
				Item item = Item.REGISTRY.getObject(new ResourceLocation(name));

				if (item == null) {
					HungryAnimals.logger.warn("{} has wrong name. It cannot find item {}", ele, name);
					return null;
				}

				if (JsonUtils.hasField(jsonobject, "damage")) {
					return new HashItemType(item, JsonUtils.getInt(jsonobject, "damage"));
				} else {
					return new HashItemType(item);
				}
			}

			public JsonElement serialize(HashItemType item, Type type, JsonSerializationContext context) {
				if (item.ignoreDamage) {
					JsonObject jsonobject = new JsonObject();
					jsonobject.addProperty("name", item.item.getRegistryName().toString());
					return new JsonObject();
				} else {
					JsonObject jsonobject = new JsonObject();
					jsonobject.addProperty("name", item.item.getRegistryName().toString());
					jsonobject.addProperty("damage", item.damage);
					return jsonobject;
				}
			}
		}
	}
}
