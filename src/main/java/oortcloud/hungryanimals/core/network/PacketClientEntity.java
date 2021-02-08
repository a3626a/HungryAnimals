package oortcloud.hungryanimals.core.network;

import net.minecraft.network.PacketBuffer;

public class PacketClientEntity {
	public int entityId;
	
	public PacketClientEntity(int entityId) {
		this.entityId = entityId;
	}

	public PacketClientEntity(PacketBuffer buf) {
		this.entityId = buf.readInt();
	}

	public void toBytes(PacketBuffer buf) {
		buf.writeInt(entityId);
	}
}
