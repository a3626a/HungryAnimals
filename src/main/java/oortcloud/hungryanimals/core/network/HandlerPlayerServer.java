package oortcloud.hungryanimals.core.network;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class HandlerPlayerServer implements
		IMessageHandler<PacketPlayerServer, PacketPlayerClient> {

	@Override
	public PacketPlayerClient onMessage(PacketPlayerServer message,
			MessageContext ctx) {

		switch (message.index) {
		
		case 0 :
			ItemStack stack = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(message.name).getHeldItem();
			if (stack != null) {
				NBTTagCompound tag = stack.getTagCompound();
				if (tag == null) {
					stack.setTagCompound(new NBTTagCompound());
					tag = stack.getTagCompound();
				}
				tag.setInteger("target", message.getInt());
			}
			break;
			
		}
		
		return null;
	}

}
