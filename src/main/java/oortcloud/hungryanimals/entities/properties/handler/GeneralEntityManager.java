package oortcloud.hungryanimals.entities.properties.handler;

import java.util.ArrayList;

import net.minecraft.entity.passive.EntityAnimal;

public class GeneralEntityManager {

	private static GeneralEntityManager instance;
	
	public ArrayList<Class<? extends EntityAnimal>> entities;
	
	private GeneralEntityManager() {
		entities = new ArrayList<Class<? extends EntityAnimal>>();
	};
	
	public void init() {
		entities.clear();
	}
	
	public static GeneralEntityManager getInstance() {
		if (instance == null) {
			instance = new GeneralEntityManager();
		}
		return instance;
	}
	
}
