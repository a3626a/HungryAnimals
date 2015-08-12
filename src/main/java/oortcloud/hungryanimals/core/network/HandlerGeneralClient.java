package oortcloud.hungryanimals.core.network;

import oortcloud.hungryanimals.HungryAnimals;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class HandlerGeneralClient implements
		IMessageHandler<PacketGeneralClient, PacketGeneralServer> {

	@Override
	public PacketGeneralServer onMessage(PacketGeneralClient message,
			MessageContext ctx) {
		switch (message.index) {
		case 0:
			if (HungryAnimals.entityOverlay.getEnabled()) {
				HungryAnimals.entityOverlay.bar_hunger = message.getDouble();
				HungryAnimals.entityOverlay.bar_health = message.getDouble();
				HungryAnimals.entityOverlay.bar_age = message.getDouble();
				HungryAnimals.entityOverlay.bar_taming = message.getDouble();
				HungryAnimals.entityOverlay.potions = message.getIntArray();
			}
			break;
		}
		return null;
	}
}
