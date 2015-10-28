package oortcloud.hungryanimals.core.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import oortcloud.hungryanimals.tileentities.TileEntityBlender;

public class HandlerTileEntityServer implements IMessageHandler<PacketTileEntityServer, PacketTileEntityClient> {

	@Override
	public PacketTileEntityClient onMessage(PacketTileEntityServer message, MessageContext ctx) {
		TileEntity te = MinecraftServer.getServer().worldServerForDimension(message.dim).getTileEntity(message.pos);
		switch (message.index) {
		}

		return null;
	}

}
