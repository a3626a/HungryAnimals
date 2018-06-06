package oortcloud.hungryanimals.core.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import oortcloud.hungryanimals.entities.capability.CapabilityProducingAnimal;
import oortcloud.hungryanimals.entities.capability.ICapabilityProducingAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderProducingAnimal;

public class HandlerClientSyncProducingFluid implements IMessageHandler<PacketClientSyncProducingFluid, IMessage> {

	@Override
	public IMessage onMessage(PacketClientSyncProducingFluid message, MessageContext ctx) {
		Minecraft.getMinecraft().addScheduledTask(() -> {
			World world = FMLClientHandler.instance().getClient().world;
			if (world == null)
				return;

			Entity entity = world.getEntityByID(message.id);
			if (entity == null)
				return;

			ICapabilityProducingAnimal capProducing = entity.getCapability(ProviderProducingAnimal.CAP, null);
			if (capProducing != null && capProducing instanceof CapabilityProducingAnimal) {
				((CapabilityProducingAnimal) capProducing).readFrom(message);
			}
		});
		return null;
	}

}
