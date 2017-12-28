package oortcloud.hungryanimals.core.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ICapabilityTamableAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderTamableAnimal;

public class HandlerEntityClient implements IMessageHandler<PacketEntityClient, PacketEntityServer> {

	@Override
	public PacketEntityServer onMessage(PacketEntityClient message, MessageContext ctx) {
		Minecraft.getMinecraft().addScheduledTask(()->{
			World world =  FMLClientHandler.instance().getClient().world;
			if (world == null) 
				return;
			
			Entity entity = world.getEntityByID(message.entityID);
			if (entity == null)
				return;
			
			switch (message.index) {
			case SyncIndex.TAMING_LEVEL_SYNC:
				if (entity.hasCapability(ProviderTamableAnimal.CAP, null)) {
					ICapabilityTamableAnimal cap = entity.getCapability(ProviderTamableAnimal.CAP, null);
					cap.setTaming(message.getDouble());
				}
				break;
			case SyncIndex.STOMACH_SYNC:
				if (entity.hasCapability(ProviderHungryAnimal.CAP, null)) {
					ICapabilityHungryAnimal cap = entity.getCapability(ProviderHungryAnimal.CAP, null);
					cap.setStomach(message.getDouble());
				}
				break;
			}
		});

		return null;
	}

}
