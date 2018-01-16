package oortcloud.hungryanimals.entities.production;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.JsonUtils;
import oortcloud.hungryanimals.api.IProductionRegistry;

public class Productions implements IProductionRegistry {

	private Map<Class<? extends EntityAnimal>, List<Function<EntityAnimal, IProduction>>> REGISTRY;
	private Map<String, Function<JsonElement, Function<EntityAnimal, IProduction>>> PARSER;
	
	private static Productions INSTANCE;
	
	private Productions() {
		REGISTRY = new HashMap<>();
		PARSER = new HashMap<>();
	}
	
	public static Productions getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Productions();
		}
		return INSTANCE;
	}

	public boolean registerProduction(Class<? extends EntityAnimal> animal, Function<EntityAnimal, IProduction> factory) {
		if (!REGISTRY.containsKey(animal)) {
			REGISTRY.put(animal, new ArrayList<>());
		}
		return REGISTRY.get(animal).add(factory);
	}
	
	public void registerParser(String type, Function<JsonElement, Function<EntityAnimal, IProduction>> parser) {
		PARSER.put(type, parser);
	}
	
	public boolean hasProduction(Class<? extends EntityAnimal> animal) {
		return REGISTRY.containsKey(animal);
	}
	
	public boolean hasProduction(EntityAnimal animal) {
		return hasProduction(animal.getClass());
	}
	
	@Nullable
	public List<IProduction> apply(EntityAnimal animal) {
		List<Function<EntityAnimal, IProduction>> functions = REGISTRY.get(animal.getClass());
		if (functions == null)
			return null;
		List<IProduction> productions = new ArrayList<>();
		for (Function<EntityAnimal, IProduction> i : functions) {
			productions.add(i.apply(animal));
		}
		return productions;
	}
	
	@Nullable
	public Function<EntityAnimal, IProduction> parse(JsonElement jsonEle) {
		JsonObject jsonObj = jsonEle.getAsJsonObject();
		
		String type = JsonUtils.getString(jsonObj, "type");
		if (!PARSER.containsKey(type)) {
			return null;
		} else {
			return PARSER.get(type).apply(jsonEle);
		}
		
	}

}
