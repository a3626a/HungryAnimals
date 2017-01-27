package oortcloud.hungryanimals.entities.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.entities.capability.ICapabilityTamableAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderTamableAnimal;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryAnimal;

public class EntityEventHandler {

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		IExtendedEntityProperties property = event.getEntity().getExtendedProperties(Strings.extendedPropertiesKey);
		if (property != null) {
			((ExtendedPropertiesHungryAnimal) property).postInit();
		}
	}

	@SubscribeEvent
	public void onLivingEntityUpdate(LivingEvent.LivingUpdateEvent event) {
		IExtendedEntityProperties property = event.getEntity().getExtendedProperties(Strings.extendedPropertiesKey);
		if (property != null) {
			((ExtendedPropertiesHungryAnimal) property).update();
		}

	}

	@SubscribeEvent
	public void onLivingEntityAttackedByPlayer(LivingAttackEvent event) {
		ICapabilityTamableAnimal cap = event.getEntity().getCapability(ProviderTamableAnimal.CAP, null);

		if (cap == null)
			return;

		EntityLivingBase entity = (EntityLivingBase) event.getEntity();
		DamageSource source = event.getSource();

		if (!entity.isEntityInvulnerable(source)) {
			if (source.getSourceOfDamage() instanceof EntityPlayer) {
				cap.addTaming(-4 / entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue()
						* event.getAmount());
			}
		}
	}

	@SubscribeEvent
	public void onInteract(EntityInteract event) {
		IExtendedEntityProperties property = event.getEntity().getExtendedProperties(Strings.extendedPropertiesKey);
		if (property != null) {
			event.setCanceled(((ExtendedPropertiesHungryAnimal) property).interact(event.getEntityPlayer()));
		}
	}

}
