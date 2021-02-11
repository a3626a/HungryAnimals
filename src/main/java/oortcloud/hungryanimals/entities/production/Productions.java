package oortcloud.hungryanimals.entities.production;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.JSONUtils;
import oortcloud.hungryanimals.api.IProductionRegistry;
import oortcloud.hungryanimals.entities.production.condition.Conditions;

public class Productions implements IProductionRegistry {

	private Map<Class<? extends MobEntity>, List<Function<MobEntity, IProduction>>> REGISTRY;
	private Map<String, Function<JsonElement, Function<MobEntity, IProduction>>> PARSER;
	
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

	public boolean registerProduction(Class<? extends MobEntity> animal, Function<MobEntity, IProduction> factory) {
		if (!REGISTRY.containsKey(animal)) {
			REGISTRY.put(animal, new ArrayList<>());
		}
		return REGISTRY.get(animal).add(factory);
	}
	
	public void registerParser(String type, Function<JsonElement, Function<MobEntity, IProduction>> parser) {
		PARSER.put(type, parser);
	}
	
	public boolean hasProduction(EntityType<?> animal) {
		return REGISTRY.containsKey(animal);
	}
	
	public boolean hasProduction(MobEntity animal) {
		return hasProduction(animal.getType());
	}
	
	@Nullable
	public List<IProduction> apply(MobEntity animal) {
		List<Function<MobEntity, IProduction>> functions = REGISTRY.get(animal.getClass());
		if (functions == null)
			return null;
		List<IProduction> productions = new ArrayList<>();
		for (Function<MobEntity, IProduction> i : functions) {
			productions.add(i.apply(animal));
		}
		return productions;
	}

	@Nullable
	public List<IProductionJEI> apply(Class<? extends MobEntity> classEntity) {
		List<Function<MobEntity, IProduction>> functions = REGISTRY.get(classEntity);
		if (functions == null)
			return null;
		List<IProductionJEI> productions = new ArrayList<>();
		for (Function<MobEntity, IProduction> i : functions) {
			if (i instanceof IProductionJEI) {
				productions.add((IProductionJEI)i);
			}
		}
		return productions;
	}
	
	@Nullable
	public Function<MobEntity, IProduction> parse(JsonElement jsonEle) {
		JsonObject jsonObj = jsonEle.getAsJsonObject();
		
		String type = JSONUtils.getString(jsonObj, "type");
		if (!PARSER.containsKey(type)) {
			return null;
		} else {
			return PARSER.get(type).apply(jsonEle);
		}
		
	}

	@Override
	public void registerCondition(String name, Function<JsonElement, Predicate<MobEntity>> parser) {
		Conditions.getInstance().register(name, parser);
	}
	
}
