package oortcloud.hungryanimals.core.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import oortcloud.hungryanimals.items.ModItems;

public class HandlerPlayerServer implements IMessageHandler<PacketPlayerServer, PacketPlayerClient> {

	@Override
	public PacketPlayerClient onMessage(PacketPlayerServer message, MessageContext ctx) {
		
		EntityPlayer player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(message.name);
		ItemStack stack = player.getHeldItemMainhand();
		switch (message.index) {
		case SyncIndex.DEBUG_SETTARGET:
			if (stack != null && stack.getItem() == ModItems.debugGlass) {
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
