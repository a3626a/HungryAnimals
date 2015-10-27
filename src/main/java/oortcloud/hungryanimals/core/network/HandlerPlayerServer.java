package oortcloud.hungryanimals.core.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import oortcloud.hungryanimals.blocks.BlockPoppy;
import oortcloud.hungryanimals.blocks.ModBlocks;
import oortcloud.hungryanimals.items.ModItems;

public class HandlerPlayerServer implements IMessageHandler<PacketPlayerServer, PacketPlayerClient> {

	@Override
	public PacketPlayerClient onMessage(PacketPlayerServer message, MessageContext ctx) {
		EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(message.name);
		ItemStack stack = player.getHeldItem();
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
		case SyncIndex.PLANTPOPPY:
			int dim = message.getInt();
			BlockPos pos = new BlockPos(message.getInt(), message.getInt(), message.getInt());
			
			World world = MinecraftServer.getServer().worldServerForDimension(dim);
			
			boolean flag1 = stack != null && stack.getItem() == ItemBlock.getItemFromBlock(Blocks.red_flower);
			boolean flag2 = world.getBlockState(pos.down()).getBlock() == Blocks.farmland;
			boolean flag3 = world.isAirBlock(pos);
			
			if (flag1 && flag2 && flag3) {
				world.setBlockState(pos, ModBlocks.poppy.getDefaultState().withProperty(BlockPoppy.AGE, 4), 2);
				stack.stackSize--;
				if (stack.stackSize == 0) {
					player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
				}
			}
			break;
		}

		return null;
	}

}
