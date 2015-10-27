package oortcloud.hungryanimals.core.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.network.PacketPlayerServer;
import oortcloud.hungryanimals.core.network.SyncIndex;

public class ForgeEventHandler {

	@SubscribeEvent
	public void onPlayerPlacePoppy(PlayerInteractEvent event) {
		if (event.action == Action.RIGHT_CLICK_BLOCK) {
			EntityPlayer player = event.entityPlayer;
			ItemStack item = player.getHeldItem();
			BlockPos pos = event.pos.offset(event.face);

			boolean flag1 = item != null && item.getItem() == ItemBlock.getItemFromBlock(Blocks.red_flower);
			boolean flag2 = event.world.getBlockState(pos.down()).getBlock() == Blocks.farmland;
			boolean flag3 = event.world.isAirBlock(pos);

			if (flag1 && flag2 && flag3) {
				PacketPlayerServer msg = new PacketPlayerServer(SyncIndex.PLANTPOPPY,player.getName());
				msg.setInt(event.world.provider.getDimensionId());
				msg.setInt(pos.getX());
				msg.setInt(pos.getY());
				msg.setInt(pos.getZ());
				HungryAnimals.simpleChannel.sendToServer(msg);
				event.setCanceled(true);
			}
		}
	}

}
