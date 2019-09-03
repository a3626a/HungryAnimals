package oortcloud.hungryanimals.core.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;

public class PacketServerDGEditInt extends PacketServerDGEdit {

	public int value;
	
	public PacketServerDGEditInt() {
		this(null, "", 0);
	}
	
	public PacketServerDGEditInt(Entity entity, String target, int value) {
		super(entity, target);
		this.value = value;
	}
	
	public PacketServerDGEditInt(int entity, String target, int value) {
		this.id = entity;
		this.target = target;
		this.value = value;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		value = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		buf.writeInt(value);
	}

}
