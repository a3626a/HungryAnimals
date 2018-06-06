package oortcloud.hungryanimals.entities.production;

import net.minecraft.entity.player.EntityPlayerMP;
import oortcloud.hungryanimals.core.network.PacketClientSyncProducing;

public interface ISyncable {
	public void syncTo(EntityPlayerMP target);
	public void readFrom(PacketClientSyncProducing message);
}
