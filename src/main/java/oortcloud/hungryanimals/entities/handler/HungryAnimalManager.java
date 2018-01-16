package oortcloud.hungryanimals.entities.handler;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.passive.EntityAnimal;

public class HungryAnimalManager {

	private static HungryAnimalManager INSTANCE;

	private Map<Class<? extends EntityAnimal>, HungryAnimalEntry> REGISTRY;
	
	public static class HungryAnimalEntry {
		public boolean disableTaming;
		
		public HungryAnimalEntry() {
			disableTaming = false;
		}
		
		public HungryAnimalEntry(boolean disableTaming) {
			this.disableTaming = disableTaming;
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
		return register(animal, false);
	}

	public boolean register(Class<? extends EntityAnimal> animal, boolean disableTaming) {
		if (!REGISTRY.containsKey(animal)) {
			REGISTRY.put(animal, new HungryAnimalEntry(disableTaming));
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
	
	public void init() {

	}

}
