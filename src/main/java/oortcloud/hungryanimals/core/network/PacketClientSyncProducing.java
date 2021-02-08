package oortcloud.hungryanimals.core.network;

import net.minecraft.network.PacketBuffer;

public class PacketClientSyncProducing extends PacketClientEntity {
	
	public String name;

	public PacketClientSyncProducing(int entityId, String name) {
		super(entityId);
		this.name = name;
	}

	public PacketClientSyncProducing(PacketBuffer buf) {
		super(buf);
		this.name = buf.readString();
	}

	@Override
	public void toBytes(PacketBuffer buf) {
		super.toBytes(buf);
		buf.writeString(name);
	}
}
