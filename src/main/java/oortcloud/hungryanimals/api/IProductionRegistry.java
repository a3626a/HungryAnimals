package oortcloud.hungryanimals.api;

import java.util.function.Function;

import com.google.common.base.Predicate;
import com.google.gson.JsonElement;

import net.minecraft.entity.EntityLiving;
import oortcloud.hungryanimals.entities.production.IProduction;

public interface IProductionRegistry {
	public void registerParser(String type, Function<JsonElement, Function<EntityLiving, IProduction>> parser);
	public void registerCondition(String name, Function<JsonElement, Predicate<EntityLiving>> parser);
}
