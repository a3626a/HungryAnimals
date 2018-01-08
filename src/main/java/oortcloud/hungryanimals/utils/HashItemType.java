package oortcloud.hungryanimals.utils;

import java.lang.reflect.Type;

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
