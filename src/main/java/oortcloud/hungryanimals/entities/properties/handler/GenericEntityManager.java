package oortcloud.hungryanimals.entities.properties.handler;

import java.util.ArrayList;

import net.minecraft.entity.passive.EntityAnimal;

public class GenericEntityManager {

	private static GenericEntityManager instance;
	
	public ArrayList<Class<? extends EntityAnimal>> entities;
	
	private GenericEntityManager() {
		entities = new ArrayList<Class<? extends EntityAnimal>>();
	};
	
	public static GenericEntityManager getInstance() {
		if (instance == null) {
			instance = new GenericEntityManager();
		}
		return instance;
	}
	
}
