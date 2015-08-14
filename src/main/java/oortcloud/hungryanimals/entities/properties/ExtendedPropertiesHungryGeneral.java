package oortcloud.hungryanimals.entities.properties;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.world.World;
import oortcloud.hungryanimals.entities.properties.handler.GeneralPropertiesHandler;
import oortcloud.hungryanimals.entities.properties.handler.GeneralProperty;

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

		acceptProperty(((GeneralProperty) GeneralPropertiesHandler.getInstance().propertyMap.get(entityClass)));

		taming_factor = 0.998;
		this.hunger = this.hunger_max / 2.0;
		this.excretion = 0;
		this.taming = -2;
	}

	@Override
	protected void applyEntityAttributes() {
		this.entity.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
		this.entity.heal(this.entity.getMaxHealth());
		this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.15D);
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
