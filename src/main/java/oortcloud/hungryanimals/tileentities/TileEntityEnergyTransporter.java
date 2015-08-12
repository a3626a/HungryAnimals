package oortcloud.hungryanimals.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.blocks.BlockEnergyTransporter;
import oortcloud.hungryanimals.core.network.PacketTileEntityClient;
import oortcloud.hungryanimals.energy.EnergyNetwork;

public class TileEntityEnergyTransporter extends TileEntity implements IUpdatePlayerListBox {
	protected EnergyNetwork network;
	public boolean isInitialized;
	private double energyCapacity;
	
	public TileEntityEnergyTransporter() {
		energyCapacity = EnergyNetwork.energyUnit;
		network = new EnergyNetwork();
	}
	
	public TileEntityEnergyTransporter(double energyCapacity) {
		this.energyCapacity = energyCapacity;
		network = new EnergyNetwork(energyCapacity);
	}
	
	public double getEnergyCapacity() {
		return energyCapacity;
	}
	
	public EnergyNetwork getNetwork() {
		return network;
	}

	public void setNetwork(EnergyNetwork net) {
		network = net;
	}

	@Override
	public void update() {

		if (!isInitialized) {
			((BlockEnergyTransporter) this.getBlockType()).setNetwork(this.worldObj, this.pos, new EnergyNetwork(0));
		}

		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			EnergyNetwork network = getNetwork();

			double energyPortion = network.getEnergy() / network.getCapacity() * EnergyNetwork.energyUnit;
			network.consumeEnergy(Math.max(energyPortion * 0.01, 0.01));
		}

		network.update(worldObj, getPos());
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setDouble("energyStored", network.getEnergy() / network.getCapacity() * EnergyNetwork.energyUnit);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.network.setEnergy(compound.getDouble("energyStored"));
	}

}
