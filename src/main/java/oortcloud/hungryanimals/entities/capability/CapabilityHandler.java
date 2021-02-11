package oortcloud.hungryanimals.entities.capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.entities.handler.HungryAnimalManager;
import oortcloud.hungryanimals.entities.production.Productions;

@Mod.EventBusSubscriber
public class CapabilityHandler {
	public static final ResourceLocation CAP_HUNGRYANIMALS = new ResourceLocation(References.MODID, "hungryanimal");
	public static final ResourceLocation CAP_TAMABLEANIMALS = new ResourceLocation(References.MODID, "tamableanimal");
	public static final ResourceLocation CAP_PRODUCINGANIMALS = new ResourceLocation(References.MODID, "producinganimal");
	public static final ResourceLocation CAP_SEXUAL = new ResourceLocation(References.MODID, "sexual");
	public static final ResourceLocation CAP_AGEABLE = new ResourceLocation(References.MODID, "ageable");
	
	@SubscribeEvent
	public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
		EntityType<?> entityType = event.getObject().getType();
		if (HungryAnimalManager.getInstance().isRegistered(entityType)) {
			Entity entity = event.getObject();
			if (!(entity instanceof MobEntity)) {
				HungryAnimals.logger.warn(
						"{} is registered to HungryAnimals, but it is not MobEntity. Capability attachment failed.",
						event.getObject().getType().getRegistryName()
				);
				return;
			}
			MobEntity mob = (MobEntity)entity;
			boolean isHungry = HungryAnimalManager.getInstance().isHungry(entityType);
			if (isHungry) {
				event.addCapability(CAP_HUNGRYANIMALS, new ProviderHungryAnimal(mob));
			}

			boolean isTamable = HungryAnimalManager.getInstance().isTamable(entityType);
			if (isTamable) {
				if (mob instanceof AbstractHorseEntity) {
					event.addCapability(CAP_TAMABLEANIMALS, new ProviderTamableAnimal((AbstractHorseEntity) mob));
				} else {
					event.addCapability(CAP_TAMABLEANIMALS, new ProviderTamableAnimal(mob));
				}
			}
			
			if (Productions.getInstance().hasProduction(entityType)) {
				event.addCapability(CAP_PRODUCINGANIMALS, new ProviderProducingAnimal(mob));
			}
			
			boolean isSexual = HungryAnimalManager.getInstance().isSexual(entityType);
			if (isSexual) {
				event.addCapability(CAP_SEXUAL, new ProviderSexual(mob));
			}
			
			boolean isAgeable = HungryAnimalManager.getInstance().isAgeable(entityType);
			if (isAgeable) {
				if (mob instanceof AgeableEntity) {
					event.addCapability(CAP_AGEABLE, new ProviderAgeable((AgeableEntity)mob));
				} else {
					event.addCapability(CAP_AGEABLE, new ProviderAgeable(mob));
				}
			}
		}
	}

	@SubscribeEvent
	public static void onStartTracking(PlayerEvent.StartTracking event) {
		Entity target = event.getTarget();
		CapabilityTamableAnimal capTamable = (CapabilityTamableAnimal) target.getCapability(ProviderTamableAnimal.CAP).orElse(null);
		if (capTamable != null)
			capTamable.syncTo((ServerPlayerEntity) event.getPlayer());
		CapabilityHungryAnimal capHungry = (CapabilityHungryAnimal) target.getCapability(ProviderHungryAnimal.CAP).orElse(null);
		if (capHungry != null)
			capHungry.syncTo((ServerPlayerEntity) event.getPlayer());
		CapabilityProducingAnimal capProducing = (CapabilityProducingAnimal) target.getCapability(ProviderProducingAnimal.CAP).orElse(null);
		if (capProducing != null)
			capProducing.syncTo((ServerPlayerEntity) event.getPlayer());
	}

}
