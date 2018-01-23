package oortcloud.hungryanimals.entities.production;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.network.PacketEntityClient;
import oortcloud.hungryanimals.core.network.SyncIndex;
import oortcloud.hungryanimals.entities.production.utils.IRange;

abstract public class ProductionInteraction implements IProductionInteraction, IProductionTickable, ISyncable {
	
	private int cooldown;
	private IRange delay;
	protected EntityAnimal animal;

	private boolean prevCanProduce;

	protected String name;

	public ProductionInteraction(String name, EntityAnimal animal, IRange delay) {
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
	public NBTBase writeNBT() {
		return new NBTTagInt(cooldown);
	}

	@Override
	public void readNBT(NBTBase nbt) {
		cooldown = ((NBTTagInt) nbt).getInt();
	}

	@Override
	public String getName() {
		return name;
	}

	public void sync() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			WorldServer world = (WorldServer) animal.getEntityWorld();
			for (EntityPlayer i : world.getEntityTracker().getTrackingPlayers(animal)) {
				PacketEntityClient packet = new PacketEntityClient(SyncIndex.PRODUCTION_SYNC, animal);
				packet.setString(getName());
				packet.setInt(cooldown);
				HungryAnimals.simpleChannel.sendTo(packet, (EntityPlayerMP) i);
			}
		}
	}

	public void syncTo(EntityPlayerMP target) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			PacketEntityClient packet = new PacketEntityClient(SyncIndex.PRODUCTION_SYNC, animal);
			packet.setString(getName());
			packet.setInt(cooldown);
			HungryAnimals.simpleChannel.sendTo(packet, target);
		}
	}

	@Override
	public void readFrom(PacketEntityClient message) {
		cooldown = message.getInt();
	}

	public int getCooldown() {
		return cooldown;
	}
}
