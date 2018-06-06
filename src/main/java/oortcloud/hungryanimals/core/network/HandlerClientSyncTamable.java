package oortcloud.hungryanimals.core.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import oortcloud.hungryanimals.entities.capability.ICapabilityTamableAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderTamableAnimal;

public class HandlerClientSyncTamable implements IMessageHandler<PacketClientSyncTamable, IMessage> {

	@Override
	public IMessage onMessage(PacketClientSyncTamable message, MessageContext ctx) {
		Minecraft.getMinecraft().addScheduledTask(() -> {
			World world = FMLClientHandler.instance().getClient().world;
			if (world == null)
				return;

			Entity entity = world.getEntityByID(message.id);
			if (entity == null)
				return;

			ICapabilityTamableAnimal cap = entity.getCapability(ProviderTamableAnimal.CAP, null);
			if (cap != null)
				cap.setTaming(message.taming);
		});
		return null;
	}

}
