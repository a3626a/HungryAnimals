package oortcloud.hungryanimals.entities.properties;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import oortcloud.hungryanimals.entities.properties.handler.HungryAnimalManager;
import oortcloud.hungryanimals.entities.properties.handler.AnimalCharacteristic;
import oortcloud.hungryanimals.entities.properties.handler.ModAttributes;

public class ExtendedPropertiesHungryRabbit extends ExtendedPropertiesHungryAnimal {

	public EntityRabbit entity;

	@Override
	public void init(Entity entity, World world) {
		super.init(entity, world);
		this.entity = (EntityRabbit) entity;
	}
	
	public void postInit() {
		super.postInit();
		this.removeAI(EntityRabbit.class.getDeclaredClasses());
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
