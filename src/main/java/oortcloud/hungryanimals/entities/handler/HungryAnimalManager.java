package oortcloud.hungryanimals.entities.handler;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.EntityType;

public class HungryAnimalManager {

	private static HungryAnimalManager INSTANCE;

	private Map<EntityType<?>, HungryAnimalEntry> REGISTRY;
	
	public static class HungryAnimalEntry {
		public boolean isTamable;
		public boolean isModelGrowing;
		public boolean isSexual;
		public boolean isAgeable;
		public boolean isHungry;

		public HungryAnimalEntry() {
			isTamable = true;
			isModelGrowing = true;
			isSexual = true;
			isAgeable = true;
			isHungry = true;
		}
		
	}
	
	public static HungryAnimalManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new HungryAnimalManager();
		}
		return INSTANCE;
	}

	private HungryAnimalManager() {
		REGISTRY = new HashMap<>();
	}

	public boolean register(EntityType<?> animal) {
		return register(animal, new HungryAnimalEntry());
	}

	public boolean register(EntityType<?> animal, HungryAnimalEntry entry) {
		if (!REGISTRY.containsKey(animal)) {
			REGISTRY.put(animal, entry);
			return true;
		}
		return false;
	}
	
	public Iterable<EntityType<?>> getRegisteredAnimal() {
		return REGISTRY.keySet();
	}

	public boolean isRegistered(EntityType<?> animal) {
		return REGISTRY.containsKey(animal);
	}

	public boolean isTamable(EntityType<?> animal) {
		if (REGISTRY.containsKey(animal)) {
			return REGISTRY.get(animal).isTamable;
		}
		return false;
	}
	
	public boolean isModelGrowing(EntityType<?> animal) {
		if (REGISTRY.containsKey(animal)) {
			return REGISTRY.get(animal).isModelGrowing;
		}
		return false;
	}
	
	public boolean isSexual(EntityType<?> animal) {
		if (REGISTRY.containsKey(animal)) {
			return REGISTRY.get(animal).isSexual;
		}
		return false;
	}
	
	public boolean isAgeable(EntityType<?> animal) {
		if (REGISTRY.containsKey(animal)) {
			return REGISTRY.get(animal).isAgeable;
		}
		return false;
	}

	public boolean isHungry(EntityType<?> animal) {
		if (REGISTRY.containsKey(animal)) {
			return REGISTRY.get(animal).isHungry;
		}
		return false;
	}

	
	public void init() {

	}

}
