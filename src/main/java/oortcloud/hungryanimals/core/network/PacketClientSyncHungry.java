package oortcloud.hungryanimals.core.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;

import java.util.function.Supplier;

public class PacketClientSyncHungry extends PacketClientEntity {
	public double stomach;
	public double weight;
	
	public PacketClientSyncHungry(int entityId, double stomach, double weight) {
		super(entityId);
		this.stomach = stomach;
		this.weight = weight;
	}

	public PacketClientSyncHungry(PacketBuffer buf) {
		super(buf);
		this.stomach = buf.readDouble();
		this.weight = buf.readDouble();
	}

	@Override
	public void toBytes(PacketBuffer buf) {
		super.toBytes(buf);
		buf.writeDouble(stomach);
		buf.writeDouble(weight);
	}

	public void onMessage(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(
				() -> {
					Entity entity = Minecraft.getInstance().world.getEntityByID(entityId);
					if (entity == null)
						return;

					entity.getCapability(ProviderHungryAnimal.CAP).ifPresent(
							cap -> {
								cap.setStomach(stomach);
								cap.setWeight(weight);
//								if (entity instanceof AgeableEntity) {
//									// TODO : Expand this function to MobEntity, currently setScale is required.
//									float ratio = (float) RenderEntityWeight.getRatio(entity);
//
//									if (((AgeableEntity) entity).getGrowingAge() < 0) {
//										ratio *= 0.5;
//									}
//
//									try {
//										setScale.invoke(entity, ratio);
//									} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//										HungryAnimals.logger.warn(
//												"Problem occured during change entity bounding box. Please report to mod author(oortcloud).");
//										e.printStackTrace();
//									}
//								}
							}
					);
				}
		);
		context.get().setPacketHandled(true);
	}
}
