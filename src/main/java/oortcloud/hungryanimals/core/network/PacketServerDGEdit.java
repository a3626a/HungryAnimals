package oortcloud.hungryanimals.core.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketServerDGEdit implements IMessage {

	public int id;
	public String target;
	
	public PacketServerDGEdit() {
		this(null, "");
	}
	
	public PacketServerDGEdit(Entity entity, String target) {
		if (entity != null) {
			this.id = entity.getEntityId();
		} else {
			this.id = -1;
		}
		this.target = target;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		id = buf.readInt();
		target = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(id);
		ByteBufUtils.writeUTF8String(buf, target);
	}

}
