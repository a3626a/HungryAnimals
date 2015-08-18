package oortcloud.hungryanimals.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import oortcloud.hungryanimals.energy.PowerNetwork;

public class TileEntityPowerTransporter extends TileEntity implements IUpdatePlayerListBox, IPowerTransporter {
	
	protected PowerNetwork powerNetwork;
	protected double powerCapacity;
	private boolean isInitialized=false;
	
	public TileEntityPowerTransporter() {
		powerCapacity = PowerNetwork.powerUnit;
		powerNetwork = new PowerNetwork();
	}

	public void setNetwork(PowerNetwork net) {
		powerNetwork = net;
	}

	@Override
	public void mergePowerNetwork(PowerNetwork powerNetwork) {
		if (powerNetwork != this.powerNetwork) {
			double containedPower = this.powerNetwork.getPowerStored()/this.powerNetwork.getPowerCapacity()*this.powerNetwork.getPowerCapacity();
			this.powerNetwork.setPowerStored(this.powerNetwork.getPowerStored()-containedPower);
			this.powerNetwork.setPowerCapacity(this.powerNetwork.getPowerCapacity()-this.powerNetwork.getPowerCapacity());
			powerNetwork.setPowerCapacity(powerNetwork.getPowerCapacity()+this.powerNetwork.getPowerCapacity());
			powerNetwork.setPowerStored(powerNetwork.getPowerStored()+containedPower);;
			
			this.setPowerNetwork(powerNetwork);
		}
		
		for (BlockPos i : getConnectedBlocks(getWorld(), pos)) {
			TileEntity tileEntity = getWorld().getTileEntity(pos);
			if (tileEntity!=null&&!tileEntity.isInvalid()&&tileEntity instanceof IPowerTransporter) {
				IPowerTransporter nextPowerTransporter = (IPowerTransporter)tileEntity;
				nextPowerTransporter.mergePowerNetwork(powerNetwork);
			}
		}
	}
	@Override
	public void validate() {
		this.mergePowerNetwork(new PowerNetwork(0));
		super.validate();
	}
	
	@Override
	public void invalidate() {
		super.invalidate();
		for (BlockPos i : getConnectedBlocks(getWorld(), pos)) {
			TileEntity tileEntity = getWorld().getTileEntity(pos);
			if (tileEntity!=null&&!tileEntity.isInvalid()&&tileEntity instanceof IPowerTransporter) {
				IPowerTransporter powerTransporter = (IPowerTransporter)tileEntity;
				powerTransporter.mergePowerNetwork(new PowerNetwork(0));
			}
		}
	}
	
	@Override
	public void update() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			PowerNetwork network = getPowerNetwork();

			double energyPortion = network.getPowerStored() / network.getPowerCapacity() * getPowerCapacity();
			network.consumeEnergy(Math.max(energyPortion * 0.01, 0.01));
		}

		powerNetwork.update(worldObj, getPos());
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setDouble("energyStored", powerNetwork.getPowerStored() / powerNetwork.getPowerCapacity() * getPowerCapacity());
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.powerNetwork.setPowerStored(compound.getDouble("energyStored"));
	}

	@Override
	public PowerNetwork getPowerNetwork() {
		return powerNetwork;
	}

	@Override
	public void setPowerNetwork(PowerNetwork powerNetwork) {
		this.powerNetwork=powerNetwork;
	}

	@Override
	public BlockPos[] getConnectedBlocks(World world, BlockPos pos) {
		return new BlockPos[] {};
	}

	@Override
	public double getPowerCapacity() {
		return powerCapacity;
	}

}
