package oortcloud.hungryanimals.core.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.network.NetworkEvent;
import oortcloud.hungryanimals.client.ParticleMilk;
import oortcloud.hungryanimals.items.ModItems;

import java.util.function.Supplier;

public class PacketClientSpawnParticle {
	
	public Vec3d pos;
	
	public PacketClientSpawnParticle(Vec3d pos) {
		this.pos = pos;
	}
	public PacketClientSpawnParticle(PacketBuffer buf) {
		this(new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble()));
	}

	public void toBytes(ByteBuf buf) {
		buf.writeDouble(pos.x);
		buf.writeDouble(pos.y);
		buf.writeDouble(pos.z);
	}

	public void onMessage(Supplier<NetworkEvent.Context> context) {
		context.get().enqueueWork(
			() -> {
				double x = pos.x;
				double y = pos.y;
				double z = pos.z;

				for (int i = 0; i < 10; i++) {
					double v = Math.random() * 0.1 + 0.03;
					double r = Math.random() * 2 * Math.PI;

					Minecraft.getInstance().world.addParticle(new ParticleMilk(Minecraft.getInstance().world, x, y, z, v * Math.cos(r), 0, v * Math.sin(r)));
				}
			}
		);
	}
}
