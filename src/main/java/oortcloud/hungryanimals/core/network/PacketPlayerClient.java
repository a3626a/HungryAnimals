package oortcloud.hungryanimals.core.network;

import io.netty.buffer.ByteBuf;

public class PacketPlayerClient extends PacketBasicClient {

	public String name;
	
	public PacketPlayerClient() {
		this(0,"");
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		this.name=this.getString();
	}
	
	public PacketPlayerClient(int index, String name) {
		super(index);
		this.setString(name);
	}

}
