package oortcloud.hungryanimals.tileentities;

import oortcloud.hungryanimals.energy.PowerNetwork;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class TileEntityAxle extends TileEntityPowerTransporter {

	private BlockPos connectedAxle;

	public BlockPos getConnectedAxle() {
		return connectedAxle;
	}
	
	public void setConnectedAxle(BlockPos pos) {
		connectedAxle=pos;
		mergePowerNetwork(new PowerNetwork(0));
	}
	
	@Override
	public BlockPos[] getConnectedBlocks() {
		if (connectedAxle != null) {
			return new BlockPos[] { pos.up(), pos.down(), connectedAxle };
		}
		return new BlockPos[] { pos.up(), pos.down() };
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
		if (compound.hasKey("connectedAxle"))
			connectedAxle=BlockPos.fromLong(compound.getLong("connectedAxle"));
	}
}
