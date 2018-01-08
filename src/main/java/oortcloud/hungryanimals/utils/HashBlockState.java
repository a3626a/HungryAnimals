package oortcloud.hungryanimals.utils;

import java.lang.reflect.Type;
import java.util.Collection;
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

public class HashBlockState {
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

			for (IProperty<?> i : block.getProperties().keySet()) {
				if (!block.getValue(i).equals(((HashBlockState) obj).block.getValue(i)))
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

	public String toString() {
		return block.toString();
	}

	public static class Serializer implements JsonDeserializer<HashBlockState>, JsonSerializer<HashBlockState> {
		public HashBlockState deserialize(JsonElement ele, Type type, JsonDeserializationContext context) throws JsonParseException {
			JsonObject jsonobject = ele.getAsJsonObject();
			String name = JsonUtils.getString(jsonobject, "name");
			Block block = Block.REGISTRY.getObject(new ResourceLocation(name));

			if (jsonobject.entrySet().size() == 1) {
				return new HashBlockState(block);
			}

			IBlockState state = block.getDefaultState();
			Collection<IProperty<?>> key = state.getPropertyKeys();
			for (IProperty<?> i : key) {
				if (JsonUtils.hasField(jsonobject, i.getName())) {
					String jsonValue = JsonUtils.getString(jsonobject, i.getName());
					state = getState(state, i, jsonValue);
				}
			}

			return new HashBlockState(state);
		}

		private static <T extends Comparable<T>> IBlockState getState(IBlockState state, IProperty<T> property, String value) {
			return state.withProperty(property, property.parseValue(value).get());
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
