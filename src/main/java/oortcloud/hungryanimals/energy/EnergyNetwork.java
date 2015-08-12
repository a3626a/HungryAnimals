package oortcloud.hungryanimals.energy;

import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.network.PacketTileEntityClient;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EnergyNetwork {

	private static float angularVelocityFactor = 3.0F;

	public final static double energyUnit = 10;
	private double energyStored;
	private double lastEnergyStored;
	private double energyCapacity;
	private long lastWorldtick;

	private float angle;
	private float lastAngle;
	
	@SideOnly(Side.CLIENT)
	private float angularVelocity;
	
	public EnergyNetwork() {
		this(energyUnit);
	}

	public EnergyNetwork(double energyCapacity) {
		this.energyStored = 0;
		this.energyCapacity = energyCapacity;
		this.angle = 0;
	}

	public double getCapacity() {
		return this.energyCapacity;
	}

	public void setCapacity(double energyCapacity) {
		this.energyCapacity = energyCapacity;
	}

	public double getEnergy() {
		return this.energyStored;
	}

	public void setEnergy(double energyStored) {
		this.energyStored = energyStored;
	}

	public void produceEnergy(double value) {
		energyStored = Math.min(energyStored + value, energyCapacity);
	}

	public double consumeEnergy(double value) {
		if (this.energyStored < value) {
			double temp = energyStored;
			energyStored = 0;

			return temp;
		} else {
			energyStored -= value;
			return value;
		}
	}

	public boolean isRotating() {
		return this.energyStored > 0;
	}

	public void update(World world, BlockPos pos) {
		
		if (world.isRemote) {
			if (world.getWorldTime() != this.lastWorldtick) {
				this.angle = (this.angle+this.angularVelocity)%360;
			}
			this.lastWorldtick = world.getWorldTime();
		} else {
			if (world.getWorldTime() != this.lastWorldtick) {
				float angularSpeed = (float) (angularVelocityFactor*energyStored/energyCapacity);
				this.angle = (this.angle+angularSpeed)%360;
				if (lastAngle != angle) {
					PacketTileEntityClient msg = new PacketTileEntityClient(1, world.provider.getDimensionId(), pos);
					msg.setFloat(angle);
					HungryAnimals.simpleChannel.sendToAll(msg);
				}
				if (lastEnergyStored != energyStored) {
					PacketTileEntityClient msg = new PacketTileEntityClient(6, world.provider.getDimensionId(), pos);
					msg.setFloat(angularSpeed);
					HungryAnimals.simpleChannel.sendToAll(msg);
				}
				this.lastEnergyStored = this.energyStored;
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
