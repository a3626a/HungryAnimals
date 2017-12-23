package oortcloud.hungryanimals.core.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;

public class PacketEntityClient extends PacketBasicServer {

	public int entityID;
	
	public PacketEntityClient() {
		this(0,null);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		entityID = getInt();
	}
	
	public PacketEntityClient(int index, Entity entity) {
		super(index);
		if (entity != null) {
			this.setInt(entity.getEntityId());
		} else {
			this.setInt(-1);
		}
	}
}
