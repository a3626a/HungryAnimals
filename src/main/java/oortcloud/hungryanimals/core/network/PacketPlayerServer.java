package oortcloud.hungryanimals.core.network;

import io.netty.buffer.ByteBuf;

public class PacketPlayerServer extends PacketBasicServer{

	public String name;
	
	public PacketPlayerServer() {
		this(0,"");
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		this.name=this.getString();
	}
	
	public PacketPlayerServer(int index, String name) {
		super(index);
		this.setString(name);
	}

}
