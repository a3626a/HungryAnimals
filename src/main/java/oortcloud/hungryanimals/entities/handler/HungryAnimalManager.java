package oortcloud.hungryanimals.entities.handler;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.EntityLiving;

public class HungryAnimalManager {

	private static HungryAnimalManager INSTANCE;

	private Map<Class<? extends EntityLiving>, HungryAnimalEntry> REGISTRY;
	
	public static class HungryAnimalEntry {
		public boolean isTamable;
		public boolean isModelGrowing;
		public boolean isSexual;
		public boolean isAgeable;
		
		public HungryAnimalEntry() {
			isTamable = true;
			isModelGrowing = true;
			isSexual = true;
			isAgeable = true;
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

	public boolean register(Class<? extends EntityLiving> animal) {
		return register(animal, new HungryAnimalEntry());
	}

	public boolean register(Class<? extends EntityLiving> animal, HungryAnimalEntry entry) {
		if (!REGISTRY.containsKey(animal)) {
			REGISTRY.put(animal, entry);
			return true;
		}
		return false;
	}
	
	public Iterable<Class<? extends EntityLiving>> getRegisteredAnimal() {
		return REGISTRY.keySet();
	}

	public boolean isRegistered(Class<? extends EntityLiving> animal) {
		return REGISTRY.containsKey(animal);
	}

	public boolean isTamable(Class<? extends EntityLiving> animal) {
		if (REGISTRY.containsKey(animal)) {
			return REGISTRY.get(animal).isTamable;
		}
		return false;
	}
	
	public boolean isModelGrowing(Class<? extends EntityLiving> animal) {
		if (REGISTRY.containsKey(animal)) {
			return REGISTRY.get(animal).isModelGrowing;
		}
		return false;
	}
	
	public boolean isSexual(Class<? extends EntityLiving> animal) {
		if (REGISTRY.containsKey(animal)) {
			return REGISTRY.get(animal).isSexual;
		}
		return false;
	}
	
	public boolean isAgeable(Class<? extends EntityLiving> animal) {
		if (REGISTRY.containsKey(animal)) {
			return REGISTRY.get(animal).isAgeable;
		}
		return false;
	}
	
	public void init() {

	}

}
