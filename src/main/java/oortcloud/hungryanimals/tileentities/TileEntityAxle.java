package oortcloud.hungryanimals.tileentities;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class TileEntityAxle extends TileEntityPowerTransporter {
	
	private BlockPos connectedAxle;
	
	@Override
	public BlockPos[] getConnectedBlocks() {
		return new BlockPos[] {pos.up(), pos.down()};
	}
}
