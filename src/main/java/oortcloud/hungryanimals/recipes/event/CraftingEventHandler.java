package oortcloud.hungryanimals.recipes.event;

import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import oortcloud.hungryanimals.items.ModItems;
import oortcloud.hungryanimals.recipes.RecipeAnimalGlue;

public class CraftingEventHandler {

	@SubscribeEvent
	public void createHideGlue(RightClickBlock event) {
		World world = event.getWorld();
		if (event.getSide() == Side.SERVER) {
			
			EntityPlayer player = event.getEntityPlayer();
			ItemStack item = event.getItemStack();
			BlockPos pos = event.getPos();
			IBlockState state = world.getBlockState(pos);
			int level;
			if (item != null && state.getBlock() == Blocks.CAULDRON && (level = (Integer)state.getValue(BlockCauldron.LEVEL)) > 0) {
				int num = RecipeAnimalGlue.getRecipe(item);
				if (num != 0) {
					world.spawnEntityInWorld(new EntityItem(world, pos.getX()+0.5F, pos.getY()+0.5F, pos.getZ()+0.5F, new ItemStack(ModItems.animalGlue, num)));
                    if (--item.stackSize == 0)
                    	player.inventory.deleteStack(item);
					world.setBlockState(pos, state.withProperty(BlockCauldron.LEVEL, level-1));
				}
			}
		}
	}

}
