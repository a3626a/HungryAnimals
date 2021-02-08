package oortcloud.hungryanimals.entities.production;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.network.PacketClientSyncProducing;
import oortcloud.hungryanimals.core.network.PacketClientSyncProducingInteraction;
import oortcloud.hungryanimals.entities.production.utils.IRange;

abstract public class ProductionInteraction implements IProductionInteraction, IProductionTickable, ISyncable, IProductionTOP {
	
	private int cooldown;
	private IRange delay;
	protected MobEntity animal;

	private boolean prevCanProduce;

	protected String name;

	public ProductionInteraction(String name, MobEntity animal, IRange delay) {
		this.name = name;
		this.animal = animal;
		this.delay = delay;
		resetCooldown();
	}

	@Override
	public void update() {
		cooldown--;
		boolean currCanProduce = canProduce();
		if (currCanProduce != prevCanProduce) {
			sync();
		}
		prevCanProduce = currCanProduce;
	}

	protected boolean canProduce() {
		return cooldown <= 0;
	}

	protected void resetCooldown() {
		cooldown = delay.get(animal);
	}

	@Override
	public INBT writeNBT() {
		return new NBTTagInt(cooldown);
	}

	@Override
	public void readNBT(INBT nbt) {
		cooldown = ((NBTTagInt) nbt).getInt();
	}

	@Override
	public String getName() {
		return name;
	}

	public void sync() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			WorldServer world = (WorldServer) animal.getEntityWorld();
			for (PlayerEntity i : world.getEntityTracker().getTrackingPlayers(animal)) {
				PacketClientSyncProducingInteraction packet = new PacketClientSyncProducingInteraction(animal, getName(), cooldown);
				HungryAnimals.simpleChannel.sendTo(packet, (ServerPlayerEntity) i);
			}
		}
	}

	public void syncTo(ServerPlayerEntity target) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			PacketClientSyncProducingInteraction packet = new PacketClientSyncProducingInteraction(animal, getName(), cooldown);
			HungryAnimals.simpleChannel.sendTo(packet, target);
		}
	}

	@Override
	public void readFrom(PacketClientSyncProducing message) {
		cooldown = ((PacketClientSyncProducingInteraction)message).cooldown;
	}

	public int getCooldown() {
		return cooldown;
	}
	
	@Override
	public String getMessage() {
		if (cooldown < 0) {
			return String.format("%s now", name);
		}
		return String.format("%s after %d seconds", name, cooldown);
	}
}
