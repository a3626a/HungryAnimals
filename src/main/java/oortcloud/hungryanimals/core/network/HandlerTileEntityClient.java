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
				case 1:
					((TileEntityPowerTransporter) te).getPowerNetwork().setAngle(message.getFloat());
					break;
				case 4:
					ItemStack[] items2 = message.getItemStackArray();
					for (int i = 0; i < items2.length; i++) {
						((TileEntityMillstone) te).setInventorySlotContents(i, items2[i]);
					}
					break;
				case 5:
					ItemStack[] items3 = message.getItemStackArray();
					for (int i = 0; i < items3.length; i++) {
						((TileEntityBlender) te).setInventorySlotContents(i, items3[i]);
					}
					break;
				case 6:
					((TileEntityPowerTransporter) te).getPowerNetwork().setAngularVelocity(message.getFloat());
					break;
				}
			}

		}
		return null;
	}

}
