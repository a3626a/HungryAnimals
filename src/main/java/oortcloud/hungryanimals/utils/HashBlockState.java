package oortcloud.hungryanimals.utils;

import java.util.Collection;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockState;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import oortcloud.hungryanimals.HungryAnimals;

public class HashBlockState {
	private BlockState block;
	private boolean ignoreProperty;

	public HashBlockState(Block block) {
		this(block.getDefaultState(), true);
	}

	public HashBlockState(BlockState block) {
		this(block, false);
	}

	public HashBlockState(BlockState block, boolean ignoreProperty) {
		this.block = block;
		this.ignoreProperty = ignoreProperty;
	}

	public boolean apply(BlockState block) {
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

	public BlockState toBlockState() {
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

		BlockState state = block.getDefaultState();
		Collection<IProperty<?>> key = state.getPropertyKeys();
		for (IProperty<?> i : key) {
			if (JsonUtils.hasField(jsonobject, i.getName())) {
				String jsonValue = JsonUtils.getString(jsonobject, i.getName());
				state = getState(state, i, jsonValue);
			}
		}
		return new HashBlockState(state);
	}

	private static <T extends Comparable<T>> BlockState getState(BlockState state, IProperty<T> property, String value) {
		return state.withProperty(property, property.parseValue(value).get());
	}

}
