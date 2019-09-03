package oortcloud.hungryanimals.core.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketClientEntity implements IMessage {
	public int id;
	
	public PacketClientEntity() {
		this(null);
	}

	public PacketClientEntity(Entity entity) {
		if (entity != null) {
			this.id = entity.getEntityId();
		} else {
			this.id = -1;
		}
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		id = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(id);
	}
}
