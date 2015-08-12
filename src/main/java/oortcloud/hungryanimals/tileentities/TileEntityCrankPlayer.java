package oortcloud.hungryanimals.tileentities;

import oortcloud.hungryanimals.energy.EnergyNetwork;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class TileEntityCrankPlayer extends TileEntityEnergyTransporter
		implements IUpdatePlayerListBox {
	public int leftTick;
	public static double energyProduction = 1;

	private static double energyCapacity = EnergyNetwork.energyUnit * 3;

	public TileEntityCrankPlayer() {
		super(energyCapacity);
	}
	
	@Override
	public void update() {
		super.update();
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			if (leftTick > 0) {
				this.getNetwork().produceEnergy(energyProduction);
				leftTick--;
			}
		}

	}

}
