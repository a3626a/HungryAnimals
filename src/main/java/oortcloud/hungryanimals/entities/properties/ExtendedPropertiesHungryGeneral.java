package oortcloud.hungryanimals.entities.properties;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.world.World;
import oortcloud.hungryanimals.entities.properties.handler.HungryAnimalManager;
import oortcloud.hungryanimals.entities.properties.handler.AnimalCharacteristic;
import oortcloud.hungryanimals.entities.properties.handler.ModAttributes;

public class ExtendedPropertiesHungryGeneral extends ExtendedPropertiesHungryAnimal {

	private EntityAnimal entity;
	private Class entityClass;

	public ExtendedPropertiesHungryGeneral(Class entityClass) {
		this.entityClass = entityClass;
	}

	@Override
	public void init(Entity entity, World world) {
		super.init(entity, world);
		this.entity = (EntityAnimal) entity;
	}

	public EntityAnimal createChild(World obj) {
		Constructor constructor;
		try {
			constructor = entityClass.getConstructor(World.class);
			EntityAnimal baby;
			try {
				baby = (EntityAnimal) constructor.newInstance(obj);
				return baby;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			return null;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
}
