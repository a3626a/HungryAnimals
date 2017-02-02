package oortcloud.hungryanimals.entities.food_preferences;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;

public class FoodPreferenceBlockState implements IFoodPreference<IBlockState> {

	private HashMap<HashBlockState, Double> map = new HashMap<HashBlockState, Double>();

	@Override
	public double getHunger(IBlockState food) {
		HashBlockState key;

		if (this.map.containsKey(key = new HashBlockState(food, true))) {
			return this.map.get(key);
		} else if (this.map.containsKey(key = new HashBlockState(food, false))) {
			return this.map.get(key);
		} else {
			return 0;
		}
	}

	@Override
	public boolean canEat(ICapabilityHungryAnimal cap, IBlockState food) {
		double hunger = getHunger(food);
		if (hunger == 0)
			return false;
		return cap.getHunger() + hunger < cap.getMaxHunger();
	}

	@Override
	public boolean shouldEat(ICapabilityHungryAnimal cap) {
		return cap.getHunger() + Collections.min(map.values()) < cap.getMaxHunger();
	}

	public static class HashBlockState {
		private IBlockState block;
		private boolean ignoreProperty;

		public HashBlockState(Block block) {
			this(block.getDefaultState(), true);
		}

		public HashBlockState(IBlockState block) {
			this(block, false);
		}

		public HashBlockState(IBlockState block, boolean ignoreProperty) {
			this.block = block;
			this.ignoreProperty = ignoreProperty;
		}

		@Override
		public boolean equals(Object obj) {
			if (this.ignoreProperty && ((HashBlockState) obj).ignoreProperty) {
				return block.getBlock() == ((HashBlockState) obj).block.getBlock();
			} else if (!this.ignoreProperty && !((HashBlockState) obj).ignoreProperty) {

				if (block.getBlock() != ((HashBlockState) obj).block.getBlock())
					return false;

				for (Object i : block.getProperties().keySet()) {
					IProperty property = (IProperty) i;
					if (!block.getValue(property).equals(((HashBlockState) obj).block.getValue(property)))
						return false;
				}

				return true;
			} else {
				return false;
			}
		}

		@Override
		public int hashCode() {
			if (ignoreProperty) {
				return block.getBlock().hashCode();
			} else {
				return block.hashCode();
			}
		}

		public static class Serializer implements JsonDeserializer<HashBlockState>, JsonSerializer<HashBlockState> {
			public HashBlockState deserialize(JsonElement ele, Type type, JsonDeserializationContext context)
					throws JsonParseException {
				JsonObject jsonobject = ele.getAsJsonObject();
				String name = JsonUtils.getString(jsonobject, "name");
				Block block = Block.REGISTRY.getObject(new ResourceLocation(name));

				if (jsonobject.entrySet().size() == 1) {
					return new HashBlockState(block); 
				}
				
				IBlockState state = block.getDefaultState();
				Collection<IProperty<?>> key = state.getPropertyNames();
				for (IProperty i : key) {
					if (JsonUtils.hasField(jsonobject, i.getName())) {
						String jsonValue = JsonUtils.getString(jsonobject, i.getName());
						Comparable comp = (Comparable) i.parseValue(jsonValue).get();
						if (i.getAllowedValues().contains(comp)) {
							state = state.withProperty(i, comp);
						}
					}
				}

				return new HashBlockState(state);
			}

			public JsonElement serialize(HashBlockState block, Type type, JsonSerializationContext context) {

				if (block.ignoreProperty) {
					JsonObject jsonobject = new JsonObject();
					jsonobject.addProperty("name", block.block.getBlock().getRegistryName().toString());
					return new JsonObject();
				} else {
					JsonObject jsonobject = new JsonObject();
					jsonobject.addProperty("name", block.block.getBlock().getRegistryName().toString());
					for (Entry<IProperty<?>, Comparable<?>> i : block.block.getProperties().entrySet()) {
						jsonobject.addProperty(i.getKey().getName(), i.getValue().toString());
					}
					return jsonobject;
				}
			}
		}
	}

}
