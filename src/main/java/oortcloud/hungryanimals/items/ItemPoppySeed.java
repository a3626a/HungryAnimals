package oortcloud.hungryanimals.items;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.blocks.ModBlocks;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;

public class ItemPoppySeed extends Item implements IPlantable {

	public ItemPoppySeed() {
		this.setUnlocalizedName(References.RESOURCESPREFIX + Strings.itemPoppySeedName);
		this.setCreativeTab(HungryAnimals.tabHungryAnimals);
		ModItems.register(this);
	}

	/*
	 * @SideOnly(Side.CLIENT)
	 * 
	 * @Override public void registerIcons(IIconRegister iconRegister) {
	 * this.itemIcon = iconRegister.registerIcon(ModItems
	 * .getUnwrappedUnlocalizedName(super.getUnlocalizedName())); }
	 */

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (side != EnumFacing.UP) {
			return false;
		} else if (playerIn.canPlayerEdit(pos.up(), side, stack)) {
			if (worldIn.getBlockState(pos).getBlock().canSustainPlant(worldIn, pos, EnumFacing.UP, this) && worldIn.isAirBlock(pos.up())) {
				worldIn.setBlockState(pos.up(), ModBlocks.poppy.getDefaultState());
				--stack.stackSize;
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public Block getSoilId() {
		return Blocks.farmland;
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
		return EnumPlantType.Crop;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
		return ModBlocks.poppy.getDefaultState();
	}
}
