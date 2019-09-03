package oortcloud.hungryanimals.entities.capability;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.network.PacketClientSyncTamable;

public class CapabilityTamableAnimal implements ICapabilityTamableAnimal {

	private double taming;
	private TamingLevel prevLevel;
	protected EntityLiving entity;

	public CapabilityTamableAnimal() {}
	
	public CapabilityTamableAnimal(EntityLiving entity) {
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
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			WorldServer world = (WorldServer) entity.getEntityWorld();
			for (EntityPlayer i : world.getEntityTracker().getTrackingPlayers(entity)) {
				PacketClientSyncTamable packet = new PacketClientSyncTamable(entity, getTaming());
				HungryAnimals.simpleChannel.sendTo(packet, (EntityPlayerMP) i);
			}
		}
	}

	public void syncTo(EntityPlayerMP target) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			PacketClientSyncTamable packet = new PacketClientSyncTamable(entity, getTaming());
			HungryAnimals.simpleChannel.sendTo(packet, target);
		}
	}
	
}
