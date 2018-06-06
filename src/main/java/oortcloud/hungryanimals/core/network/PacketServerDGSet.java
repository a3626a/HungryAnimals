package oortcloud.hungryanimals.core.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketServerDGSet implements IMessage {

	public String player;
	public int entity;
	
	public PacketServerDGSet() {
		this(null, null);
	}
	
	public PacketServerDGSet(EntityPlayer player, Entity entity) {
		if (entity != null) {
			this.entity = entity.getEntityId();
		} else {
			this.entity = -1;
		}
		if (player != null) {
			this.player = player.getName();
		} else {
			this.player = "";
		}
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		player = ByteBufUtils.readUTF8String(buf);
		entity = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeUTF8String(buf, player);
		buf.writeInt(entity);
	}

}
