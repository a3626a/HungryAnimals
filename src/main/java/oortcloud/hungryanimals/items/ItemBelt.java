package oortcloud.hungryanimals.items;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.blocks.BlockAxle;
import oortcloud.hungryanimals.blocks.ModBlocks;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.energy.PowerNetwork;
import oortcloud.hungryanimals.tileentities.TileEntityAxle;

public class ItemBelt extends Item {

	public static int MAX_LENGTH = 8;

	public ItemBelt() {
		super();
		this.setCreativeTab(HungryAnimals.tabHungryAnimals);
		this.setUnlocalizedName(References.RESOURCESPREFIX + Strings.itemBeltName);
		this.setMaxStackSize(1);
		ModItems.register(this);
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
		ItemStack itemStack = new ItemStack(this,1,16);
		subItems.add(itemStack);
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		tooltip.add("Length: " + stack.getItemDamage() + " m");
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("SelectedBlockPos")) {
			tooltip.add("Connected to: " + BlockPos.fromLong(stack.getTagCompound().getLong("SelectedBlockPos")));
		}
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!TileEntityAxle.isValidAxle(worldIn, pos)) {
			return false;
		}
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("SelectedBlockPos")) {
			BlockPos selectedPos = BlockPos.fromLong(stack.getTagCompound().getLong("SelectedBlockPos"));
			if (pos.equals(selectedPos)) {
				return false;
			}
			if (pos.getY()!=selectedPos.getY()) {
				return false;
			}
			if (!TileEntityAxle.isValidAxle(worldIn, selectedPos)) {
				stack.getTagCompound().removeTag("SelectedBlockPos");
				return false;
			}
			TileEntityAxle axle1 = (TileEntityAxle) worldIn.getTileEntity(selectedPos);
			TileEntityAxle axle2 = (TileEntityAxle) worldIn.getTileEntity(pos);
			if (axle1 == null || axle1.isConnected()) {
				stack.getTagCompound().removeTag("SelectedBlockPos");
				return false;
			}
			if (axle2 == null || axle2.isConnected()) {
				return false;
			}
			double dist = pos.distanceSq(selectedPos);
			int requiredBelt = (int) (Math.ceil(Math.sqrt(dist)));
			if (requiredBelt <= Math.min(stack.getItemDamage(), MAX_LENGTH)) {
				stack.setItemDamage(stack.getItemDamage() - requiredBelt);
				axle1.setConnectedAxle(pos);
				axle2.setConnectedAxle(selectedPos);
				axle1.mergePowerNetwork(new PowerNetwork(0));
				stack.getTagCompound().removeTag("SelectedBlockPos");
				if (stack.getItemDamage() == 0) {
					playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, null);
				}
				return true;
			}
			return false;
		} else {
			TileEntityAxle axle = (TileEntityAxle) worldIn.getTileEntity(pos);
			if (axle != null && !axle.isConnected()) {
				if (!stack.hasTagCompound()) {
					stack.setTagCompound(new NBTTagCompound());
				}
				stack.getTagCompound().setLong("SelectedBlockPos", pos.toLong());
				return true;
			}
			return false;
		}
	}

}
