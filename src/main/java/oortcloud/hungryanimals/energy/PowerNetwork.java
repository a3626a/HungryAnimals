package oortcloud.hungryanimals.energy;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.network.PacketTileEntityClient;

public class PowerNetwork {

	private static float angularVelocityFactor = 3.0F;

	public final static double powerUnit = 10;
	private double powerStored;
	private double lastPowerStored;
	private double powerCapacity;
	private long lastWorldtick;

	private float angle;
	private float lastAngle;
	
	@SideOnly(Side.CLIENT)
	private float angularVelocity;
	
	public PowerNetwork() {
		this(powerUnit);
	}

	public PowerNetwork(double energyCapacity) {
		this.powerStored = 0;
		this.powerCapacity = energyCapacity;
		this.angle = 0;
	}

	public double getPowerCapacity() {
		return this.powerCapacity;
	}

	public void setPowerCapacity(double energyCapacity) {
		this.powerCapacity = energyCapacity;
	}

	public double getPowerStored() {
		return this.powerStored;
	}

	public void setPowerStored(double powerStored) {
		this.powerStored = powerStored;
	}

	public void producePower(double powerProduced) {
		powerStored = Math.min(powerStored + powerProduced, powerCapacity);
	}

	public double consumeEnergy(double powerConsumed) {
		if (this.powerStored < powerConsumed) {
			double temp = powerStored;
			powerStored = 0;

			return temp;
		} else {
			powerStored -= powerConsumed;
			return powerConsumed;
		}
	}

	public boolean isRotating() {
		return this.powerStored > 0;
	}

	public void update(World world, BlockPos pos) {
		if (world.isRemote) {
			if (world.getWorldTime() != this.lastWorldtick) {
				this.angle = (this.angle+this.angularVelocity)%360;
			}
			this.lastWorldtick = world.getWorldTime();
		} else {
			if (world.getWorldTime() != this.lastWorldtick) {
				float angularSpeed = (float) (angularVelocityFactor*powerStored/powerCapacity);
				this.angle = (this.angle+angularSpeed)%360;
				if (lastAngle != angle) {
					PacketTileEntityClient msg = new PacketTileEntityClient(1, world.provider.getDimensionId(), pos);
					msg.setFloat(angle);
					HungryAnimals.simpleChannel.sendToAll(msg);
				}
				if (lastPowerStored != powerStored) {
					PacketTileEntityClient msg = new PacketTileEntityClient(6, world.provider.getDimensionId(), pos);
					msg.setFloat(angularSpeed);
					HungryAnimals.simpleChannel.sendToAll(msg);
				}
				this.lastPowerStored = this.powerStored;
				this.lastAngle = this.angle;
				this.lastWorldtick = world.getWorldTime();
			}
		}
	}

	public float getAngle(float partialtick) {
		return (float) (angle + angularVelocity * partialtick);
	}

	public void setAngularVelocity(float angularVelocity) {
		this.angularVelocity = angularVelocity;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}
	
	/*
	 * public static EnergyNetwork mergeNetwork(EnergyNetwork n1, EnergyNetwork
	 * n2) { EnergyNetwork ret = new EnergyNetwork(n1.energyCapacity +
	 * n2.energyCapacity); ret.energyStored = n1.energyStored + n2.energyStored;
	 * return ret; }
	 */
}
