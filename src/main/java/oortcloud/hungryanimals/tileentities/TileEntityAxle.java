package oortcloud.hungryanimals.tileentities;

import oortcloud.hungryanimals.energy.PowerNetwork;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class TileEntityAxle extends TileEntityPowerTransporter {

	private BlockPos connectedAxle;

	public BlockPos getConnectedAxle() {
		return connectedAxle;
	}
	
	public void setConnectedAxle(BlockPos pos) {
		connectedAxle=pos;
	}
	
	@Override
	public BlockPos[] getConnectedBlocks() {
		if (connectedAxle != null) {
			return new BlockPos[] { pos.up(), pos.down(), connectedAxle };
		}
		return new BlockPos[] { pos.up(), pos.down() };
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound compound = new NBTTagCompound();
		writeToNBT(compound);
		return  new S35PacketUpdateTileEntity(getPos(), getBlockMetadata(), compound);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		NBTTagCompound compound = pkt.getNbtCompound();
		readFromNBT(compound);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		if (connectedAxle != null)
			compound.setLong("connectedAxle", connectedAxle.toLong());
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("connectedAxle")) {
			connectedAxle=BlockPos.fromLong(compound.getLong("connectedAxle"));
		}
	}
}
