package oortcloud.hungryanimals.core.network;

import net.minecraft.util.BlockPos;
import io.netty.buffer.ByteBuf;

public class PacketTileEntityServer extends PacketBasicServer{

	public int dim;
	public BlockPos pos;
	
	public PacketTileEntityServer() {
		this(0,0,new BlockPos(0,0,0));
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		this.dim=this.getInt();
		this.pos= new BlockPos(this.getInt(),this.getInt(),this.getInt());
	}
	
	public PacketTileEntityServer(int index, int dim, BlockPos pos) {
		super(index);
		this.setInt(dim);
		this.setInt(pos.getX());
		this.setInt(pos.getY());
		this.setInt(pos.getZ());
	}


}
