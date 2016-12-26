package oortcloud.hungryanimals.items;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.blocks.BlockTrough;
import oortcloud.hungryanimals.blocks.ModBlocks;
import oortcloud.hungryanimals.core.lib.Strings;

public class ItemTrough extends Item {

	public ItemTrough() {
		super();
		setUnlocalizedName(Strings.itemTroughBoxName);
		setCreativeTab(HungryAnimals.tabHungryAnimals);

		ModItems.register(this);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) {
			return EnumActionResult.SUCCESS;
		} else if (side != EnumFacing.UP) {
			return EnumActionResult.FAIL;
		} else {
			IBlockState iblockstate = worldIn.getBlockState(pos);
			Block block = iblockstate.getBlock();
			boolean flag = block.isReplaceable(worldIn, pos);

			if (!flag) {
				pos = pos.up();
			}

			int i = MathHelper.floor_double((double) (playerIn.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
			EnumFacing enumfacing1 = EnumFacing.getHorizontal(i);
			BlockPos blockpos = pos.offset(enumfacing1);

			if (playerIn.canPlayerEdit(pos, side, stack) && playerIn.canPlayerEdit(blockpos, side, stack)) {
				boolean flag1 = worldIn.getBlockState(blockpos).getBlock().isReplaceable(worldIn, blockpos);
				boolean flag2 = flag || worldIn.isAirBlock(pos);
				boolean flag3 = flag1 || worldIn.isAirBlock(blockpos);

				if (flag2 && flag3 && worldIn.getBlockState(pos.down()).isFullyOpaque()
						&& worldIn.getBlockState(blockpos.down()).isFullyOpaque()) {
					IBlockState iblockstate1 = ModBlocks.trough.getDefaultState()
							.withProperty(BlockTrough.FACING, enumfacing1)
							.withProperty(BlockTrough.PART, BlockTrough.EnumPartType.FOOT);

					// TODO what does flag 8 mean?!
					if (worldIn.setBlockState(pos, iblockstate1, 11)) {
						IBlockState iblockstate2 = iblockstate1.withProperty(BlockTrough.PART,
								BlockTrough.EnumPartType.HEAD);
						worldIn.setBlockState(blockpos, iblockstate2, 11);
					}
					--stack.stackSize;
					return EnumActionResult.SUCCESS;
				} else {
					return EnumActionResult.FAIL;
				}
			} else {
				return EnumActionResult.FAIL;
			}
		}
	}

}
