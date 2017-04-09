package oortcloud.hungryanimals.entities.capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.entities.handler.HungryAnimalManager;

public class CapabilityHandler {
	public static final ResourceLocation CAP_HUNGRYANIMALS = new ResourceLocation(References.MODID, "hungryanimal");
	public static final ResourceLocation CAP_TAMABLEANIMALS = new ResourceLocation(References.MODID, "tamableanimal");
	
	@SubscribeEvent
	public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
		if (!(event.getObject() instanceof EntityAnimal))
			return;
		
		EntityAnimal animal = (EntityAnimal) event.getObject();
		if (HungryAnimalManager.getInstance().isRegistered(animal.getClass())) {
			event.addCapability(CAP_HUNGRYANIMALS, new ProviderHungryAnimal(animal));
			event.addCapability(CAP_TAMABLEANIMALS, new ProviderTamableAnimal());
		}
	}
}
