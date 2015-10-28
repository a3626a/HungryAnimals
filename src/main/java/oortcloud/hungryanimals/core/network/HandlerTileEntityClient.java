package oortcloud.hungryanimals.core.network;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import oortcloud.hungryanimals.fluids.ModFluids;
import oortcloud.hungryanimals.tileentities.TileEntityBlender;
import oortcloud.hungryanimals.tileentities.TileEntityPowerTransporter;
import oortcloud.hungryanimals.tileentities.TileEntityMillstone;
import oortcloud.hungryanimals.tileentities.TileEntityThresher;
import oortcloud.hungryanimals.tileentities.TileEntityTrough;

public class HandlerTileEntityClient implements IMessageHandler<PacketTileEntityClient, PacketTileEntityServer> {

	@Override
	public PacketTileEntityServer onMessage(PacketTileEntityClient message, MessageContext ctx) {

		if (Minecraft.getMinecraft().theWorld.provider.getDimensionId() == message.dim) {
			TileEntity te = Minecraft.getMinecraft().theWorld.getTileEntity(message.pos);
			if (te != null) {
				switch (message.index) {
				case SyncIndex.IENERGYTRANSPORTER_SYNC_ANGLE:
					((TileEntityPowerTransporter) te).getPowerNetwork().setAngle(message.getFloat());
					break;
				case SyncIndex.IENERGYTRANSPORTER_SYNC_ANGULARVELOCITY:
					((TileEntityPowerTransporter) te).getPowerNetwork().setAngularVelocity(message.getFloat());
					break;
				}
			}

		}
		return null;
	}

}
