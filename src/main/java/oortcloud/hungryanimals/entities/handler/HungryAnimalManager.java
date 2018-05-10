package oortcloud.hungryanimals.entities.handler;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.passive.EntityAnimal;

public class HungryAnimalManager {

	private static HungryAnimalManager INSTANCE;

	private Map<Class<? extends EntityAnimal>, HungryAnimalEntry> REGISTRY;
	
	public static class HungryAnimalEntry {
		public boolean disableTaming;
		public boolean modelGrowing;
		
		public HungryAnimalEntry() {
			disableTaming = false;
			modelGrowing = true;
		}
		
		public HungryAnimalEntry(boolean disableTaming, boolean modelGrowing) {
			this.disableTaming = disableTaming;
			this.modelGrowing = modelGrowing;
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

	public boolean register(Class<? extends EntityAnimal> animal) {
		return register(animal, false, true);
	}

	public boolean register(Class<? extends EntityAnimal> animal, boolean disableTaming, boolean modelGrowing) {
		if (!REGISTRY.containsKey(animal)) {
			REGISTRY.put(animal, new HungryAnimalEntry(disableTaming, modelGrowing));
			return true;
		}
		return false;
	}
	
	public Iterable<Class<? extends EntityAnimal>> getRegisteredAnimal() {
		return REGISTRY.keySet();
	}

	public boolean isRegistered(Class<? extends EntityAnimal> animal) {
		return REGISTRY.containsKey(animal);
	}

	public boolean isDisabledTaming(Class<? extends EntityAnimal> animal) {
		if (REGISTRY.containsKey(animal)) {
			return REGISTRY.get(animal).disableTaming;
		}
		return true;
	}
	
	public boolean isModelGrowing(Class<? extends EntityAnimal> animal) {
		if (REGISTRY.containsKey(animal)) {
			return REGISTRY.get(animal).modelGrowing;
		}
		return true;
	}
	
	public void init() {

	}

}
