package oortcloud.hungryanimals.tileentities;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import oortcloud.hungryanimals.energy.PowerNetwork;

public interface IPowerTransporter {
	
	public PowerNetwork getPowerNetwork();
	public void setPowerNetwork(PowerNetwork powerNetwork);
	public void mergePowerNetwork(PowerNetwork powerNetwork);
	public double getPowerCapacity();
	public BlockPos[] getConnectedBlocks();
}
