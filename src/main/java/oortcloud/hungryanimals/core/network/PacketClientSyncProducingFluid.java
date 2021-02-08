package oortcloud.hungryanimals.core.network;

import javax.annotation.Nonnull;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.network.NetworkEvent;
import oortcloud.hungryanimals.entities.capability.CapabilityProducingAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderProducingAnimal;

import java.util.function.Supplier;

public class PacketClientSyncProducingFluid extends PacketClientSyncProducing {
	
	@Nonnull
	public FluidTank tank;

	public PacketClientSyncProducingFluid(int entityId, String name, @Nonnull FluidTank tank) {
		super(entityId, name);
		this.tank = tank;
	}
	
	public PacketClientSyncProducingFluid(PacketBuffer buf) {
		super(buf);
		tank.readFromNBT(buf.readCompoundTag());
	}

	@Override
	public void toBytes(PacketBuffer buf) {
		super.toBytes(buf);
		buf.writeCompoundTag(tank.writeToNBT(new CompoundNBT()));
	}

	public void onMessage(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(
				() -> {
					Entity entity = Minecraft.getInstance().world.getEntityByID(entityId);
					if (entity == null)
						return;

					entity.getCapability(ProviderProducingAnimal.CAP).ifPresent(
							cap -> {
								if (cap instanceof CapabilityProducingAnimal) {
									((CapabilityProducingAnimal) cap).readFrom(this);
								}
							}
					);
				}
		);
		context.get().setPacketHandled(true);
	}
}
