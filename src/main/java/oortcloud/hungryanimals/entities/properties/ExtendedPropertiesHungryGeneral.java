package oortcloud.hungryanimals.entities.properties;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.world.World;
import oortcloud.hungryanimals.entities.properties.handler.GenericPropertiesHandler;
import oortcloud.hungryanimals.entities.properties.handler.GenericProperty;

public class ExtendedPropertiesHungryGeneral extends ExtendedPropertiesHungryAnimal{

	private EntityAnimal entity;
	private Class entityClass;
	
	public ExtendedPropertiesHungryGeneral(Class entityClass) {
		this.entityClass = entityClass;
	}
	
	@Override
	public void init(Entity entity, World world) {
		super.init(entity, world);
		this.entity = (EntityAnimal) entity;

		acceptProperty(((GenericProperty)GenericPropertiesHandler.getInstance().propertyMap.get(entityClass)));
		
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
	
}
