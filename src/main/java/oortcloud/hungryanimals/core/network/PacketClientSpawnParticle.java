package oortcloud.hungryanimals.core.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketClientSpawnParticle implements IMessage {
	
	public Vec3d pos;
	
	public PacketClientSpawnParticle() {
		this(Vec3d.ZERO);
	}

	public PacketClientSpawnParticle(Vec3d pos) {
		this.pos = pos;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeDouble(pos.x);
		buf.writeDouble(pos.y);
		buf.writeDouble(pos.z);
	}

}
