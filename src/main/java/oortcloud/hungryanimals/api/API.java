package oortcloud.hungryanimals.api;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.passive.EntityAnimal;
import oortcloud.hungryanimals.entities.properties.handler.HungryAnimalManager;

public class API {
	
	/**
	 * It registers given animalclass to Hungry Animals.
	 * This will make the animals have default ExtendedProperty.
	 * It also enables other registration for attributes and AIs.
	 * 
	 * @param animalclass
	 * @return true if registration failed, otherwise false
	 */
	public static boolean registerAnimal(Class<? extends EntityAnimal> animalclass) {
		return registerAnimal(animalclass);
	}
	
	/**
	 * It registers the attribute with val to the animalclass.
	 * When shouldRegistered is true, the attribute will be registered to the entity,
	 * when shouldRegistered is false, the attribute will only be set the base value as val.
	 * You must set shouldRegistered to false for attributes that is registered by vanila or other mods.
	 * For example, max health and move speed.
	 * This val may be ignored and a value from configuration could be used.
	 * 
	 * @param animalclass
	 * @param attribute
	 * @param val
	 * @param shouldRegistered
	 * @return true if registration failed, otherwise false
	 */
	public static boolean registerAttribute(Class<? extends EntityAnimal> animalclass, IAttribute attribute, double val, boolean shouldRegistered) {
		HungryAnimalManager.getInstance().getAnimalDefaultCharacteristic(animalclass).putAttribute(attribute, val, shouldRegistered);;
		
		return true;
	}
	
	/**
	 * It registers given aifactory to the animalclass.
	 * AIFactory is a functional interface, which takes EntityAnimal and returns EntityAIBase.
	 * 
	 * @param animalclass
	 * @param ai
	 * @return true if registration failed, otherwise false
	 */
	public static boolean registerAI(Class<? extends EntityAnimal> animalclass, AIFactory aifactory) {
		return false;
	}
	
	/**
	 * It is similar to registerAI,
	 * but instances made by aifactory can be retrieved with the key by calling getAI.
	 * 
	 * @param animalclass
	 * @param key
	 * @param ai
	 * @return true if registration failed, otherwise false
	 */
	public static boolean registerAccessableAI(Class<? extends EntityAnimal> animalclass, String key, AIFactory aifactory) {
		return false;
	}
	
	/**
	 * It returns the EntityAIBase that is registered by
	 * registerAccessableAI with given key.
	 * 
	 * @param animalclass
	 * @param key
	 * @return EntityAIBase if the key exists, null otherwise
	 */
	public static EntityAIBase getAI(EntityAnimal animal, String key) {
	
		return null;
	}
	
	@FunctionalInterface
	public interface AIFactory {
		public EntityAIBase getAI(EntityAnimal animal);
	}
}
