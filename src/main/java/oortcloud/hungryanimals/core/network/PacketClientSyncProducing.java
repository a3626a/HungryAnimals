package oortcloud.hungryanimals.core.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketClientSyncProducing extends PacketClientEntity {
	
	public String name;
	
	public PacketClientSyncProducing() {
		this(null, "");
	}

	public PacketClientSyncProducing(Entity entity, String name) {
		super(entity);
		this.name = name;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		name = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		ByteBufUtils.writeUTF8String(buf, name);
	}
}
