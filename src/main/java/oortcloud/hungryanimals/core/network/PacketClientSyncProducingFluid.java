package oortcloud.hungryanimals.core.network;

import javax.annotation.Nonnull;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketClientSyncProducingFluid extends PacketClientSyncProducing {
	
	@Nonnull
	public FluidTank tank;
	
	public PacketClientSyncProducingFluid() {
		this(null, "", new FluidTank(0));
	}

	public PacketClientSyncProducingFluid(Entity entity, String name, @Nonnull FluidTank tank) {
		super(entity, name);
		this.tank = tank;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		super.fromBytes(buf);
		tank.readFromNBT(ByteBufUtils.readTag(buf));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		super.toBytes(buf);
		ByteBufUtils.writeTag(buf, tank.writeToNBT(new CompoundNBT()));
	}
}
