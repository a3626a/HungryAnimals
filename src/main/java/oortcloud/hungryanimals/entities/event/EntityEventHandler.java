package oortcloud.hungryanimals.entities.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryAnimal;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryChicken;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryCow;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryPig;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryRabbit;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungrySheep;
import oortcloud.hungryanimals.items.ModItems;

public class EntityEventHandler {

	@SubscribeEvent
	public void onEntityDrops(LivingDropsEvent event) {
		EntityLivingBase entity = event.entityLiving;

		if (entity instanceof EntityChicken || entity instanceof EntityCow || entity instanceof EntityPig
				|| entity instanceof EntitySheep || entity instanceof EntityRabbit) {
			((ExtendedPropertiesHungryAnimal) entity.getExtendedProperties(Strings.extendedPropertiesKey)).dropFewItems(
					event.recentlyHit, event.lootingLevel, event.drops);
		}
	}

	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
		if (event.entity instanceof EntityChicken) {
			event.entity.registerExtendedProperties(Strings.extendedPropertiesKey, new ExtendedPropertiesHungryChicken());
		}
		if (event.entity instanceof EntityCow) {
			event.entity.registerExtendedProperties(Strings.extendedPropertiesKey, new ExtendedPropertiesHungryCow());
		}
		if (event.entity instanceof EntityPig) {
			event.entity.registerExtendedProperties(Strings.extendedPropertiesKey, new ExtendedPropertiesHungryPig());
		}
		if (event.entity instanceof EntitySheep) {
			event.entity.registerExtendedProperties(Strings.extendedPropertiesKey, new ExtendedPropertiesHungrySheep());
		}
		if (event.entity instanceof EntityRabbit) {
			event.entity.registerExtendedProperties(Strings.extendedPropertiesKey, new ExtendedPropertiesHungryRabbit());
		}
	}

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		IExtendedEntityProperties property = event.entity.getExtendedProperties(Strings.extendedPropertiesKey);
		if (property != null) {
			((ExtendedPropertiesHungryAnimal) property).postInit();
		}
	}
	
	@SubscribeEvent
	public void onLivingEntityUpdate(LivingEvent.LivingUpdateEvent event) {
		EntityLivingBase entity = event.entityLiving;

		IExtendedEntityProperties property = entity.getExtendedProperties(Strings.extendedPropertiesKey);
		if (property != null) {
			((ExtendedPropertiesHungryAnimal) property).update();
		}
	}

	@SubscribeEvent
	public void onLivingEntityAttackedByPlayer(LivingAttackEvent event) {

		Entity entity = event.entityLiving;
		IExtendedEntityProperties property = entity.getExtendedProperties(Strings.extendedPropertiesKey);
		if (property != null) {
			((ExtendedPropertiesHungryAnimal) property).onAttackedByPlayer(event.ammount, event.source);
		}

	}

	@SubscribeEvent
	public void onInteract(EntityInteractEvent event) {
		Entity entity = event.target;

		ExtendedPropertiesHungryAnimal property = (ExtendedPropertiesHungryAnimal) entity
				.getExtendedProperties(Strings.extendedPropertiesKey);
		if (property != null) {
			event.setCanceled(property.interact(event.entityPlayer));
		}
	}
	
}
