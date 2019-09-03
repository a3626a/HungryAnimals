package oortcloud.hungryanimals.core.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;

public class PacketClientSyncProducingInteraction extends PacketClientSyncProducing {
	
	public int cooldown;
	
	public PacketClientSyncProducingInteraction() {
		this(null, "", 0);
	}

	public PacketClientSyncProducingInteraction(Entity entity, String name, int cooldown) {
		super(entity, name);
		this.cooldown = cooldown;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		cooldown = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeInt(cooldown);
	}
}
