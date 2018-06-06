package oortcloud.hungryanimals.core.network;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import oortcloud.hungryanimals.client.ParticleMilk;

public class HandlerGeneralClient implements IMessageHandler<PacketGeneralClient, PacketGeneralServer> {

	@Override
	public PacketGeneralServer onMessage(PacketGeneralClient message, MessageContext ctx) {
		Minecraft.getMinecraft().addScheduledTask((Runnable) () -> {
			switch (message.index) {
			case SyncIndex.SPAWN_MILK_PARTICLE:
				double x = message.getDouble();
				double y = message.getDouble();
				double z = message.getDouble();

				for (int i = 0; i < 10; i++) {
					double v = Math.random() * 0.1 + 0.03;
					double r = Math.random() * 2 * Math.PI;

					Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleMilk(Minecraft.getMinecraft().world, x, y, z, v * Math.cos(r), 0, v * Math.sin(r)));
				}
			}
		});
		return null;
	}
}
