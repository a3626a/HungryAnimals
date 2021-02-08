package oortcloud.hungryanimals.core.network;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import oortcloud.hungryanimals.entities.capability.*;

import java.util.function.Supplier;

public class PacketServerDGEditDouble extends PacketServerDGEdit {

	public double value;
	
	public PacketServerDGEditDouble(int entityId, String target, double value) {
		super(entityId, target);
		this.value = value;
	}

	public PacketServerDGEditDouble(PacketBuffer buf) {
		super(buf);
		this.value = buf.readDouble();
	}

	public void toBytes(PacketBuffer buf) {
		super.toBytes(buf);
		buf.writeDouble(value);
	}

	public void onMessage(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(
			() -> {
				int id2 = entityId;
				ServerPlayerEntity serverPlayerEntity = context.get().getSender();
				if (serverPlayerEntity == null)
					return;
				ServerWorld worldServer = serverPlayerEntity.getServerWorld();
				Entity entity = worldServer.getEntityByID(id2);
				if (entity == null)
					return;
				switch (target) {
					case "nutrient":
						entity.getCapability(ProviderHungryAnimal.CAP).ifPresent(
								cap -> cap.setNutrient(value)
						);
						break;
					case "stomach":
						entity.getCapability(ProviderHungryAnimal.CAP).ifPresent(
								cap -> cap.setStomach(value)
						);
						break;
					case "weight":
						entity.getCapability(ProviderHungryAnimal.CAP).ifPresent(
								cap -> cap.setWeight(value)
						);
						break;
					case "excretion":
						entity.getCapability(ProviderHungryAnimal.CAP).ifPresent(
								cap -> cap.setExcretion(value)
						);
						break;
					case "taming":
						entity.getCapability(ProviderTamableAnimal.CAP).ifPresent(
								cap -> cap.setTaming(value)
						);
						break;
				}
			}
		);
		context.get().setPacketHandled(true);
	}
}
