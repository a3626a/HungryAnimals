package oortcloud.hungryanimals.tileentities;

import oortcloud.hungryanimals.blocks.BlockAxle;
import oortcloud.hungryanimals.blocks.ModBlocks;
import oortcloud.hungryanimals.energy.PowerNetwork;
import oortcloud.hungryanimals.items.ItemBelt;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.AxisAlignedBB;
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
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		if (isConnected()) {
			return super.getRenderBoundingBox().expand(getBeltLength(), 0, getBeltLength());
		}
		return super.getRenderBoundingBox();
	}
	
	public int getBeltLength() {
		double dist = pos.distanceSq(this.getConnectedAxle());
		int requiredBelt = (int) (Math.ceil(Math.sqrt(dist)));
		return requiredBelt;
	}
	
	public boolean isConnected() {
		if (this.getConnectedAxle() == null) {
			return false;
		} else {
			if (!isValidAxle(worldObj, this.getConnectedAxle())) {
				return false;
			} else {
				TileEntityAxle axleConnected = (TileEntityAxle) worldObj.getTileEntity(this.getConnectedAxle());
				if (axleConnected == null) {
					return false;
				} else {
					if (axleConnected.getConnectedAxle() == null) {
						return false;
					} else {
						return this.getPos().equals(axleConnected.getConnectedAxle());
					}
				}
			}
		}
	}
	
	public static boolean isValidAxle(World worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos) == ModBlocks.axle.getDefaultState().withProperty(BlockAxle.VARIANT, true);
	}
	
}
