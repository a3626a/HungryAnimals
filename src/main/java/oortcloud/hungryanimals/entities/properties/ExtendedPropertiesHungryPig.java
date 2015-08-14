package oortcloud.hungryanimals.entities.properties;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import oortcloud.hungryanimals.entities.properties.handler.GenericPropertiesHandler;
import oortcloud.hungryanimals.entities.properties.handler.GenericProperty;

public class ExtendedPropertiesHungryPig extends ExtendedPropertiesHungryAnimal {

	public EntityPig entity;

	@Override
	public void init(Entity entity, World world) {
		super.init(entity, world);
		this.entity = (EntityPig) entity;

		acceptProperty(((GenericProperty)GenericPropertiesHandler.getInstance().propertyMap.get(entity.getClass())));
		
		taming_factor = 0.998;
		this.hunger = this.hunger_max / 2.0;
		this.excretion = 0;
		this.taming = -2;
	}

	@Override
	protected void applyEntityAttributes() {
		this.entity.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
		this.entity.heal(this.entity.getMaxHealth());
		this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
	}
	
	@Override
	public void postInit() {
		super.postInit();
		this.entity.tasks.addTask(4, new EntityAITempt(this.entity,1.5D, Items.carrot_on_a_stick,false));
	}

	@Override
	public void dropFewItems(boolean isHitByPlayer, int looting, List<EntityItem> drops) {
		super.dropFewItems(isHitByPlayer, looting, drops);
		if (this.entity.getSaddled()) {
			this.entity.dropItem(Items.saddle, 1);
		}
		if (entity.isBurning()) {
			for (EntityItem i : drops) {
				if (i.getEntityItem().getItem() == Items.porkchop) {
					i.setEntityItemStack(new ItemStack(Items.cooked_porkchop, i.getEntityItem().stackSize));
				}
			}
		}
	}

}
