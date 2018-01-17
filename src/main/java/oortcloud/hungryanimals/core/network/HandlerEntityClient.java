package oortcloud.hungryanimals.core.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import oortcloud.hungryanimals.entities.capability.CapabilityProducingAnimal;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ICapabilityProducingAnimal;
import oortcloud.hungryanimals.entities.capability.ICapabilityTamableAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderProducingAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderTamableAnimal;

public class HandlerEntityClient implements IMessageHandler<PacketEntityClient, PacketEntityServer> {

	@Override
	public PacketEntityServer onMessage(PacketEntityClient message, MessageContext ctx) {
		Minecraft.getMinecraft().addScheduledTask(() -> {
			World world = FMLClientHandler.instance().getClient().world;
			if (world == null)
				return;

			Entity entity = world.getEntityByID(message.entityID);
			if (entity == null)
				return;

			switch (message.index) {
			case SyncIndex.TAMING_LEVEL_SYNC:
				ICapabilityTamableAnimal cap1 = entity.getCapability(ProviderTamableAnimal.CAP, null);
				if (cap1 != null)
					cap1.setTaming(message.getDouble());
				break;
			case SyncIndex.STOMACH_SYNC:
				ICapabilityHungryAnimal cap2 = entity.getCapability(ProviderHungryAnimal.CAP, null);
				if (cap2 != null)
					cap2.setStomach(message.getDouble());
				break;
			case SyncIndex.PRODUCTION_SYNC:
				ICapabilityProducingAnimal capProducing = entity.getCapability(ProviderProducingAnimal.CAP, null);
				if (capProducing != null && capProducing instanceof CapabilityProducingAnimal) {
					((CapabilityProducingAnimal)capProducing).readFrom(message);
				}
			}
		});

		return null;
	}

}
