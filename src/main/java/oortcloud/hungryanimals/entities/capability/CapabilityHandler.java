package oortcloud.hungryanimals.entities.capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.entities.handler.HungryAnimalManager;

@Mod.EventBusSubscriber
public class CapabilityHandler {
	public static final ResourceLocation CAP_HUNGRYANIMALS = new ResourceLocation(References.MODID, "hungryanimal");
	public static final ResourceLocation CAP_TAMABLEANIMALS = new ResourceLocation(References.MODID, "tamableanimal");

	@SubscribeEvent
	public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
		if (!(event.getObject() instanceof EntityAnimal))
			return;

		EntityAnimal animal = (EntityAnimal) event.getObject();
		if (HungryAnimalManager.getInstance().isRegistered(animal.getClass())) {
			event.addCapability(CAP_HUNGRYANIMALS, new ProviderHungryAnimal(animal));
			if (animal instanceof AbstractHorse) {
				event.addCapability(CAP_TAMABLEANIMALS, new ProviderTamableAnimal((AbstractHorse) animal));
			} else {
				event.addCapability(CAP_TAMABLEANIMALS, new ProviderTamableAnimal(animal));
			}
		}
	}

	@SubscribeEvent
	public static void onStartTracking(PlayerEvent.StartTracking event) {
		Entity target = event.getTarget();
		if (target.hasCapability(ProviderTamableAnimal.CAP, null)) {
			CapabilityTamableAnimal cap = (CapabilityTamableAnimal) target.getCapability(ProviderTamableAnimal.CAP, null);
			cap.syncTo((EntityPlayerMP)event.getEntityPlayer());
		}
		if (target.hasCapability(ProviderHungryAnimal.CAP, null)) {
			CapabilityHungryAnimal cap = (CapabilityHungryAnimal) target.getCapability(ProviderHungryAnimal.CAP, null);
			cap.syncTo((EntityPlayerMP)event.getEntityPlayer());
		}
	}

}
