package oortcloud.hungryanimals.core.network;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ICapabilityTamableAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderTamableAnimal;
import oortcloud.hungryanimals.entities.properties.handler.HungryAnimalManager;

public class HandlerGeneralServer implements IMessageHandler<PacketGeneralServer, PacketGeneralClient> {

	@Override
	public PacketGeneralClient onMessage(PacketGeneralServer message, MessageContext ctx) {

		switch (message.index) {
		case SyncIndex.HERBICIDE_SETBLOCK:
			/*
			 * int dim1 = message.getInt(); BlockPos pos1 = new
			 * BlockPos(message.getInt(), message.getInt(), message.getInt());
			 * World world1 =
			 * MinecraftServer.getServer().worldServerForDimension(dim1);
			 * 
			 * if (world1.getBlockState(pos1).getBlock() == Blocks.grass) { //
			 * world1.setBlockState(pos1, 1, 2); } if
			 * (world1.getBlockState(pos1.up()).getBlock() == Blocks.tallgrass)
			 * { world1.setBlockToAir(pos1.up()); }
			 */
			break;
		case SyncIndex.ENTITYOVERLAY_EDIT_DOUBLE:
			int id2 = message.getInt();
			WorldServer[] aworldserver2 = FMLCommonHandler.instance().getMinecraftServerInstance().worldServers;
			for (int j = 0; j < aworldserver2.length; ++j) {
				WorldServer worldserver = aworldserver2[j];

				if (worldserver != null) {
					Entity entity = worldserver.getEntityByID(id2);
					if (entity != null && entity instanceof EntityAnimal) {
						EntityAnimal animal = (EntityAnimal) entity;
						
						if (!HungryAnimalManager.getInstance().isRegistered(animal.getClass()))
							break;
						
						switch(message.getString()) {
						case "hunger" :
							ICapabilityHungryAnimal capHungry = animal.getCapability(ProviderHungryAnimal.CAP, null);
							capHungry.setHunger(message.getDouble());
							break;
						case "taming" :
							ICapabilityTamableAnimal capTaming = animal.getCapability(ProviderTamableAnimal.CAP, null);
							capTaming.setTaming(message.getDouble());
							break;
						}
					}
				}
			}

			break;
		case SyncIndex.ENTITYOVERLAY_SYNC_REQUEST:
			break;
		}

		return null;
	}

}
