package oortcloud.hungryanimals.entities.production;

import net.minecraft.entity.player.ServerPlayerEntity;
import oortcloud.hungryanimals.core.network.PacketClientSyncProducing;

public interface ISyncable {
	public void syncTo(ServerPlayerEntity target);
	public void readFrom(PacketClientSyncProducing message);
}
