package oortcloud.hungryanimals.core.network;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.entities.capability.ICapabilityHungryAnimal;
import oortcloud.hungryanimals.entities.capability.ProviderHungryAnimal;
import oortcloud.hungryanimals.entities.render.RenderEntityWeight;

public class HandlerClientSyncHungry implements IMessageHandler<PacketClientSyncHungry, IMessage> {

	public static Method setScale = ReflectionHelper.findMethod(EntityAgeable.class, "setScale", "func_98055_j",
			float.class);

	@Override
	public IMessage onMessage(PacketClientSyncHungry message, MessageContext ctx) {
		Minecraft.getMinecraft().addScheduledTask(() -> {
			World world = FMLClientHandler.instance().getClient().world;
			if (world == null)
				return;

			Entity entity = world.getEntityByID(message.id);
			if (entity == null || !(entity instanceof EntityLiving))
				return;

			ICapabilityHungryAnimal cap2 = entity.getCapability(ProviderHungryAnimal.CAP, null);
			if (cap2 != null) {
				cap2.setStomach(message.stomach);
				cap2.setWeight(message.weight);

				if (entity instanceof EntityAgeable) {
					// TODO : Expand this function to EntityLiving, currently setScale is required.
					float ratio = (float) RenderEntityWeight.getRatio(entity);

					if (((EntityAgeable) entity).getGrowingAge() < 0) {
						ratio *= 0.5;
					}

					try {
						setScale.invoke(entity, ratio);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						HungryAnimals.logger.warn(
								"Problem occured during change entity bounding box. Please report to mod author(oortcloud).");
						e.printStackTrace();
					}
				}
			}
		});
		return null;
	}

}
