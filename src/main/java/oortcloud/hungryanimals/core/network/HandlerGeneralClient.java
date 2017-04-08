package oortcloud.hungryanimals.core.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class HandlerGeneralClient implements IMessageHandler<PacketGeneralClient, PacketGeneralServer> {

	@Override
	public PacketGeneralServer onMessage(PacketGeneralClient message, MessageContext ctx) {
		switch (message.index) {
		}
		return null;
	}
}
