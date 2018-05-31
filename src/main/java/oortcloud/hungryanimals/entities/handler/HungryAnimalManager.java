package oortcloud.hungryanimals.entities.handler;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.passive.EntityAnimal;

public class HungryAnimalManager {

	private static HungryAnimalManager INSTANCE;

	private Map<Class<? extends EntityAnimal>, HungryAnimalEntry> REGISTRY;
	
	public static class HungryAnimalEntry {
		public boolean isTamable;
		public boolean isModelGrowing;
		public boolean isSexual;
		
		public HungryAnimalEntry() {
			isTamable = false;
			isModelGrowing = true;
			isSexual = true;
		}
		
		public HungryAnimalEntry setTamable(boolean isTamable) {
			this.isTamable = isTamable;
			return this;
		}
		
		public HungryAnimalEntry setModelGrowing(boolean isModelGrowing) {
			this.isModelGrowing = isModelGrowing;
			return this;
		}
		
		public HungryAnimalEntry setSexual(boolean isSexual) {
			this.isSexual = isSexual;
			return this;
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
		return register(animal, new HungryAnimalEntry());
	}

	public boolean register(Class<? extends EntityAnimal> animal, HungryAnimalEntry entry) {
		if (!REGISTRY.containsKey(animal)) {
			REGISTRY.put(animal, entry);
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
			return REGISTRY.get(animal).isTamable;
		}
		return true;
	}
	
	public boolean isModelGrowing(Class<? extends EntityAnimal> animal) {
		if (REGISTRY.containsKey(animal)) {
			return REGISTRY.get(animal).isModelGrowing;
		}
		return false;
	}
	
	public boolean isSexual(Class<? extends EntityAnimal> animal) {
		if (REGISTRY.containsKey(animal)) {
			return REGISTRY.get(animal).isSexual;
		}
		return false;
	}
	
	public void init() {

	}

}
