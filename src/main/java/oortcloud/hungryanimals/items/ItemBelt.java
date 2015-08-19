package oortcloud.hungryanimals.items;

import java.util.List;

import net.minecraft.block.state.IBlockState;
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
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		tooltip.add("Length: " + stack.getItemDamage() + " m");
		if (stack.hasTagCompound()&&stack.getTagCompound().hasKey("SelectedBlockPos")) {
			tooltip.add("Connected to: " + BlockPos.fromLong(stack.getTagCompound().getLong("SelectedBlockPos")));
		}
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (isValidAxle(worldIn,pos)) {
			if (stack.hasTagCompound() && stack.getTagCompound().hasKey("SelectedBlockPos")) {
				BlockPos selectedPos = BlockPos.fromLong(stack.getTagCompound().getLong("SelectedBlockPos"));
				if (isValidAxle(worldIn, selectedPos)) {
					TileEntityAxle axle1 = (TileEntityAxle) worldIn.getTileEntity(selectedPos);
					TileEntityAxle axle2 = (TileEntityAxle) worldIn.getTileEntity(pos);
					if (axle1!=null&&axle2!=null&&!isConnected(worldIn, axle1)&&!isConnected(worldIn, axle2)) {
						double dist = pos.distanceSq(selectedPos);
						if (dist <= Math.min(stack.getItemDamage() * stack.getItemDamage(), MAX_LENGTH * MAX_LENGTH)) {
							stack.setItemDamage(stack.getItemDamage() - (int)(Math.ceil(Math.sqrt(dist))));
							axle1.setConnectedAxle(pos);
							axle2.setConnectedAxle(selectedPos);
							stack.getTagCompound().removeTag("SelectedBlockPos");
							if (stack.getItemDamage()==0) {
								playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, null);
							}
						}
					}
					stack.getTagCompound().removeTag("SelectedBlockPos");
				}
				return false;
			} else {
				TileEntityAxle axle = (TileEntityAxle) worldIn.getTileEntity(pos);
				if (axle!=null&&!isConnected(worldIn, axle)) {
					if (!stack.hasTagCompound())
			        {
						stack.setTagCompound(new NBTTagCompound());
			        }
			        stack.getTagCompound().setLong("SelectedBlockPos", pos.toLong());
				}
			}
		}
		return false;
	}
	
	public boolean isValidAxle(World worldIn, BlockPos pos) {
		return worldIn.getBlockState(pos) == ModBlocks.axle.getDefaultState().withProperty(BlockAxle.VARIANT, true);
	}
	
	public boolean isConnected(World worldIn, TileEntityAxle axle) {
		if (axle.getConnectedAxle() == null) {
			return false;
		} else {
			if (!isValidAxle(worldIn, axle.getConnectedAxle())) {
				return false;
			} else {
				TileEntityAxle axleConnected = (TileEntityAxle) worldIn.getTileEntity(axle.getConnectedAxle());
				if (axleConnected == null) {
					return false;
				} else {
					if (axleConnected.getConnectedAxle() == null) {
						return false;
					} else {
						return axle.getPos().equals(axleConnected.getConnectedAxle());
					}
				}
			}
		}
	}
	
}
