package oortcloud.hungryanimals.core.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;

public class PacketEntityServer extends PacketBasicServer {

	public Entity entity;
	
	public PacketEntityServer() {
		this(0,null);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		World world = FMLClientHandler.instance().getClient().world;
		if (world != null) {
			Entity entity = world.getEntityByID(this.getInt());
			this.entity = entity;
		}
	}
	
	public PacketEntityServer(int index, Entity entity) {
		super(index);
		if (entity != null) {
			this.setInt(entity.getEntityId());
		} else {
			this.setInt(-1);
		}
	}
}
