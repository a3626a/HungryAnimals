package oortcloud.hungryanimals.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.core.network.PacketTileEntityServer;
import oortcloud.hungryanimals.energy.EnergyNetwork;
import oortcloud.hungryanimals.tileentities.TileEntityBlender;

public class BlockBlender extends BlockEnergyTransporter {

	protected BlockBlender() {
		super(Material.iron);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);

		this.setUnlocalizedName(References.RESOURCESPREFIX + Strings.blockBlenderName);
		this.setCreativeTab(HungryAnimals.tabHungryAnimals);
		ModBlocks.register(this);
	}

	@Override
	public TileEntity createNewTileEntity(World wolrd, int meta) {
		return new TileEntityBlender();
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean isNormalCube() {
		return false;
	}

	@Override
	public void divideNetwork(World world, BlockPos pos) {
		Block block;
		block = world.getBlockState(pos.up()).getBlock();
		if (block instanceof BlockEnergyTransporter) {
			((BlockEnergyTransporter) block).setNetwork(world, pos.up(), new EnergyNetwork(0));
		}
	}

	@Override
	public boolean isTowards(World world, BlockPos pos, EnumFacing side) {
		return side == EnumFacing.UP;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (side == EnumFacing.UP) {
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if (tileEntity != null) {
				TileEntityBlender blender = (TileEntityBlender) tileEntity;
				float angle = blender.getNetwork().getAngle(0.0F);
				int rotationalOffset = (int) (angle / 90);
				angle = angle % 90;
				int index = 0;
				boolean flag1;
				boolean flag2;
				hitX -= 0.5;
				hitZ -= 0.5;
				if (angle == 0) {
					flag1 = hitZ > 0;
					flag2 = hitX > 0;
				} else {
					flag1 = hitZ > Math.tan(Math.toRadians(angle)) * hitX;
					flag2 = hitZ > Math.tan(Math.toRadians(angle + 90)) * hitX;
				}
				if (flag1) {
					if (flag2) {
						index = 0;
					} else {
						index = 1;
					}
				} else {
					if (flag2) {
						index = 3;
					} else {
						index = 2;
					}
				}
				index = (index - rotationalOffset + 4) % 4;
				ItemStack itemStackBlender = blender.getStackInSlot(index);
				ItemStack itemStackPlayer = playerIn.getCurrentEquippedItem();
				if (itemStackBlender != null && itemStackPlayer == null) {
					playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, itemStackBlender);
					blender.setInventorySlotContents(index, null);
					return true;
				}
				if (itemStackBlender == null && itemStackPlayer != null) {
					playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, null);
					blender.setInventorySlotContents(index, itemStackPlayer);
					return true;
				}
				if (itemStackBlender != null && itemStackPlayer != null) {
					if (itemStackBlender.isItemEqual(itemStackPlayer)) {
						int space = itemStackBlender.getMaxStackSize()-itemStackBlender.stackSize;
						if (space >= itemStackPlayer.stackSize) {
							itemStackBlender.stackSize+=itemStackPlayer.stackSize;
							playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, null);
						} else {
							itemStackBlender.stackSize=itemStackBlender.getMaxStackSize();
							itemStackPlayer.stackSize-=space;
						}
						return true;
					}
					return false;
				}
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof IInventory)
        {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
        }

        super.breakBlock(worldIn, pos, state);
    }
}
