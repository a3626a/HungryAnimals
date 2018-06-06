package oortcloud.hungryanimals.core.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;

public class PacketClientSyncHungry extends PacketClientEntity {
	
	public double stomach;
	public double weight;
	
	public PacketClientSyncHungry() {
		this(null, 0, 0);
	}

	public PacketClientSyncHungry(Entity entity, double stomach, double weight) {
		super(entity);
		this.stomach = stomach;
		this.weight = weight;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		stomach = buf.readDouble();
		weight = buf.readDouble();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeDouble(stomach);
		buf.writeDouble(weight);
	}
}
