package oortcloud.hungryanimals.core.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import oortcloud.hungryanimals.items.ModItems;

import java.util.function.Supplier;

public class PacketServerDGSet {
	public int entityId;

	public PacketServerDGSet(int entityId) {
		this.entityId = entityId;
	}

	public PacketServerDGSet(PacketBuffer buf) {
		this.entityId = buf.readInt();
	}

	public void toBytes(PacketBuffer buf) {
		buf.writeInt(entityId);
	}

	public void onMessage(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(
			() -> {
				ServerPlayerEntity serverPlayerEntity = context.get().getSender();
				if (serverPlayerEntity == null)
					return;

				ItemStack stack = serverPlayerEntity.getHeldItemMainhand();
				if (!stack.isEmpty() && stack.getItem() == ModItems.DEBUG_GLASS.get()) {
					CompoundNBT tag = stack.getTag();
					if (tag == null) {
						tag = new CompoundNBT();
						stack.setTag(tag);
					}
					tag.putInt("target", entityId);
				}
			}
		);
		context.get().setPacketHandled(true);
	}
}
