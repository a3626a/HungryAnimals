package oortcloud.hungryanimals.core.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import oortcloud.hungryanimals.entities.capability.ProviderTamableAnimal;

import java.util.function.Supplier;

public class PacketClientSyncTamable extends PacketClientEntity {
	
	public double taming;
	
	public PacketClientSyncTamable(int entityId, double taming) {
		super(entityId);
		this.taming = taming;
	}

	public PacketClientSyncTamable(PacketBuffer buf) {
		super(buf);
		this.taming = buf.readDouble();
	}

	@Override
	public void toBytes(PacketBuffer buf) {
		super.toBytes(buf);
		buf.writeDouble(taming);
	}

	public void onMessage(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(
				() -> {
					Entity entity = Minecraft.getInstance().world.getEntityByID(entityId);
					if (entity == null)
						return;

					entity.getCapability(ProviderTamableAnimal.CAP).ifPresent(
						cap -> cap.setTaming(taming)
					);
				}
		);
		context.get().setPacketHandled(true);
	}
}
