package oortcloud.hungryanimals.core.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import oortcloud.hungryanimals.entities.capability.CapabilityProducingAnimal;
import oortcloud.hungryanimals.entities.capability.ICapabilityProducingAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderProducingAnimal;

import java.util.function.Supplier;

public class PacketClientSyncProducingInteraction extends PacketClientSyncProducing {
	public int cooldown;
	
	public PacketClientSyncProducingInteraction(int entityId, String name, int cooldown) {
		super(entityId, name);
		this.cooldown = cooldown;
	}

	public PacketClientSyncProducingInteraction(PacketBuffer buf) {
		super(buf);
		this.cooldown = buf.readInt();
	}

	@Override
	public void toBytes(PacketBuffer buf) {
		super.toBytes(buf);
		buf.writeInt(cooldown);
	}

	public void onMessage(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(
				() -> {
					Entity entity = Minecraft.getInstance().world.getEntityByID(entityId);
					if (entity == null)
						return;

					entity.getCapability(ProviderProducingAnimal.CAP).ifPresent(
							cap -> {
								if (cap instanceof CapabilityProducingAnimal) {
									((CapabilityProducingAnimal) cap).readFrom(this);
								}
							}
					);
				}
		);
		context.get().setPacketHandled(true);
	}
}
