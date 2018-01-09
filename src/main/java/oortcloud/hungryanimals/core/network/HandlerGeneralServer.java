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
import oortcloud.hungryanimals.entities.handler.HungryAnimalManager;

public class HandlerGeneralServer implements IMessageHandler<PacketGeneralServer, PacketGeneralClient> {

	@Override
	public PacketGeneralClient onMessage(PacketGeneralServer message, MessageContext ctx) {
		FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
			switch (message.index) {
			case SyncIndex.HERBICIDE_SETBLOCK:
				/*
				 * int dim1 = message.getInt(); BlockPos pos1 = new
				 * BlockPos(message.getInt(), message.getInt(),
				 * message.getInt()); World world1 =
				 * MinecraftServer.getServer().worldServerForDimension(dim1);
				 * 
				 * if (world1.getBlockState(pos1).getBlock() == Blocks.grass) {
				 * // world1.setBlockState(pos1, 1, 2); } if
				 * (world1.getBlockState(pos1.up()).getBlock() ==
				 * Blocks.tallgrass) { world1.setBlockToAir(pos1.up()); }
				 */
				break;
			case SyncIndex.ENTITYOVERLAY_EDIT_DOUBLE:
				int id2 = message.getInt();
				WorldServer[] aworldserver2 = FMLCommonHandler.instance().getMinecraftServerInstance().worlds;
				for (int j = 0; j < aworldserver2.length; ++j) {
					WorldServer worldserver = aworldserver2[j];

					if (worldserver != null) {
						Entity entity = worldserver.getEntityByID(id2);
						if (entity != null && entity instanceof EntityAnimal) {
							EntityAnimal animal = (EntityAnimal) entity;

							if (!HungryAnimalManager.getInstance().isRegistered(animal.getClass()))
								break;

							switch (message.getString()) {
							case "nutrient":
								ICapabilityHungryAnimal capHungry1 = animal.getCapability(ProviderHungryAnimal.CAP, null);
								capHungry1.setNutrient(message.getDouble());
								break;
							case "stomach":
								ICapabilityHungryAnimal capHungry2 = animal.getCapability(ProviderHungryAnimal.CAP, null);
								capHungry2.setStomach(message.getDouble());
								break;
							case "weight":
								ICapabilityHungryAnimal capHungry3 = animal.getCapability(ProviderHungryAnimal.CAP, null);
								capHungry3.setWeight(message.getDouble());
								break;
							case "excretion":
								ICapabilityHungryAnimal capHungry4 = animal.getCapability(ProviderHungryAnimal.CAP, null);
								capHungry4.setExcretion(message.getDouble());
								break;
							case "taming":
								ICapabilityTamableAnimal capTaming = animal.getCapability(ProviderTamableAnimal.CAP, null);
								capTaming.setTaming(message.getDouble());
								break;
							}
						}
					}
				}
				break;
			case SyncIndex.ENTITYOVERLAY_EDIT_INT:
				int id3 = message.getInt();
				WorldServer[] aworldserver3 = FMLCommonHandler.instance().getMinecraftServerInstance().worlds;
				for (int j = 0; j < aworldserver3.length; ++j) {
					WorldServer worldserver = aworldserver3[j];

					if (worldserver != null) {
						Entity entity = worldserver.getEntityByID(id3);
						if (entity != null && entity instanceof EntityAnimal) {
							EntityAnimal animal = (EntityAnimal) entity;

							if (!HungryAnimalManager.getInstance().isRegistered(animal.getClass()))
								break;

							switch (message.getString()) {
							case "age":
								animal.setGrowingAge(message.getInt());
							}
						}
					}
				}

				break;
			case SyncIndex.ENTITYOVERLAY_SYNC_REQUEST:
				break;
			}
		});

		return null;
	}

}
