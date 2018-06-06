package oortcloud.hungryanimals.core.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;

public class PacketClientSyncTamable extends PacketClientEntity {
	
	public double taming;
	
	public PacketClientSyncTamable() {
		this(null, 0);
	}

	public PacketClientSyncTamable(Entity entity, double taming) {
		super(entity);
		this.taming = taming;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		taming = buf.readDouble();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeDouble(taming);
	}

}
