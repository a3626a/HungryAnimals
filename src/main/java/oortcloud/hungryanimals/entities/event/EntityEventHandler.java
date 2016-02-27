package oortcloud.hungryanimals.entities.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryAnimal;
import oortcloud.hungryanimals.entities.properties.handler.HungryAnimalManager;

public class EntityEventHandler {

	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
		if (!(event.entity instanceof EntityAnimal))
			return;
		
		EntityAnimal animal = (EntityAnimal) event.entity;
		
		if (HungryAnimalManager.getInstance().isRegistered(animal.getClass())) {
			event.entity.registerExtendedProperties(Strings.extendedPropertiesKey, HungryAnimalManager.getInstance().createProperty(animal));
		}
		
	}

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		IExtendedEntityProperties property = event.entity.getExtendedProperties(Strings.extendedPropertiesKey);
		if (property != null) {
			((ExtendedPropertiesHungryAnimal)property).postInit();
		}
	}

	@SubscribeEvent
	public void onLivingEntityUpdate(LivingEvent.LivingUpdateEvent event) {
		IExtendedEntityProperties property = event.entity.getExtendedProperties(Strings.extendedPropertiesKey);
		if (property != null) {
			((ExtendedPropertiesHungryAnimal) property).update();
		}
	}

	@SubscribeEvent
	public void onLivingEntityAttackedByPlayer(LivingAttackEvent event) {
		IExtendedEntityProperties property = event.entity.getExtendedProperties(Strings.extendedPropertiesKey);
		if (property != null) {
			((ExtendedPropertiesHungryAnimal) property).onAttackedByPlayer(event.ammount, event.source);
		}
	}

	@SubscribeEvent
	public void onInteract(EntityInteractEvent event) {
		IExtendedEntityProperties property = event.entity.getExtendedProperties(Strings.extendedPropertiesKey);
		if (property != null) {
			event.setCanceled(((ExtendedPropertiesHungryAnimal)property).interact(event.entityPlayer));
		}
	}
	
	@SubscribeEvent
	public void onEntityDrops(LivingDropsEvent event) {
		IExtendedEntityProperties property = event.entity.getExtendedProperties(Strings.extendedPropertiesKey);
		if (property != null) {
			((ExtendedPropertiesHungryAnimal)property).dropFewItems(event.recentlyHit, event.lootingLevel, event.drops);
		}
	}

}
