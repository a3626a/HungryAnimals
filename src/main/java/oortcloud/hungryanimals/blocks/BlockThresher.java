package oortcloud.hungryanimals.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import oortcloud.hungryanimals.HungryAnimals;
import oortcloud.hungryanimals.core.lib.References;
import oortcloud.hungryanimals.core.lib.Strings;
import oortcloud.hungryanimals.energy.EnergyNetwork;
import oortcloud.hungryanimals.tileentities.TileEntityThresher;

public class BlockThresher extends BlockEnergyTransporter {

	public static final float exhaustion = 0.5F;

	protected BlockThresher() {
		super(Material.wood);

		this.setBlockBounds((float) 0.375, 0, (float) 0.375, (float) 0.625, 1, (float) 0.625);
		this.setUnlocalizedName(References.RESOURCESPREFIX + Strings.blockThresherName);
		this.setCreativeTab(HungryAnimals.tabHungryAnimals);
		ModBlocks.register(this);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityThresher();
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean isNormalCube() {
		return false;
	}

	public boolean isFullCube() {
		return false;
	}

	@Override
	public void divideNetwork(World world, BlockPos pos) {
		Block block;
		block = world.getBlockState(pos.up()).getBlock();
		if (block instanceof BlockEnergyTransporter) {
			((BlockEnergyTransporter) block).setNetwork(world, pos.up(), new EnergyNetwork(0));
		}
		block = world.getBlockState(pos.down()).getBlock();
		if (block instanceof BlockEnergyTransporter) {
			((BlockEnergyTransporter) block).setNetwork(world, pos.down(), new EnergyNetwork(0));
		}
	}

	@Override
	public boolean isTowards(World world, BlockPos pos, EnumFacing side) {
		return side == EnumFacing.DOWN || side == EnumFacing.UP;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntityThresher tileEntity = (TileEntityThresher) worldIn.getTileEntity(pos);

		ItemStack itemStackThresher = tileEntity.getStackInSlot(0);
		ItemStack itemStackPlayer = playerIn.getHeldItem();
		if (itemStackThresher != null && itemStackPlayer == null) {
			if (tileEntity.canTakeOut()) {
				playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, itemStackThresher);
				tileEntity.setInventorySlotContents(0, null);
				return true;
			} else if (itemStackThresher.stackSize > 1) {
				ItemStack split = itemStackThresher.splitStack(itemStackThresher.stackSize-1);
				playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, split);
				return true;
			}
		}
		if (itemStackThresher == null && itemStackPlayer != null) {
			playerIn.inventory.setInventorySlotContents(playerIn.inventory.currentItem, null);
			tileEntity.setInventorySlotContents(0, itemStackPlayer);
			return true;
		}
		return false;
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
