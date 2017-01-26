package oortcloud.hungryanimals.core.network;

import java.lang.reflect.Field;
import java.util.Iterator;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ICapabilityTamableAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderTamableAnimal;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryAnimal;

public class HandlerGeneralServer implements IMessageHandler<PacketGeneralServer, PacketGeneralClient> {

	@Override
	public PacketGeneralClient onMessage(PacketGeneralServer message, MessageContext ctx) {

		switch (message.index) {
		case SyncIndex.HERBICIDE_SETBLOCK:
			/*
			int dim1 = message.getInt();
			BlockPos pos1 = new BlockPos(message.getInt(), message.getInt(), message.getInt());
			World world1 = MinecraftServer.getServer().worldServerForDimension(dim1);

			if (world1.getBlockState(pos1).getBlock() == Blocks.grass) {
				// world1.setBlockState(pos1, 1, 2);
			}
			if (world1.getBlockState(pos1.up()).getBlock() == Blocks.tallgrass) {
				world1.setBlockToAir(pos1.up());
			}
			*/
			break;
		case SyncIndex.ENTITYOVERLAY_EDIT_INT:
			int id = message.getInt();
			WorldServer[] aworldserver = FMLCommonHandler.instance().getMinecraftServerInstance().worldServers;
			for (int j = 0; j < aworldserver.length; ++j) {
				WorldServer worldserver = aworldserver[j];
				if (worldserver != null) {
					Entity entity = worldserver.getEntityByID(id);
					if (entity != null && entity instanceof EntityAnimal) {
						EntityAnimal animal = (EntityAnimal) entity;
						ExtendedPropertiesHungryAnimal properties = (ExtendedPropertiesHungryAnimal) animal.getExtendedProperties(Strings.extendedPropertiesKey);
						if (properties != null) {
							try {
								Field f = properties.getClass().getField(message.getString());
								f.setInt(properties, message.getInt());
							} catch (NoSuchFieldException e) {
								e.printStackTrace();
							} catch (SecurityException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							}

						}
					}
				}
			}
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
						ExtendedPropertiesHungryAnimal properties = (ExtendedPropertiesHungryAnimal) animal.getExtendedProperties(Strings.extendedPropertiesKey);
						if (properties != null) {
							try {
								Field f = properties.getClass().getField(message.getString());
								f.setDouble(properties, message.getDouble());
							} catch (NoSuchFieldException e) {
								e.printStackTrace();
							} catch (SecurityException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							}

						}
					}
				}
			}
			break;
		case SyncIndex.ENTITYOVERLAY_SYNC_REQUEST:
			int id3 = message.getInt();
			WorldServer[] aworldserver3 = FMLCommonHandler.instance().getMinecraftServerInstance().worldServers;
			for (int j = 0; j < aworldserver3.length; ++j) {
				WorldServer worldserver = aworldserver3[j];

				if (worldserver != null) {
					Entity entity = worldserver.getEntityByID(id3);
					if (entity != null && entity instanceof EntityAnimal) {
						EntityAnimal animal = (EntityAnimal) entity;
						ICapabilityHungryAnimal capHungryAnimal = animal.getCapability(ProviderHungryAnimal.CAP, null);
						ICapabilityTamableAnimal capTamableAnimal = animal.getCapability(ProviderTamableAnimal.CAP, null);
						
						PacketGeneralClient msg = new PacketGeneralClient(SyncIndex.ENTITYOVERLAY_SYNC);
						msg.setDouble(capHungryAnimal.getHunger() / capHungryAnimal.getMaxHunger());
						msg.setDouble(animal.getHealth()/animal.getMaxHealth());
						msg.setDouble(animal.getGrowingAge() / 24000.0);
						msg.setDouble(capTamableAnimal.getTaming() / 2.0);
						int[] potions = new int[animal.getActivePotionEffects().size()];
						Iterator iterator = animal.getActivePotionEffects().iterator();
						int potions_index = 0;
						while (iterator.hasNext()) {
							PotionEffect i = (PotionEffect) iterator.next();
							potions[potions_index++]=Potion.getIdFromPotion(i.getPotion());
						}
						msg.setIntArray(potions);
						return msg;
					}
				}
			}
			break;
		}

		return null;
	}

}
