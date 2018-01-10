package oortcloud.hungryanimals.utils;

import java.util.Collection;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import oortcloud.hungryanimals.HungryAnimals;

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

	public boolean apply(IBlockState block) {
		if (this.block.getBlock() != block.getBlock()) {
			return false;
		}
		
		if (!ignoreProperty) {
			for (IProperty<?> i : block.getProperties().keySet()) {
				if (!block.getValue(i).equals(this.block.getValue(i)))
					return false;
			}
		}
		
		return true;
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

	public IBlockState toBlockState() {
		if (ignoreProperty) {
			HungryAnimals.logger.warn("Get block state from HashBlockState with ignoreProperty == true,");
			HungryAnimals.logger.warn("    Block State is not well defined,");
			HungryAnimals.logger.warn("    Block : {}", toString());
		}
		return block;
	}
	
	public static HashBlockState parse(JsonElement ele) {
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

	public static JsonElement serialize(HashBlockState block) {
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
