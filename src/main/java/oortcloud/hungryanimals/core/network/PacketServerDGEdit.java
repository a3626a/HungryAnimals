package oortcloud.hungryanimals.core.network;

import net.minecraft.network.PacketBuffer;

public class PacketServerDGEdit {
	public int entityId;
	public String target;
	
	public PacketServerDGEdit(int entityId, String target) {
		this.entityId = entityId;
		this.target = target;
	}

	public PacketServerDGEdit(PacketBuffer buf) {
		this(buf.readInt(), buf.readString());
	}

	public void toBytes(PacketBuffer buf) {
		buf.writeInt(entityId);
		buf.writeString(target);
	}
}
