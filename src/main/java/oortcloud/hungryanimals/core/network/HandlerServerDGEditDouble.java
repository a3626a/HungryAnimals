package oortcloud.hungryanimals.core.network;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ICapabilityTamableAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderTamableAnimal;

public class HandlerServerDGEditDouble implements IMessageHandler<PacketServerDGEditDouble, IMessage> {

	@Override
	public IMessage onMessage(PacketServerDGEditDouble message, MessageContext ctx) {
		FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
			int id2 = message.id;
			WorldServer[] aworldserver2 = FMLCommonHandler.instance().getMinecraftServerInstance().worlds;
			for (int j = 0; j < aworldserver2.length; ++j) {
				WorldServer worldserver = aworldserver2[j];

				if (worldserver != null) {
					Entity entity = worldserver.getEntityByID(id2);
					if (entity != null && entity instanceof EntityLiving) {
						EntityLiving animal = (EntityLiving) entity;

						switch (message.target) {
						case "nutrient":
							ICapabilityHungryAnimal capHungry1 = animal.getCapability(ProviderHungryAnimal.CAP, null);
							if (capHungry1 != null)
								capHungry1.setNutrient(message.value);
							break;
						case "stomach":
							ICapabilityHungryAnimal capHungry2 = animal.getCapability(ProviderHungryAnimal.CAP, null);
							if (capHungry2 != null)
								capHungry2.setStomach(message.value);
							break;
						case "weight":
							ICapabilityHungryAnimal capHungry3 = animal.getCapability(ProviderHungryAnimal.CAP, null);
							if (capHungry3 != null)
								capHungry3.setWeight(message.value);
							break;
						case "excretion":
							ICapabilityHungryAnimal capHungry4 = animal.getCapability(ProviderHungryAnimal.CAP, null);
							if (capHungry4 != null)
								capHungry4.setExcretion(message.value);
							break;
						case "taming":
							ICapabilityTamableAnimal capTaming = animal.getCapability(ProviderTamableAnimal.CAP, null);
							if (capTaming != null)
								capTaming.setTaming(message.value);
							break;
						}
					}
				}
			}
		});
		return null;
	}

}
