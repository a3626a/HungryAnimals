package oortcloud.hungryanimals.entities.properties;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import oortcloud.hungryanimals.entities.properties.handler.GeneralPropertiesHandler;
import oortcloud.hungryanimals.entities.properties.handler.GeneralProperty;

public class ExtendedPropertiesHungryRabbit extends ExtendedPropertiesHungryAnimal {

	public EntityRabbit entity;

	@Override
	public void init(Entity entity, World world) {
		super.init(entity, world);
		this.entity = (EntityRabbit) entity;

		acceptProperty(((GeneralProperty)GeneralPropertiesHandler.getInstance().propertyMap.get(entity.getClass())));
		
		taming_factor = 0.998;
		this.hunger = this.hunger_max / 2.0;
		this.excretion = 0;
		this.taming = -2;
	}
	
	public void postInit() {
		super.postInit();
		this.removeAI(EntityRabbit.class.getDeclaredClasses());
		//this.removeAI(new Class[] {Class.forName("net.minecraft.entity.passive.EntityRabbit.AIPanic"), Class.forName("net.minecraft.entity.passive.EntityRabbit.AIRaidFarm"), Class.forName("net.minecraft.entity.passive.EntityRabbit.AIAvoidEntity") });
	}

	@Override
	protected void applyEntityAttributes() {
		this.entity.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
		this.entity.heal(this.entity.getMaxHealth());
		this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3D);
	}

	@Override
	public void dropFewItems(boolean isHitByPlayer, int looting, List<EntityItem> drops) {
		super.dropFewItems(isHitByPlayer, looting, drops);
		if (entity.isBurning()) {
			for (EntityItem i : drops) {
				if (i.getEntityItem().getItem() == Items.rabbit) {
					i.setEntityItemStack(new ItemStack(Items.cooked_rabbit, i.getEntityItem().stackSize));
				}
			}
		}
	}
}
