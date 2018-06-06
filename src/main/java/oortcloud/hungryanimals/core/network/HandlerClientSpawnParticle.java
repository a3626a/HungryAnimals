package oortcloud.hungryanimals.core.network;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import oortcloud.hungryanimals.client.ParticleMilk;

public class HandlerClientSpawnParticle implements IMessageHandler<PacketClientSpawnParticle, IMessage> {

	@Override
	public IMessage onMessage(PacketClientSpawnParticle message, MessageContext ctx) {
		Minecraft.getMinecraft().addScheduledTask(() -> {
			double x = message.pos.x;
			double y = message.pos.y;
			double z = message.pos.z;

			for (int i = 0; i < 10; i++) {
				double v = Math.random() * 0.1 + 0.03;
				double r = Math.random() * 2 * Math.PI;

				Minecraft.getMinecraft().effectRenderer
						.addEffect(new ParticleMilk(Minecraft.getMinecraft().world, x, y, z, v * Math.cos(r), 0, v * Math.sin(r)));
			}
		});
		return null;
	}

}
