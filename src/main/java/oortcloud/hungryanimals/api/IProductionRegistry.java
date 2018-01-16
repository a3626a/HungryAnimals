package oortcloud.hungryanimals.api;

import java.util.function.Function;

import com.google.gson.JsonElement;

import net.minecraft.entity.passive.EntityAnimal;
import oortcloud.hungryanimals.entities.production.IProduction;

public interface IProductionRegistry {
	public void registerParser(String type, Function<JsonElement, Function<EntityAnimal, IProduction>> parser);
}
