package oortcloud.hungryanimals.core.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;

public class PacketServerDGEditDouble extends PacketServerDGEdit {

	public double value;
	
	public PacketServerDGEditDouble() {
		this(null, "", 0);
	}
	
	public PacketServerDGEditDouble(Entity entity, String target, double value) {
		super(entity, target);
		this.value = value;
	}
	
	public PacketServerDGEditDouble(int entity, String target, double value) {
		this.id = entity;
		this.target = target;
		this.value = value;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		value = buf.readDouble();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeDouble(value);
	}

}
