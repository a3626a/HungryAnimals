package oortcloud.hungryanimals.core.network;

import java.lang.reflect.Field;
import java.util.Iterator;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import oortcloud.hungryanimals.blocks.BlockPoppy;
import oortcloud.hungryanimals.blocks.ModBlocks;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.entities.properties.ExtendedPropertiesHungryAnimal;

public class HandlerGeneralServer implements IMessageHandler<PacketGeneralServer, PacketGeneralClient> {

	@Override
	public PacketGeneralClient onMessage(PacketGeneralServer message, MessageContext ctx) {

		switch (message.index) {
		case 0:
			int dim1 = message.getInt();
			BlockPos pos1 = new BlockPos(message.getInt(), message.getInt(), message.getInt());
			World world1 = MinecraftServer.getServer().worldServerForDimension(dim1);

			if (world1.getBlockState(pos1).getBlock() == Blocks.grass) {
				// world1.setBlockState(pos1, 1, 2);
			}
			if (world1.getBlockState(pos1.up()).getBlock() == Blocks.tallgrass) {
				world1.setBlockToAir(pos1.up());
			}
			break;
		case 1:
			int dim2 = message.getInt();
			BlockPos pos2 = new BlockPos(message.getInt(), message.getInt(), message.getInt());
			String name = message.getString();
			World world2 = MinecraftServer.getServer().worldServerForDimension(dim2);

			EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(name);
			ItemStack item = player.getHeldItem();

			boolean flag1 = item != null && item.getItem() == ItemBlock.getItemFromBlock(Blocks.red_flower);
			boolean flag2 = world2.getBlockState(pos2.down()).getBlock() == Blocks.farmland;
			boolean flag3 = world2.isAirBlock(pos2);

			world2.setBlockState(pos2, ModBlocks.poppy.getDefaultState().withProperty(BlockPoppy.AGE, 4), 2);
			item.stackSize--;
			if (item.stackSize == 0) {
				player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
			}
			break;
		case 2:
			int id = message.getInt();
			WorldServer[] aworldserver = MinecraftServer.getServer().worldServers;
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
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SecurityException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}
				}
			}
			break;
		case 3:
			int id2 = message.getInt();
			WorldServer[] aworldserver2 = MinecraftServer.getServer().worldServers;
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
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SecurityException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}
				}
			}
			break;
		case 4:
			int id3 = message.getInt();
			WorldServer[] aworldserver3 = MinecraftServer.getServer().worldServers;
			for (int j = 0; j < aworldserver3.length; ++j) {
				WorldServer worldserver = aworldserver3[j];

				if (worldserver != null) {
					Entity entity = worldserver.getEntityByID(id3);
					if (entity != null && entity instanceof EntityAnimal) {
						EntityAnimal animal = (EntityAnimal) entity;
						ExtendedPropertiesHungryAnimal properties = (ExtendedPropertiesHungryAnimal) animal.getExtendedProperties(Strings.extendedPropertiesKey);
						
						PacketGeneralClient msg = new PacketGeneralClient(0);
						msg.setDouble(properties.getHungry());
						msg.setDouble(animal.getHealth()/animal.getMaxHealth());
						msg.setDouble(animal.getGrowingAge() / 24000.0);
						msg.setDouble(properties.taming / 2.0);
						int[] potions = new int[animal.getActivePotionEffects().size()];
						Iterator iterator = animal.getActivePotionEffects().iterator();
						int potions_index = 0;
						while (iterator.hasNext()) {
							PotionEffect i = (PotionEffect) iterator.next();
							potions[potions_index++]=i.getPotionID();
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
