package oortcloud.hungryanimals.recipes.event;

import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import oortcloud.hungryanimals.items.ModItems;
import oortcloud.hungryanimals.recipes.RecipeAnimalGlue;

public class CraftingEventHandler {

	@SubscribeEvent
	public void createHideGlue(PlayerInteractEvent event) {
		World world = event.world;
		if (!world.isRemote && event.action == Action.RIGHT_CLICK_BLOCK) {
			EntityPlayer player = event.entityPlayer;
			ItemStack item = player.getHeldItem();
			BlockPos pos = event.pos;
			IBlockState state = world.getBlockState(pos);
			int level;
			if (item != null && state.getBlock() == Blocks.cauldron && (level = (Integer)state.getValue(BlockCauldron.LEVEL)) > 0) {
				int num = RecipeAnimalGlue.getRecipe(item);
				if (num != 0) {
					event.world.spawnEntityInWorld(new EntityItem(world, pos.getX()+0.5F, pos.getY()+0.5F, pos.getZ()+0.5F, new ItemStack(ModItems.animalGlue, num)));
					player.inventory.decrStackSize(player.inventory.currentItem, 1);
					world.setBlockState(pos, state.withProperty(BlockCauldron.LEVEL, level-1));
				}
			}
		}
	}

}
