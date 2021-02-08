package oortcloud.hungryanimals.core.network;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import oortcloud.hungryanimals.entities.capability.ProviderAgeable;

import java.util.function.Supplier;

public class PacketServerDGEditInt extends PacketServerDGEdit {

	public int value;
	
	public PacketServerDGEditInt(int entityId, String target, int value) {
		super(entityId, target);
		this.value = value;
	}

	public PacketServerDGEditInt(PacketBuffer buf) {
		super(buf);
		this.value = buf.readInt();
	}

	public void toBytes(PacketBuffer buf) {
		super.toBytes(buf);
		buf.writeInt(value);
	}

	public void onMessage(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(
			() -> {
				int id3 = entityId;
				ServerPlayerEntity serverPlayerEntity = context.get().getSender();
				if (serverPlayerEntity == null)
					return;
				ServerWorld worldServer = serverPlayerEntity.getServerWorld();
				Entity entity = worldServer.getEntityByID(id3);
				if (entity != null && "age".equals(target)) {
					entity.getCapability(ProviderAgeable.CAP).ifPresent(
							ageable -> ageable.setAge(value)
					);
				}
			}
		);
		context.get().setPacketHandled(true);
	}
}
