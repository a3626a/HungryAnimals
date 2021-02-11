package oortcloud.hungryanimals.entities.capability;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.network.ModPacketHandler;
import oortcloud.hungryanimals.core.network.PacketClientSyncTamable;

public class CapabilityTamableAnimal implements ICapabilityTamableAnimal {

	private double taming;
	private TamingLevel prevLevel;
	protected MobEntity entity;

	public CapabilityTamableAnimal() {}
	
	public CapabilityTamableAnimal(MobEntity entity) {
		this.entity = entity;
		setTaming(-2);
		this.prevLevel = TamingLevel.WILD;
	}

	@Override
	public double getTaming() {
		return taming;
	}

	@Override
	public TamingLevel getTamingLevel() {
		return TamingLevel.fromTaming(getTaming());
	}
	
	@Override
	public double setTaming(double taming) {
		double old = this.taming;
		if (taming > 2) {
			this.taming = 2;
		} else if (taming < -2) {
			this.taming = -2;
		} else {
			this.taming = taming;
		}
		TamingLevel currLevel = TamingLevel.fromTaming(this.taming);
		if (currLevel != prevLevel) {
			sync();
		}
		prevLevel = currLevel;
		return old;
	}

	@Override
	public double addTaming(double taming) {
		return setTaming(getTaming() + taming);
	}

	public void sync() {
		if (!entity.getEntityWorld().isRemote) {
			ModPacketHandler.INSTANCE.send(
					PacketDistributor.TRACKING_ENTITY.with(() -> entity),
					new PacketClientSyncTamable(entity.getEntityId(), getTaming())
			);
		}
	}

	public void syncTo(ServerPlayerEntity target) {
		if (!entity.getEntityWorld().isRemote) {
			ModPacketHandler.INSTANCE.send(
					PacketDistributor.PLAYER.with(()->target),
					new PacketClientSyncTamable(entity.getEntityId(), getTaming())
			);
		}
	}
	
}
