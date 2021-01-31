package oortcloud.hungryanimals.core.network;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import oortcloud.hungryanimals.entities.capability.ICapabilityAgeable;
import oortcloud.hungryanimals.entities.capability.ProviderAgeable;

public class HandlerServerDGEditInt implements IMessageHandler<PacketServerDGEditInt, IMessage> {

	@Override
	public IMessage onMessage(PacketServerDGEditInt message, MessageContext ctx) {
		FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
			int id3 = message.id;
			WorldServer[] aworldserver3 = FMLCommonHandler.instance().getMinecraftServerInstance().worlds;
			for (int j = 0; j < aworldserver3.length; ++j) {
				WorldServer worldserver = aworldserver3[j];

				if (worldserver != null) {
					Entity entity = worldserver.getEntityByID(id3);
					if (entity != null && entity instanceof MobEntity) {
						MobEntity animal = (MobEntity) entity;

						switch (message.target) {
						case "age": {
							ICapabilityAgeable ageable = animal.getCapability(ProviderAgeable.CAP, null);
							if (ageable != null) {
								ageable.setAge(message.value);
							}
						}
						}
					}
				}
			}
		});
		return null;
	}

}
